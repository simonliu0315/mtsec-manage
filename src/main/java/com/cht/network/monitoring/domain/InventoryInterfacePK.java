package com.cht.network.monitoring.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class InventoryInterfacePK implements Serializable {

        @Id
        @Column(name = "inventory_id", nullable = false, length = 100)
        private String inventoryId;

        @Id
        @Column(name = "if_index", nullable = false, length = 255)
        private String ifIndex;

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

        @Override
        public boolean equals(Object o) {
                if (o == null || getClass() != o.getClass()) return false;
                InventoryInterfacePK that = (InventoryInterfacePK) o;
                return Objects.equals(inventoryId, that.inventoryId) && Objects.equals(ifIndex, that.ifIndex);
        }

        @Override
        public int hashCode() {
                return Objects.hash(inventoryId, ifIndex);
        }
}
