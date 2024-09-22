package com.cht.network.monitoring.domain;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "event_log")
@IdClass(EventLogPK.class)
public class EventLog {

    @Id
    @Column(name = "id", nullable = false, length = 100)
    private String id;

    @Column(name = "level")
    private String level;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "access_type")
    private String accessType;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "device_unit")
    private String deviceUnit;

    @Column(name = "description")
    private String description;

    @Column(name = "occurred_at")
    private Date occurredAt;

    @Column(name = "handled_by")
    private String handledBy;

    @Column(name = "handled_at")
    private String handledAt;

    @Column(name = "result")
    private String result;

    @Column(name = "closed_at")
    private Date closedAt;

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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceUnit() {
        return deviceUnit;
    }

    public void setDeviceUnit(String deviceUnit) {
        this.deviceUnit = deviceUnit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(Date occurredAt) {
        this.occurredAt = occurredAt;
    }

    public String getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(String handledBy) {
        this.handledBy = handledBy;
    }

    public String getHandledAt() {
        return handledAt;
    }

    public void setHandledAt(String handledAt) {
        this.handledAt = handledAt;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
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
        return "EventLog{" +
                "id='" + id + '\'' +
                ", level='" + level + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", accessType='" + accessType + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", deviceUnit='" + deviceUnit + '\'' +
                ", description='" + description + '\'' +
                ", occurredAt=" + occurredAt +
                ", handledBy='" + handledBy + '\'' +
                ", handledAt='" + handledAt + '\'' +
                ", result='" + result + '\'' +
                ", closedAt=" + closedAt +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", updatedAt=" + updatedAt +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}
