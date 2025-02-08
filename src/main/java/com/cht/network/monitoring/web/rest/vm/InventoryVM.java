package com.cht.network.monitoring.web.rest.vm;

import com.cht.network.monitoring.domain.Inventory;
import com.cht.network.monitoring.dto.InventoryDto;
import com.cht.network.monitoring.service.icmp.IcmpPingResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

public class InventoryVM extends InventoryDto {

    @Schema(description = "素材管理查詢任務Request" +
            "目的: 依據素材名稱、素材描述，進行素材查詢，顯示於素材清單列表 (應含未刪除註記、最新版次、最新版本日期)" +
            "列表包含項次(rowCount)、素材名稱(name)、素材描述(description)、最新版次(maxResVerCreated)、最新版本日期(maxResVer)"
    )
    public static class FindAllReq {
        @Schema(description = "request參數1: 輸入之關鍵字")
        private String filter;

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }
    }

    @Schema(description = "素材管理查詢任務Request" +
            "目的: 依據素材名稱、素材描述，進行素材查詢，顯示於素材清單列表 (應含未刪除註記、最新版次、最新版本日期)" +
            "列表包含項次(rowCount)、素材名稱(name)、素材描述(description)、最新版次(maxResVerCreated)、最新版本日期(maxResVer)"
    )
    public static class FindAllResp {

        @Schema(description = "response參數1: 查詢結果清單 包含項次(rowCount)、素材名稱(name)、素材描述(description)、最新版次(maxResVerCreated)、最新版本日期(maxResVer)")
        private Page<InventoryDto> inventoryDto;

        public Page<InventoryDto> getInventoryDto() {
            return inventoryDto;
        }

        public void setInventoryDto(Page<InventoryDto> inventoryDto) {
            this.inventoryDto = inventoryDto;
        }
    }

    public static class FindOneReq {

        @Schema(description = "")
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
    public static class FindOneResp {

        @Schema(description = "")
        private InventoryDto inventoryDto;

        public InventoryDto getInventoryDto() {
            return inventoryDto;
        }

        public void setInventoryDto(InventoryDto inventoryDto) {
            this.inventoryDto = inventoryDto;
        }
    }

    public static class UpdateOneReq extends Inventory {

        private List<String> interfaces;

        public List<String> getInterfaces() {
            return interfaces;
        }

        public void setInterfaces(List<String> interfaces) {
            this.interfaces = interfaces;
        }
    }

    public static class DeleteOneReq extends Inventory {

    }

    public static class PingReq {
        private String manageIp;

        public String getManageIp() {
            return manageIp;
        }

        public void setManageIp(String manageIp) {
            this.manageIp = manageIp;
        }
    }

    public static class PingResp {
        private IcmpPingResponse icmpPingResponse;

        public IcmpPingResponse getIcmpPingResponse() {
            return icmpPingResponse;
        }

        public void setIcmpPingResponse(IcmpPingResponse icmpPingResponse) {
            this.icmpPingResponse = icmpPingResponse;
        }
    }

    public static class SnmpWalkReq {

        private String filter;

        private String manageIp;

        private String id;

        private String snmpProtocol;

        private String snmpPort;

        private String snmpVersion;

        private String snmpCommunity;

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }

        public String getManageIp() {
            return manageIp;
        }

        public void setManageIp(String manageIp) {
            this.manageIp = manageIp;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSnmpProtocol() {
            return snmpProtocol;
        }

        public void setSnmpProtocol(String snmpProtocol) {
            this.snmpProtocol = snmpProtocol;
        }

        public String getSnmpPort() {
            return snmpPort;
        }

        public void setSnmpPort(String snmpPort) {
            this.snmpPort = snmpPort;
        }

        public String getSnmpVersion() {
            return snmpVersion;
        }

        public void setSnmpVersion(String snmpVersion) {
            this.snmpVersion = snmpVersion;
        }

        public String getSnmpCommunity() {
            return snmpCommunity;
        }

        public void setSnmpCommunity(String snmpCommunity) {
            this.snmpCommunity = snmpCommunity;
        }

        @Override
        public String toString() {
            return "SnmpWalkReq{" +
                    "filter='" + filter + '\'' +
                    ", manageIp='" + manageIp + '\'' +
                    ", id='" + id + '\'' +
                    ", snmpProtocol='" + snmpProtocol + '\'' +
                    ", snmpPort='" + snmpPort + '\'' +
                    ", snmpVersion='" + snmpVersion + '\'' +
                    ", snmpCommunity='" + snmpCommunity + '\'' +
                    '}';
        }
    }

    public static class SnmpWalkResp {

        private Page<SnmpWalk> snmpWalk;

        private List<List<Integer>> itemSelected;

        public Page<SnmpWalk> getSnmpWalk() {
            return snmpWalk;
        }

        public void setSnmpWalk(Page<SnmpWalk> snmpWalk) {
            this.snmpWalk = snmpWalk;
        }

        public List<List<Integer>> getItemSelected() {
            return itemSelected;
        }

        public void setItemSelected(List<List<Integer>> itemSelected) {
            this.itemSelected = itemSelected;
        }
    }

    public static class interfaces {
        private String ifIndex;
        private String ifDescr;

        public String getIfIndex() {
            return ifIndex;
        }

        public void setIfIndex(String ifIndex) {
            this.ifIndex = ifIndex;
        }

        @Override
        public String toString() {
            return "interfaces{" +
                    "ifIndex='" + ifIndex + '\'' +
                    '}';
        }
    }
    public static class SnmpWalk {
        private String ifIndex;
        private String ifDescr;
        private String ifType;
        private String ifSpeed;
        private String ifOperStatus;
        private String oid;

        private String ifName;

        private String ifAlias;

        public String getIfIndex() {
            return ifIndex;
        }

        public void setIfIndex(String ifIndex) {
            this.ifIndex = ifIndex;
        }

        public String getIfDescr() {
            return ifDescr;
        }

        public void setIfDescr(String ifDescr) {
            this.ifDescr = ifDescr;
        }

        public String getIfType() {
            return ifType;
        }

        public void setIfType(String ifType) {
            this.ifType = ifType;
        }

        public String getIfSpeed() {
            return ifSpeed;
        }

        public void setIfSpeed(String ifSpeed) {
            this.ifSpeed = ifSpeed;
        }

        public String getIfOperStatus() {
            return ifOperStatus;
        }

        public void setIfOperStatus(String ifOperStatus) {
            this.ifOperStatus = ifOperStatus;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getIfName() {
            return ifName;
        }

        public void setIfName(String ifName) {
            this.ifName = ifName;
        }

        public String getIfAlias() {
            return ifAlias;
        }

        public void setIfAlias(String ifAlias) {
            this.ifAlias = ifAlias;
        }

        @Override
        public String toString() {
            return "SnmpWalk{" +
                    "ifIndex='" + ifIndex + '\'' +
                    ", ifDescr='" + ifDescr + '\'' +
                    ", ifType='" + ifType + '\'' +
                    ", ifSpeed='" + ifSpeed + '\'' +
                    ", ifOperStatus='" + ifOperStatus + '\'' +
                    ", oid='" + oid + '\'' +
                    ", ifName='" + ifName + '\'' +
                    ", ifAlias='" + ifAlias + '\'' +
                    '}';
        }
    }
}
