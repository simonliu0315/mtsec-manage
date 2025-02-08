package com.cht.network.monitoring.domain;

import jakarta.persistence.*;

import java.sql.Timestamp;


@Entity
@Table(name = "inventory")
@IdClass(InventoryPK.class)
public class Inventory {

    @Id
    @Column(name = "id", nullable = false, length = 100)
    private String id;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "device_interface")
    private String deviceInterface;

    @Column(name = "interface_description")
    private String interfaceDescription;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "manage_ip")
    private String manageIp;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "vendor")
    private String vendor;

    @Column(name = "model")
    private String model;

    @Column(name = "location")
    private String location;

    @Column(name = "snmp_frequency")
    private String snmpFrequency;

    @Column(name = "ping_frequency")
    private String pingFrequency;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "exporter_type")
    private String exporterType;

    @Column(name = "snmp_port")
    private String snmpPort;

    @Column(name = "snmp_protocol")
    private String snmpProtocol;

    @Column(name = "snmp_version")
    private String snmpVersion;

    @Column(name = "snmp_community")
    private String snmpCommunity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceInterface() {
        return deviceInterface;
    }

    public void setDeviceInterface(String deviceInterface) {
        this.deviceInterface = deviceInterface;
    }

    public String getInterfaceDescription() {
        return interfaceDescription;
    }

    public void setInterfaceDescription(String interfaceDescription) {
        this.interfaceDescription = interfaceDescription;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getManageIp() {
        return manageIp;
    }

    public void setManageIp(String manageIp) {
        this.manageIp = manageIp;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSnmpFrequency() {
        return snmpFrequency;
    }

    public void setSnmpFrequency(String snmpFrequency) {
        this.snmpFrequency = snmpFrequency;
    }

    public String getPingFrequency() {
        return pingFrequency;
    }

    public void setPingFrequency(String pingFrequency) {
        this.pingFrequency = pingFrequency;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getExporterType() {
        return exporterType;
    }

    public void setExporterType(String exporterType) {
        this.exporterType = exporterType;
    }

    public String getSnmpPort() {
        return snmpPort;
    }

    public void setSnmpPort(String snmpPort) {
        this.snmpPort = snmpPort;
    }

    public String getSnmpProtocol() {
        return snmpProtocol;
    }

    public void setSnmpProtocol(String snmpProtocol) {
        this.snmpProtocol = snmpProtocol;
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
        return "Inventory{" +
                "id='" + id + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceInterface='" + deviceInterface + '\'' +
                ", interfaceDescription='" + interfaceDescription + '\'' +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", updatedAt=" + updatedAt +
                ", updatedBy='" + updatedBy + '\'' +
                ", manageIp='" + manageIp + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", vendor='" + vendor + '\'' +
                ", model='" + model + '\'' +
                ", location='" + location + '\'' +
                ", snmpFrequency='" + snmpFrequency + '\'' +
                ", pingFrequency='" + pingFrequency + '\'' +
                ", groupId='" + groupId + '\'' +
                ", exporterType='" + exporterType + '\'' +
                ", snmpPort='" + snmpPort + '\'' +
                ", snmpProtocol='" + snmpProtocol + '\'' +
                ", snmpVersion='" + snmpVersion + '\'' +
                ", snmpCommunity='" + snmpCommunity + '\'' +
                '}';
    }
}
