package com.cht.network.monitoring.rest;

import com.cht.network.monitoring.dto.DomesticCircuitDto;
import com.cht.network.monitoring.dto.InventoryDto;
import com.cht.network.monitoring.rest.vm.DomesticCircuitVM;
import com.cht.network.monitoring.rest.vm.InventoryVM;
import com.cht.network.monitoring.service.DomesticCircuitService;
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
public class InventoryController {

    private static final Logger log = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @Operation(summary = "取得資產")
    @PostMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<InventoryVM.FindAllRes> findAllRes(@Valid @RequestBody InventoryVM.FindAllReq findAllReq,
                                                                                 @ParameterObject Pageable page, HttpServletResponse response) {
        //response.addHeader("Access-Control-Allow-Origin", "http://localhost:5173");

        Page<InventoryDto> findAllPage = inventoryService.findAll("",page);
        InventoryVM.FindAllRes res = new InventoryVM.FindAllRes();
        res.setInventoryDto(findAllPage);
        return ResponseEntity.ok().body(res);
    }
}
