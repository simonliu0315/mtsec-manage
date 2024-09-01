package com.cht.network.monitoring.domain;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "event_statistics")
@IdClass(EventStatisticsPK.class)
public class EventStatistics {

    @Id
    @Column(name = "check_time", nullable = false)
    private Date checkTime;

    @Id
    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @Column(name = "critical_count")
    private int criticalCount;

    @Column(name = "minor_count")
    private int minorCount;

    @Column(name = "normal_count")
    private int normalCount;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCriticalCount() {
        return criticalCount;
    }

    public void setCriticalCount(int criticalCount) {
        this.criticalCount = criticalCount;
    }

    public int getMinorCount() {
        return minorCount;
    }

    public void setMinorCount(int minorCount) {
        this.minorCount = minorCount;
    }

    public int getNormalCount() {
        return normalCount;
    }

    public void setNormalCount(int normalCount) {
        this.normalCount = normalCount;
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
}
