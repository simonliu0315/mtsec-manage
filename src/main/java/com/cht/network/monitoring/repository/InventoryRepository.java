package com.cht.network.monitoring.repository;

import com.cht.network.monitoring.domain.Inventory;
import com.cht.network.monitoring.domain.InventoryPK;
import com.cht.network.monitoring.domain.OperationTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, InventoryPK>, InventoryRepositoryCustom {


    Page<Inventory> findInventoryByDeviceNameIsContainingOrDeviceInterfaceIsContainingOrInterfaceDescriptionIsContaining(String deviceName,
                                                                                           String deviceInterface,
                                                                                           String interfaceDescription,
                                                                                           Pageable pageable);
}
