package com.cht.network.monitoring.domain;

import jakarta.persistence.*;

import java.sql.Timestamp;


@Entity
@Table(name = "Inventory_interface")
@IdClass(InventoryInterfacePK.class)
public class InventoryInterface {

    @Id
    @Column(name = "inventory_id", nullable = false, length = 100)
    private String inventoryId;

    @Id
    @Column(name = "if_index", nullable = false, length = 255)
    private String ifIndex;

    @Column(name = "if_descr")
    private String ifDescr;

    @Column(name = "if_name")
    private String ifName;

    @Column(name = "if_speed")
    private String ifSpeed;

    @Column(name = "if_speed_Nm")
    private String ifSpeedNm;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

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

    public String getIfName() {
        return ifName;
    }

    public void setIfName(String ifName) {
        this.ifName = ifName;
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

    public String getIfSpeed() {
        return ifSpeed;
    }

    public void setIfSpeed(String ifSpeed) {
        this.ifSpeed = ifSpeed;
    }

    public String getIfSpeedNm() {
        return ifSpeedNm;
    }

    public void setIfSpeedNm(String ifSpeedNm) {
        this.ifSpeedNm = ifSpeedNm;
    }

    @Override
    public String toString() {
        return "InventoryInterface{" +
                "inventoryId='" + inventoryId + '\'' +
                ", ifIndex='" + ifIndex + '\'' +
                ", ifDescr='" + ifDescr + '\'' +
                ", ifName='" + ifName + '\'' +
                ", ifSpeed='" + ifSpeed + '\'' +
                ", ifSpeedNm='" + ifSpeedNm + '\'' +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", updatedAt=" + updatedAt +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}
