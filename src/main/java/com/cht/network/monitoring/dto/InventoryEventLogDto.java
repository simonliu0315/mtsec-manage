package com.cht.network.monitoring.dto;

import com.cht.network.monitoring.domain.EventLog;
import com.cht.network.monitoring.domain.Inventory;

public class InventoryEventLogDto extends Inventory {

    private String level;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
