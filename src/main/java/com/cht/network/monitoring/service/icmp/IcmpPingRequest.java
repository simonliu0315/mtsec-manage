package com.cht.network.monitoring.service.icmp;

public class IcmpPingRequest {

    // source: the ip of the source interface to use for sending the packet.
    // host: the host name or ipv4 address of the destination to ping
    // ttl: the TTL as defined by ICMP
    // packetsize: the packet size as defined by ICMP
    // timeout: max time to wait, milliseconds, for the response to return
    // charsetName: the charsetName to use for parsing output when native ping processes are spawned
    private String source;
    private String host;
    private int ttl;
    private int packetSize;
    private long timeout;
    private String charsetName;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public int getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getCharsetName() {
        return charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    @Override
    public String toString() {
        return "IcmpPingRequest{" +
                "source='" + source + '\'' +
                ", host='" + host + '\'' +
                ", ttl=" + ttl +
                ", packetSize=" + packetSize +
                ", timeout=" + timeout +
                ", charsetName='" + charsetName + '\'' +
                '}';
    }
}
