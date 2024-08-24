package com.cht.network.monitoring.domain;

import jakarta.persistence.*;


@Entity
@Table(name = "inventory")
@IdClass(InventoryPK.class)
public class Inventory {

    @Id
    @Column(name = "id", nullable = false, length = 20)
    private int id;

    @Column(name = "device_name")
    private String deviceName;
    @Column(name = "device_interface")
    private String deviceInterface;
    @Column(name = "interface_description")
    private String interfaceDescription;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", deviceName='" + deviceName + '\'' +
                ", deviceInterface='" + deviceInterface + '\'' +
                ", interfaceDescription='" + interfaceDescription + '\'' +
                '}';
    }
}
