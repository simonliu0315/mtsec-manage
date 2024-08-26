package com.cht.network.monitoring.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

public class InventoryPK implements Serializable {

        @Id
        @Column(name = "id", nullable = false, length = 100)
        private String id;

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                InventoryPK that = (InventoryPK) o;
                return id == that.id;
        }

        @Override
        public int hashCode() {
                return Objects.hashCode(id);
        }
}
