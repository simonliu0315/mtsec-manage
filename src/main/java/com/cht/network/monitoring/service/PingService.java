package com.cht.network.monitoring.service;

import com.cht.network.monitoring.service.icmp.IcmpPingRequest;
import com.cht.network.monitoring.service.icmp.IcmpPingResponse;
import com.cht.network.monitoring.util.ProcessUtil;
import com.cht.network.monitoring.util.StringUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

@Component
public class PingService {

    private static final Logger log = LoggerFactory.getLogger(PingService.class);

    public boolean PingReachable(String host) {
        log.info("Pinging IP: " + host);
        try {
            //DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(host);
            boolean reachable = address.isReachable(5000);
            log.info("reachable: " + reachable);
            return reachable;
        } catch(Exception e) {
            log.error("", e);
        }
        return false;
    }

    public IcmpPingResponse Ping(String host) {
        IcmpPingRequest request = new IcmpPingRequest();
        request.setHost (host);
        request.setCharsetName("Big5");
        request.setPacketSize (32);
        request.setTimeout (5000);
        request.setTtl (255);
        //   os.name
        //   AIX
        //   Digital Unix
        //   FreeBSD
        //   HP UX
        //   Irix
        //   Linux
        //   Mac OS
        //   Mac OS X
        //   MPE/iX
        //   Netware 4.11
        //   OS/2
        //   Solaris
        //   Windows 2000
        //   Windows 95
        //   Windows 98
        //   Windows NT
        //   Windows Vista
        //   Windows XP
        //   Windows 11
        final String osName = System.getProperty ("os.name");
        String command = "ping -4 -c 4 " + host;
        if ("Linux".equals (osName)) {
            //command = "ping -c 1 -s " + packetSize + " -w " + timeoutAsSeconds + " " + host;
        } else if (osName.startsWith ("Windows")) {
            // execute the ping command:
            //   ping -n 1 -l 32 -w 1000 www.google.com
            //   ping -n 1 -l 32 -w 1000 -S 192.168.1.7 www.google.com
            // note that the timeout is in milliseconds
            command = "ping "+
                    " -4 -n 4 " + (request.getPacketSize() > 0 ? "-l " + request.getPacketSize() + " " : "") +
                    (request.getTimeout() > 0 ? "-w " + request.getTimeout()  + " " : "") +
                    (request.getSource() != null ? "-S " + request.getSource() + " " : "") +
                    host;
        } else {
            command = "ping -c 4 " + host;
        }
        final long icmpSendEchoStartTime = System.currentTimeMillis ();
        final List<String> stringList = ProcessUtil.executeProcessAndGetOutputAsStringList (command, request.getCharsetName());
        final long icmpSendEchoDuration = System.currentTimeMillis () - icmpSendEchoStartTime;

        // check for timeout
        final boolean timeoutFlag = icmpSendEchoDuration >= request.getTimeout();
        if (timeoutFlag) {
            final IcmpPingResponse response = new IcmpPingResponse ();
            response.setErrorMessage ("Timeout reached after " + icmpSendEchoDuration + " msecs");
            response.setSuccessFlag (false);
            response.setTimeoutFlag (true);
            // done
            return response;
        }
        log.info("stringList {}", stringList.size());

        final IcmpPingResponse response = executePingRequest (stringList);
        response.setCommand (command);
        response.setOutput (StringUtil.joinByNewLine (stringList));

        log.info("response {}", response);
        // done
        return response;
    }

