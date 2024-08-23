package com.cht.network.monitoring.rest;

import com.cht.network.monitoring.dto.DomesticCircuitDto;
import com.cht.network.monitoring.rest.vm.DomesticCircuitVM;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import com.cht.network.monitoring.service.DomesticCircuitService;

@RestController
@RequestMapping("domesticCircuit")
public class DomesticCircuitController {

    private static final Logger log = LoggerFactory.getLogger(DomesticCircuitController.class);

    private final DomesticCircuitService domesticCircuitService;

    public DomesticCircuitController(DomesticCircuitService domesticCircuitService) {
        this.domesticCircuitService = domesticCircuitService;
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @Operation(summary = "取得素材清單")
    @PostMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<DomesticCircuitVM.FindAllRes> findAllRes(@Valid @RequestBody DomesticCircuitVM.FindAllReq findAllReq,
                                                                                 @ParameterObject Pageable page, HttpServletResponse response) {
        //response.addHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        log.info("AAAAAAAAAAAAAAAAAAAAAA");
        Page<DomesticCircuitDto> findAllPage = domesticCircuitService.findAll("",page);
        DomesticCircuitVM.FindAllRes res = new DomesticCircuitVM.FindAllRes();
        res.setDomesticCircuitDto(findAllPage);
        return ResponseEntity.ok().body(res);
    }
}
