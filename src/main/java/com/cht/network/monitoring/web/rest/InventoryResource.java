package com.cht.network.monitoring.web.rest;

import com.cht.network.monitoring.domain.Inventory;
import com.cht.network.monitoring.dto.InventoryDto;
import com.cht.network.monitoring.web.rest.vm.InventoryVM;
import com.cht.network.monitoring.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("inventory")
public class InventoryResource {

    private static final Logger log = LoggerFactory.getLogger(InventoryResource.class);

    private final InventoryService inventoryService;

    public InventoryResource(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    //@CrossOrigin(origins = "http://localhost:5173")
    @Operation(summary = "取得資產")
    @PostMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<InventoryVM.FindAllRes> findAllInventory(@Valid @RequestBody InventoryVM.FindAllReq findAllReq,
                                                                                 @ParameterObject Pageable page, HttpServletResponse response) {
        //response.addHeader("Access-Control-Allow-Origin", "http://localhost:5173");

        Page<InventoryDto> findAllPage = inventoryService.findAll("",page);
        InventoryVM.FindAllRes res = new InventoryVM.FindAllRes();
        res.setInventoryDto(findAllPage);
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "取得資產")
    @PostMapping(value = "/find/one", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<InventoryVM.FindOneRes> findOneInventory(@Valid @RequestBody InventoryVM.FindOneReq findOneReq,
                                                                           HttpServletResponse response) {

        log.info("findOneRes {}", findOneReq.getId());
        InventoryDto dto = inventoryService.findOne(findOneReq.getId(), "");
        InventoryVM.FindOneRes res = new InventoryVM.FindOneRes();
        res.setInventoryDto(dto);
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "修改資產")
    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<InventoryVM.FindOneRes> updateInventory(@Valid @RequestBody InventoryVM.UpdateOneReq updateOneReq,
                                                                           HttpServletResponse response) {

        log.info("update {}", updateOneReq);
        Inventory inventory = inventoryService.save(updateOneReq.getId(),
                updateOneReq.getDeviceName(),
                updateOneReq.getDeviceInterface(),
                updateOneReq.getInterfaceDescription());
        InventoryVM.FindOneRes res = new InventoryVM.FindOneRes();
        res.setInventoryDto(null);
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "刪除資產")
    @PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<InventoryVM.FindOneRes> deleteOneInventory(@Valid @RequestBody InventoryVM.DeleteOneReq deleteOneReq,
                                                                          HttpServletResponse response) {

        log.info("deleteOneReq {}", deleteOneReq);
        inventoryService.delete(deleteOneReq.getId());
        InventoryVM.FindOneRes res = new InventoryVM.FindOneRes();
        return ResponseEntity.ok().body(res);
    }
}