    /**
     * Executes the given request
     * @param stringList
     * @return IcmpEchoResponse
     */
    public IcmpPingResponse executePingRequest (final List<String> stringList) {

        // look for the first line with some output
        // sample output from win7 to a valid host
        //   c:\dev\icmp4j\trunk>ping -n 1 -l 32 -w 5 www.google.com
        //
        //   Pinging www.google.com [216.58.217.196] with 32 bytes of data:
        //   Reply from 216.58.217.196: bytes=32 time=12ms TTL=56
        //
        //   Ping statistics for 216.58.217.196:
        //       Packets: Sent = 1, Received = 1, Lost = 0 (0% loss),
        //   Approximate round trip times in milli-seconds:
        //       Minimum = 12ms, Maximum = 12ms, Average = 12ms
        //
        // sample output from win7 to localohost where time< instead of time=
        //   c:\dev\icmp4j\trunk\icmp4j\test>ping -n 1 -l 32 -w 5 127.0.0.1
        //
        //   Pinging 127.0.0.1 with 32 bytes of data:
        //   Reply from 127.0.0.1: bytes=32 time<1ms TTL=128
        //
        //   Ping statistics for 127.0.0.1:
        //       Packets: Sent = 1, Received = 1, Lost = 0 (0% loss),
        //   Approximate round trip times in milli-seconds:
        //       Minimum = 0ms, Maximum = 0ms, Average = 0ms
        //
        // sample output from win7 to non-existing host
        //   c:\dev\icmp4j\trunk>ping -n 1 -l 32 -w 5 www.googgle.com
        //   Ping request could not find host www.googgle.com. Please check the name and try again.
        // objectify
        IcmpPingResponse response = new IcmpPingResponse ();
        for (final String string : stringList) {

            // this is what we are looking for:
            // Reply from 216.58.217.196: bytes=32 time=12ms TTL=56

            // discriminate against non-ping lines
            if (!(string.startsWith ("Reply from ") || string.startsWith ("回覆自 "))) {
                continue;
            }

            int size = 0;
            // parse response
            String sizeAsString = StringUtil.parseSequentialDigits (
                    string,
                    "bytes=");
            if (sizeAsString == null) {
                sizeAsString = StringUtil.parseSequentialDigits (
                        string,
                        "位元組=");
            }
            size = Integer.parseInt (StringUtils.defaultIfBlank(sizeAsString, "0"));

            String responseAddress = StringUtil.parseString (
                    string,
                    "from ",
                    ":");
            if (responseAddress == null) {
                responseAddress = StringUtil.parseString (
                        string,
                        "回覆自 ",
                        ":");
            }

            String ttlAsString = StringUtil.parseSequentialDigits (
                    string,
                    "TTL=");
            final int ttl = Integer.parseInt (StringUtils.defaultIfBlank(ttlAsString, "0"));

            String rttAsStringEquals = StringUtil.parseSequentialDigits (
                    string,
                    "time=");
            if (rttAsStringEquals == null) {
                rttAsStringEquals = StringUtil.parseSequentialDigits (
                        string,
                        "時間=");
            }
            String rttAsStringLessThan = StringUtil.parseSequentialDigits (
                    string,
                    "time<");
            if (rttAsStringLessThan == null) {
                rttAsStringLessThan = StringUtil.parseSequentialDigits (
                        string,
                        "時間<");
            }
            final String rttAsString = rttAsStringEquals != null ?
                    rttAsStringEquals :
                    rttAsStringLessThan;
            final Float rttAsFloat = Float.parseFloat (rttAsString);
            final int rtt = rttAsFloat.intValue ();



            response.setHost (responseAddress);
            response.setErrorMessage (null);
            response.setRtt (rtt);
            response.setSize (size);
            response.setSuccessFlag (true);
            response.setTtl (ttl);

            // done
            //return response;
        }
        for (final String string : stringList) {
            if (!(string.indexOf ("Packets") > 0 || string.indexOf ("封包") > 0 ) ) {
                continue;
            }
            //統計
            int sent = -1;
            String sentAsString = StringUtil.parseSequentialDigits (
                    string,
                    "Sent = ");
            if (sentAsString == null) {
                sentAsString = StringUtil.parseSequentialDigits (
                        string,
                        "已傳送 = ");
                log.info("string {} 已傳送 {}", string, sentAsString);
            }
            sent = Integer.parseInt (StringUtils.defaultIfBlank(sentAsString, "-1"));

            int received = -1;
            String receivedAsString = StringUtil.parseSequentialDigits (
                    string,
                    "Received = ");
            if (receivedAsString == null) {
                receivedAsString = StringUtil.parseSequentialDigits (
                        string,
                        "已收到 = ");
            }
            received = Integer.parseInt (StringUtils.defaultIfBlank(receivedAsString, "-1"));
            int lost = -1;
            String lostAsString = StringUtil.parseSequentialDigits (
                    string,
                    "Lost = ");
            if (lostAsString == null) {
                lostAsString = StringUtil.parseSequentialDigits (
                        string,
                        "已遺失 = ");
            }
            lost = Integer.parseInt (StringUtils.defaultIfBlank(lostAsString, "-1"));

            response.setStatistics(new IcmpPingResponse.Statistics());
            response.getStatistics().setSent(sent);
            response.getStatistics().setReceived(received);
            response.getStatistics().setLost(lost);

        }

        for (final String string : stringList) {
            if (!(string.indexOf ("Minium ") > 0 || string.indexOf ("最小值 ") > 0)) {
                continue;
            }

            //RTT
            int minium = -1;
            String miniumAsString = StringUtil.parseSequentialDigits (
                    string,
                    "Minium = ");
            if (miniumAsString == null) {
                miniumAsString = StringUtil.parseSequentialDigits (
                        string,
                        "最小值 = ");
            }
            minium = Integer.parseInt (StringUtils.defaultIfBlank(miniumAsString, "-1"));

            int maxium = -1;
            String maxiumAsString = StringUtil.parseSequentialDigits (
                    string,
                    "最大值 = ");
            if (maxiumAsString == null) {
                maxiumAsString = StringUtil.parseSequentialDigits (
                        string,
                        "Maxium = ");
            }
            maxium = Integer.parseInt (StringUtils.defaultIfBlank(maxiumAsString, "-1"));
            int average = -1;
            String averageAsString = StringUtil.parseSequentialDigits (
                    string,
                    "Average = ");
            if (averageAsString == null) {
                averageAsString = StringUtil.parseSequentialDigits (
                        string,
                        "平均 = ");
            }
            average = Integer.parseInt (StringUtils.defaultIfBlank(averageAsString, "-1"));

            response.setRoundTripTimes(new IcmpPingResponse.RoundTripTimes());
            response.getRoundTripTimes().setMinium(minium);
            response.getRoundTripTimes().setMaxium(maxium);
            response.getRoundTripTimes().setAverage(average);

            return response;
        }

        // not found - if there is at least one line, use that as the error message
        // noinspection LoopStatementThatDoesntLoop
        for (final String string : stringList) {

            // objectify
            response = new IcmpPingResponse ();
            response.setErrorMessage (string);
            response.setSuccessFlag (false);

            // done
            return response;
        }

        // no results found
        {
            // objectify
            response = new IcmpPingResponse ();
            response.setErrorMessage ("No results could be parsed");
            response.setSuccessFlag (false);

            // done
            return response;
        }
    }
}
