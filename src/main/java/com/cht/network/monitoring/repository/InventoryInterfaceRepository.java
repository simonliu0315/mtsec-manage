package com.cht.network.monitoring.repository;

import com.cht.network.monitoring.domain.Inventory;
import com.cht.network.monitoring.domain.InventoryInterface;
import com.cht.network.monitoring.domain.InventoryInterfacePK;
import com.cht.network.monitoring.domain.InventoryPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryInterfaceRepository extends JpaRepository<InventoryInterface, InventoryInterfacePK>, InventoryInterfaceRepositoryCustom {

    public int deleteByInventoryId(String inventoryId);

    @Query(value = "SELECT ifIndex from InventoryInterface where inventoryId = :inventoryId")
    public List<String> findIfIndexByInventoryId(String inventoryId);

    @Query(value = "SELECT i from InventoryInterface i where inventoryId = :inventoryId")
    public List<InventoryInterface> findByInventoryId(String inventoryId);
}
