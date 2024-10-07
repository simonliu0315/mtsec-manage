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
                '}';
    }
}
