package com.cht.network.monitoring.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.util.Date;
import java.util.Objects;

public class EventStatisticsPK {

    @Id
    @Column(name = "check_time", nullable = false)
    private Date checkTime;

    @Id
    @Column(name = "type", nullable = false, length = 100)
    private String type;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventStatisticsPK that = (EventStatisticsPK) o;
        return Objects.equals(checkTime, that.checkTime) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checkTime, type);
    }
}
