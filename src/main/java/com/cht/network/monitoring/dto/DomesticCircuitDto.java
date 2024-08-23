package com.cht.network.monitoring.dto;

import java.util.Date;

public class DomesticCircuitDto {

    private String deviceId;
    private String deviceName;
    private String deviceInterface;
    private String interfaceDescription;
    private Date checkTime;
    private long inputUsage;
    private long outputUsage;
    private String remarks;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public long getInputUsage() {
        return inputUsage;
    }

    public void setInputUsage(long inputUsage) {
        this.inputUsage = inputUsage;
    }

    public long getOutputUsage() {
        return outputUsage;
    }

    public void setOutputUsage(long outputUsage) {
        this.outputUsage = outputUsage;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
