package com.cht.network.monitoring.repository;

import com.cht.network.monitoring.domain.Inventory;
import com.cht.network.monitoring.domain.InventoryPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, InventoryPK> {

}
