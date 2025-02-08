package com.cht.network.monitoring.service;

import com.cht.network.monitoring.dto.SnmpWalkDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class SnmpService {

    private static final Logger log = LoggerFactory.getLogger(SnmpService.class);

    private static final int snmpVersion = SnmpConstants.version2c;
    private int snmpTimeout = 500;
    private int numRetries = 2;

    public List<SnmpWalkDto> doSNMPBulkWalk(String ipAddr, String port, String commStr, String bulkOID, String operation) throws Exception {
        //testTableUtils(ipAddr, "1.3.6.1.2.1.2.2.1");
        log.info("**********************************");
        List<SnmpWalkDto> dtos = new ArrayList<>();

        Snmp snmp = null;

        snmp = new Snmp(new DefaultUdpTransportMapping());

        UserTarget targetV3 = null;
        CommunityTarget targetV2 = null;
        UsmUser user = null;
        PDU request = null;
        snmp.listen();
        Address add = new UdpAddress(ipAddr + "/" + port);

        if (snmpVersion == SnmpConstants.version2c || snmpVersion == SnmpConstants.version1) {
            targetV2 = new CommunityTarget();
            targetV2.setCommunity(new OctetString(commStr));
            targetV2.setAddress(add);
            targetV2.setTimeout(snmpTimeout);
            targetV2.setRetries(numRetries);
            targetV2.setVersion(snmpVersion);
            targetV2.setMaxSizeRequestPDU(65535);
        }

        if (snmpVersion == SnmpConstants.version2c) {
            request = new PDU();
            //request.setMaxRepetitions(100);
            //request.setNonRepeaters(0);
        }

        request.setType(PDU.GET);
        OID oID = new OID(bulkOID);
        request.add(new VariableBinding(oID));
        OID rootOID = request.get(0).getOid();
        VariableBinding vb, ar[];
        List<TreeEvent> l = null;
        TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
        if (snmpVersion == SnmpConstants.version2c) {
            targetV2.setCommunity(new OctetString(commStr));
            if (operation.equalsIgnoreCase("bulkwalk")) {
                OID[] rootOIDs = new OID[1];
                rootOIDs[0] = rootOID;
                l = treeUtils.walk(targetV2, rootOIDs);
            } else {
                l = treeUtils.getSubtree(targetV2, rootOID);
            }

        }
        //System.out.println(l);
        log.info("size="+l.size());
        for(TreeEvent t : l){
            log.info("QQ {} {}" , t.getUserObject(), t.getSource().toString());
            if (t.getVariableBindings() == null) {
                break;
            }

            for(VariableBinding vbx : t.getVariableBindings()) {
                 //testTableByGetNext(ipAddr, vbx.getOid().toString());
                SnmpWalkDto dto = new SnmpWalkDto();
                if ("OCTET STRING".equals(vbx.getVariable().getSyntaxString())) {
                    log.info("1. oid {} ,v {}, vss {}", vbx.getOid().toString(), vbx.getVariable().toString(), vbx.getVariable().getSyntaxString());
                    log.info("2. oid {} ,v {}, vss {}", vbx.getOid().toString(), h(vbx.getVariable().toString()), vbx.getVariable().getSyntaxString());
                    dto.setOid(vbx.getOid().toString());
                    dto.setVariable(h(vbx.getVariable().toString()));
                    dto.setVariableSyntax(vbx.getVariable().getSyntaxString());
                } else {
                    log.info("1. Syntax {}, oid {} ,v {}, vss {}, ValueString {}", vbx.getSyntax(), vbx.getOid().toString(), vbx.getVariable().toString(), vbx.getVariable().getSyntaxString(), vbx.toValueString());
                    dto.setOid(vbx.getOid().toString());
                    dto.setVariable(vbx.getVariable().toString());
                    dto.setVariableSyntax(vbx.getVariable().getSyntaxString());
                }
                dtos.add(dto);
            }
//            VariableBinding[] vbs= t.getVariableBindings();
//            for (int i = 0; (vbs != null) && i < vbs.length; i++) {
//                vb = vbs[i];
//                String s = vb.toString();
//                log.info(s);
//            }
        }
        snmp.close();
        return dtos;
    }

    public String h(String hexStringInput) {
        try {
            String hexString = hexStringInput.replaceAll(":", "").replaceAll("00", "");
            if (hexString.length() == 12) {
                return hexStringInput;
            }
            byte[] bytes = new byte[hexString.length() / 2];

            for (int i = 0; i < bytes.length ; i++) {
                String hex = hexString.substring(i * 2, i * 2 + 2);
                int value = Integer.parseInt(hex, 16);
                if (value >= -128 && value <= 127) {
                    bytes[i] = (byte) value;
                } else {
                    log.warn("Value out of range: {}",  hex);
                    // 您可以選擇在此處處理溢出情況，比如用0代替，或者拋出異常
                    //bytes[i] = 0;
                    return hexStringInput;
                }
            }
            return new String(bytes, StandardCharsets.UTF_8);
        } catch(Exception e) {
            return hexStringInput;
        }

    }

    public PDU snmpGet(String ipAddr, String port, String oid) throws IOException {
        log.info("********************************************");
        Snmp snmp = null;

        snmp = new Snmp(new DefaultUdpTransportMapping());
        snmp.listen();
        Address add = new UdpAddress(ipAddr + "/" + port);

        CommunityTarget targetV2 = new CommunityTarget();
        targetV2.setCommunity(new OctetString("public"));
        targetV2.setAddress(new UdpAddress( add + "/161"));
        targetV2.setTimeout(snmpTimeout);
        targetV2.setRetries(numRetries);
        targetV2.setVersion(snmpVersion);
        targetV2.setMaxSizeRequestPDU(65535);

        //request = new PDU();

        //ScopedPDU pdu = new ScopedPDU();
        PDU pdu = new PDU();
        pdu.setType(PDU.GET);
        pdu.add(new VariableBinding(new OID(oid)));

        ResponseEvent responseEvent = snmp.send(pdu, targetV2);
        PDU response = responseEvent.getResponse();
        if(response == null){
            log.warn("response null - error:{} peerAddress:{} source:{} request:{}",
                    responseEvent.getError(),
                    responseEvent.getPeerAddress(),
                    responseEvent.getSource(),
                    responseEvent.getRequest());
        } else {
                for (int i = 0; i < response.getErrorIndex(); i++) {
                    VariableBinding vb = response.getVariableBindings().get(i);
                    System.out.println(vb.toString());
                }
            log.info("1====> {}",response.getAll().size());
            log.info("2====> {}",response.getVariableBindings().size());
            for (int i = 0; i < response.getVariableBindings().size(); i++) {
                VariableBinding vb = response.getVariableBindings().get(i);
                OID o = vb.getOid();
                Variable variable = vb.getVariable();
                log.info("oid {} vb {} syntax {}",o.toString(), vb.toString(), vb.getSyntax());
                log.info("o====> {}",o.toString());
                log.info("v {}", variable);
                // 匹配 OID 并提取值
                if (o.toString().equals("1.3.6.1.2.1.2.2.1.1")) { // ifIndex
                    int ifIndex = ((Integer32) variable).getValue();
                    // ... 将 ifIndex 赋值给你的 NetworkInterface 对象
                } else if (o.toString().equals("1.3.6.1.2.1.2.2.1.7.1")) {
                    // ... 处理 ifDescr
                    log.info("variable {}", variable.toString());
                    //int ifIndex = ((Integer32) variable).getValue();
                }
            }

        }
        log.info("********************************************");
        return response;
    }


    /**
     * 使用TableUtils方式获取表格的所有元素
     */
    public void testTableUtils(String add, String oid){
        Snmp snmp = null;
        TransportMapping transport = null;
        try {
            transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();

        } catch (IOException e) {

        }
        CommunityTarget targetV2 = new CommunityTarget();
        targetV2.setCommunity(new OctetString("public"));
        targetV2.setAddress(new UdpAddress( add + "/161"));
        targetV2.setTimeout(snmpTimeout);
        targetV2.setRetries(numRetries);
        targetV2.setVersion(snmpVersion);
        targetV2.setMaxSizeRequestPDU(65535);


        //GETNEXT or GETBULK
        TableUtils utils = new TableUtils(snmp, new DefaultPDUFactory(PDU.GETBULK));
        //only for GETBULK, set max-repetitions, default is 10
        utils.setMaxNumRowsPerPDU(2);
        OID[] columnOids = new OID[] {
                new OID(oid)
        };
        // If not null, all returned rows have an index in a range (lowerBoundIndex, upperBoundIndex]
        //第3个参数是最小列oid索引，第四个参数是最大列oid索引
        List<TableEvent> list = utils.getTable(targetV2, columnOids, new OID("1"), new OID("10"));
        if(list == null || list.size() <= 0){
            log.warn("list is null or empty");
            return;
        }
        log.info("list size : "+ list.size());
        List<SnmpWalkDto> dtos = new ArrayList<>();
        for (TableEvent event : list) {
            log.info("{}", event);
        }
    }

    /**
     * 通过getNext遍历获取表格所有元素
     * @return
     */
    public void testTableByGetNext(String add, String oid)  {

        Snmp snmp = null;
        TransportMapping transport = null;
        PDU pdu = null;
        OID targetOID = new OID(oid);
        try {
            transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();

        } catch (IOException e) {

        }
        CommunityTarget targetV2 = new CommunityTarget();
        targetV2.setCommunity(new OctetString("public"));
        targetV2.setAddress(new UdpAddress( add + "/161"));
        targetV2.setTimeout(snmpTimeout);
        targetV2.setRetries(numRetries);
        targetV2.setVersion(snmpVersion);
        targetV2.setMaxSizeRequestPDU(65535);
        try{

            pdu = new PDU();
            log.info("targetOID {}", targetOID);
            pdu.add(new VariableBinding(targetOID));
            boolean finished = false;
            //walk操作
            while (!finished) {
                VariableBinding vb = null;
                // 向Agent发送PDU实施getNext操作，并接收Response
                ResponseEvent respEvent = snmp.getNext(pdu, targetV2);
                // 解析Response数据
                PDU response = respEvent.getResponse();
                log.info("response {}", response);
                if (null == response) {
                    finished = true;
                    break;
                }
                vb = response.get(0);
                // 检查是否结束
                finished = checkWalkFinished(targetOID, pdu, vb);
                log.info("finished {}", finished);
                if (!finished) {
                    System.out.println(vb.getOid() + "="+  vb.getVariable() +" ," + vb.getVariable().getSyntaxString());
                    // 将variable binding设置到下一个
                    pdu.setRequestID(new Integer32(0));
                    pdu.set(0, vb);

                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(snmp !=null){
                try {
                    snmp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 使用TableUtils方式获取表格的所有元素
     */
    public List<SnmpWalkDto> snmpTableUtils(String address, String oid){
        Snmp snmp = null;
        TransportMapping transport = null;
        try {
            transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();

        } catch (IOException e) {

        }
        CommunityTarget targetV2 = new CommunityTarget();
        targetV2.setCommunity(new OctetString("public"));
        targetV2.setAddress(new UdpAddress( address + "/161"));
        targetV2.setTimeout(snmpTimeout);
        targetV2.setRetries(numRetries);
        targetV2.setVersion(snmpVersion);
        targetV2.setMaxSizeRequestPDU(65535);


        //GETNEXT or GETBULK
        TableUtils utils = new TableUtils(snmp, new DefaultPDUFactory(PDU.GETBULK));
        //only for GETBULK, set max-repetitions, default is 10
        utils.setMaxNumRowsPerPDU(2);
        OID[] columnOids = new OID[] {
                new OID(oid)
        };
        // If not null, all returned rows have an index in a range (lowerBoundIndex, upperBoundIndex]
        //第3个参数是最小列oid索引，第四个参数是最大列oid索引
        List<TableEvent> list = utils.getTable(targetV2, columnOids, new OID("1"), new OID("10"));
        if(list == null || list.size() <= 0){
            log.warn("list is null or empty");
            return null;
        }
        log.info("list size : "+ list.size());
        List<SnmpWalkDto> dtos = new ArrayList<>();
        for (TableEvent event : list) {
            //log.info("{}", event);
            SnmpWalkDto dto = new SnmpWalkDto();
            if(event.getIndex().toString().indexOf("1.") == 0){
                //ifIndex
                dto.setName("ifIndex");
                dto.setOid(event.getColumns()[0].getOid().toString());
                dto.setVariable(event.getColumns()[0].getVariable().toString());
                dto.setVariableSyntax(event.getColumns()[0].getVariable().getSyntaxString());
                log.info("dto {}", dto);
                dtos.add(dto);
            } else if(event.getIndex().toString().indexOf("2.") == 0){
                //ifDescr
                dto.setName("ifDescr");
                dto.setOid(event.getColumns()[0].getOid().toString());
                dto.setVariable(event.getColumns()[0].getVariable().toString());
                dto.setVariableSyntax(event.getColumns()[0].getVariable().getSyntaxString());
                if ("OCTET STRING".equals(event.getColumns()[0].getVariable().getSyntaxString())) {
                    dto.setVariable(h(event.getColumns()[0].getVariable().toString()));
                }
                log.info("dto {}", dto);
                dtos.add(dto);
            } else if(event.getIndex().toString().indexOf("3.") == 0){
                //ifDescr
                dto.setName("ifType");
                dto.setOid(event.getColumns()[0].getOid().toString());
                dto.setVariable(event.getColumns()[0].getVariable().toString());
                dto.setVariableSyntax(event.getColumns()[0].getVariable().getSyntaxString());
                if ("OCTET STRING".equals(event.getColumns()[0].getVariable().getSyntaxString())) {
                    dto.setVariable(h(event.getColumns()[0].getVariable().toString()));
                }
                log.info("dto {}", dto);
                dtos.add(dto);
            } else if(event.getIndex().toString().indexOf("5.") == 0){
                //ifDescr
                dto.setName("ifSpeed");
                dto.setOid(event.getColumns()[0].getOid().toString());
                dto.setVariable(event.getColumns()[0].getVariable().toString());
                dto.setVariableSyntax(event.getColumns()[0].getVariable().getSyntaxString());
                if ("OCTET STRING".equals(event.getColumns()[0].getVariable().getSyntaxString())) {
                    dto.setVariable(h(event.getColumns()[0].getVariable().toString()));
                }
                log.info("dto {}", dto);
                dtos.add(dto);
            } else if(event.getIndex().toString().indexOf("8.") == 0){
                //ifDescr
                dto.setName("ifOperStatus");
                dto.setOid(event.getColumns()[0].getOid().toString());
                dto.setVariable(event.getColumns()[0].getVariable().toString());
                dto.setVariableSyntax(event.getColumns()[0].getVariable().getSyntaxString());
                if ("OCTET STRING".equals(event.getColumns()[0].getVariable().getSyntaxString())) {
                    dto.setVariable(h(event.getColumns()[0].getVariable().toString()));
                }
                log.info("dto {}", dto);
                dtos.add(dto);
            }

        }
        return dtos;
    }


    /**
     * 检查walk是否结束
     * @param targetOID
     * @param pdu
     * @param vb
     * @return
     */
    private static boolean checkWalkFinished(OID targetOID, PDU pdu, VariableBinding vb) {
        boolean finished = false;
        if (pdu.getErrorStatus() != 0) {
            finished = true;
        } else if (vb.getOid() == null) {
            finished = true;
        } else if (vb.getOid().size() < targetOID.size()) {
            finished = true;
        } else if (targetOID.leftMostCompare(targetOID.size(), vb.getOid()) != 0) {
            finished = true;
        } else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
            finished = true;
        } else if (vb.getOid().compareTo(targetOID) <= 0) {
            finished = true;
        }
        return finished;
    }


}
