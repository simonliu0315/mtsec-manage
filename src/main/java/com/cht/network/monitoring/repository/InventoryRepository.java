package com.cht.network.monitoring.repository;

import com.cht.network.monitoring.domain.Inventory;
import com.cht.network.monitoring.domain.InventoryPK;
import com.cht.network.monitoring.domain.OperationTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, InventoryPK>, InventoryRepositoryCustom {


    Page<Inventory> findInventoryByDeviceNameIsContainingOrDeviceInterfaceIsContainingOrInterfaceDescriptionIsContaining(String deviceName,
                                                                                           String deviceInterface,
                                                                                           String interfaceDescription,
                                                                                           Pageable pageable);

    @Query("SELECT i FROM Inventory i WHERE (:deviceType IS NULL OR i.deviceType LIKE %:deviceType%) AND (:location IS NULL OR i.location LIKE %:location%)")
    public List<Inventory> getInventoriesByDeviceTypeAndLocation(@Param("deviceType") String deviceType, @Param("location") String Location);

    List<Inventory> getInventoriesByDeviceName(String deviceName);
}
