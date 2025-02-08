package com.cht.network.monitoring.web.rest;

import com.cht.network.monitoring.StatusCodes;
import com.cht.network.monitoring.domain.Inventory;
import com.cht.network.monitoring.domain.InventoryInterface;
import com.cht.network.monitoring.dto.InventoryDto;
import com.cht.network.monitoring.dto.SnmpWalkDto;
import com.cht.network.monitoring.service.PingService;
import com.cht.network.monitoring.service.SnmpService;
import com.cht.network.monitoring.service.icmp.IcmpPingResponse;
import com.cht.network.monitoring.util.BandwidthConverter;
import com.cht.network.monitoring.util.PaginationUtil;
import com.cht.network.monitoring.web.rest.vm.InventoryVM;
import com.cht.network.monitoring.service.InventoryService;
import com.cht.network.monitoring.web.util.HeaderUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "InventoryMaintenance", description = "資產維護")
@RestController
@RequestMapping("api/inventoryMaintenance")
public class InventoryMaintenanceResource {

    private static final Logger log = LoggerFactory.getLogger(InventoryMaintenanceResource.class);

    private final InventoryService inventoryService;

    private final PingService pingService;

    private final SnmpService snmpService;

    public InventoryMaintenanceResource(InventoryService inventoryService, PingService pingService, SnmpService snmpService) {
        this.inventoryService = inventoryService;
        this.pingService = pingService;
        this.snmpService = snmpService;
    }

    //@CrossOrigin(origins = "http://localhost:5173")
    @Operation(summary = "取得資產")
    @PostMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<InventoryVM.FindAllResp> findInventoryMaintenanceAllInventory(@Valid @RequestBody InventoryVM.FindAllReq findAllReq,
                                                                                 @ParameterObject Pageable page, HttpServletResponse response) {
        //response.addHeader("Access-Control-Allow-Origin", "http://localhost:5173");

        Page<InventoryDto> findAllPage = inventoryService.findAll("",page);
        InventoryVM.FindAllResp resp = new InventoryVM.FindAllResp();
        resp.setInventoryDto(findAllPage);
        return ResponseEntity.ok().body(resp);
    }

    @Operation(summary = "取得資產")
    @PostMapping(value = "/find/one", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<InventoryVM.FindOneResp> findInventoryMaintenanceOneInventory(@Valid @RequestBody InventoryVM.FindOneReq findOneReq,
                                                                           HttpServletResponse response) {

        log.info("findOneRes {}", findOneReq.getId());
        InventoryDto dto = inventoryService.findOne(findOneReq.getId(), "");
        InventoryVM.FindOneResp res = new InventoryVM.FindOneResp();
        res.setInventoryDto(dto);
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "修改資產")
    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<InventoryVM.FindOneResp> updateInventoryMaintenanceInventory(@Valid @RequestBody InventoryVM.UpdateOneReq updateOneReq,
                                                                           HttpServletResponse response) throws Exception {

        log.info("update {}", updateOneReq);
        log.info("update Interfaces {}", updateOneReq.getInterfaces().size());

        List<InventoryVM.SnmpWalk> list = new ArrayList<>();
        List<SnmpWalkDto> dtos = snmpService.doSNMPBulkWalk(updateOneReq.getManageIp(), updateOneReq.getSnmpPort(), updateOneReq.getSnmpCommunity(), ".1.3.6.1.2.1.2.2.1.1", "bulkwalk");
        for(SnmpWalkDto dto : dtos) {
            InventoryVM.SnmpWalk snmpWalk = new InventoryVM.SnmpWalk();
            snmpWalk.setIfIndex(dto.getVariable());
            snmpWalk.setOid(".1.3.6.1.2.1.2.2.1.1." + dto.getVariable());
            list.add(snmpWalk);
        }

        dtos = snmpService.doSNMPBulkWalk(updateOneReq.getManageIp(), updateOneReq.getSnmpPort(),updateOneReq.getSnmpCommunity(), ".1.3.6.1.2.1.2.2.1.2", "bulkwalk");
        for(int i = 0 ; i < dtos.size() ; i++) {
            list.get(i).setIfDescr(dtos.get(i).getVariable());
        }

        dtos = snmpService.doSNMPBulkWalk(updateOneReq.getManageIp(), updateOneReq.getSnmpPort(),updateOneReq.getSnmpCommunity(), ".1.3.6.1.2.1.2.2.1.3", "bulkwalk");
        for(int i = 0 ; i < dtos.size() ; i++) {
            list.get(i).setIfType(dtos.get(i).getVariable());
        }
        dtos = snmpService.doSNMPBulkWalk(updateOneReq.getManageIp(), updateOneReq.getSnmpPort(),updateOneReq.getSnmpCommunity(), ".1.3.6.1.2.1.2.2.1.5", "bulkwalk");
        for(int i = 0 ; i < dtos.size() ; i++) {
            list.get(i).setIfSpeed(dtos.get(i).getVariable());
        }

        dtos = snmpService.doSNMPBulkWalk(updateOneReq.getManageIp(), updateOneReq.getSnmpPort(),updateOneReq.getSnmpCommunity(), ".1.3.6.1.2.1.2.2.1.8", "bulkwalk");
        for(int i = 0 ; i < dtos.size() ; i++) {
            list.get(i).setIfOperStatus(dtos.get(i).getVariable());
        }

        dtos = snmpService.doSNMPBulkWalk(updateOneReq.getManageIp(), updateOneReq.getSnmpPort(),updateOneReq.getSnmpCommunity(), ".1.3.6.1.2.1.31.1.1.1.1", "bulkwalk");
        for(int i = 0 ; i < dtos.size() ; i++) {
            list.get(i).setIfName(dtos.get(i).getVariable());
        }

        dtos = snmpService.doSNMPBulkWalk(updateOneReq.getManageIp(), updateOneReq.getSnmpPort(),updateOneReq.getSnmpCommunity(), ".1.3.6.1.2.1.31.1.1.1.18", "bulkwalk");
        for(int i = 0 ; i < dtos.size() ; i++) {
            list.get(i).setIfAlias(dtos.get(i).getVariable());
        }

        List<InventoryInterface> inventoryInterfaces = new ArrayList<>();
        for(int i = 0 ; i < updateOneReq.getInterfaces().size() ; i++) {
            if(updateOneReq.getInterfaces().get(i) != null) {
                log.info("inventory interface insert data : {}", list.get(i));
                InventoryInterface inventoryInterface = new InventoryInterface();
                inventoryInterface.setIfIndex(list.get(i).getIfIndex());
                inventoryInterface.setIfDescr(list.get(i).getIfDescr());
                inventoryInterface.setIfName(list.get(i).getIfName());
                inventoryInterface.setIfSpeed(list.get(i).getIfSpeed());
                inventoryInterface.setIfSpeedNm(BandwidthConverter.bandwidth(list.get(i).getIfSpeed()));
                inventoryInterfaces.add(inventoryInterface);
            }
        }


        Inventory inventory = inventoryService.save(updateOneReq.getId(),
                updateOneReq.getDeviceName(),
                updateOneReq.getDeviceInterface(),
                updateOneReq.getInterfaceDescription(),
                updateOneReq.getManageIp(),
                updateOneReq.getDeviceType(),
                updateOneReq.getVendor(),
                updateOneReq.getModel(),
                updateOneReq.getLocation(),
                updateOneReq.getSnmpFrequency(),
                updateOneReq.getPingFrequency(),
                updateOneReq.getGroupId(),
                updateOneReq.getExporterType(),
                updateOneReq.getSnmpProtocol(),
                updateOneReq.getSnmpPort(),
                updateOneReq.getSnmpVersion(),
                updateOneReq.getSnmpCommunity(),
                inventoryInterfaces);
        InventoryVM.FindOneResp resp = new InventoryVM.FindOneResp();
        resp.setInventoryDto(null);
        return ResponseEntity.ok().headers(HeaderUtils.createAlert(StatusCodes.NET_UPDATE_0001_S())).body(resp);
    }

    @Operation(summary = "刪除資產")
    @PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<InventoryVM.FindOneResp> deleteInventoryMaintenanceOneInventory(@Valid @RequestBody InventoryVM.DeleteOneReq deleteOneReq,
                                                                          HttpServletResponse response) {

        log.info("deleteOneReq {}", deleteOneReq);
        inventoryService.delete(deleteOneReq.getId());
        InventoryVM.FindOneResp resp = new InventoryVM.FindOneResp();
        return ResponseEntity.ok().headers(HeaderUtils.createAlert(StatusCodes.NET_DELETE_0001_S())).body(resp);
    }

    @Operation(summary = "Ping設備")
    @PostMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<InventoryVM.PingResp> pingDevice(@Valid @RequestBody InventoryVM.PingReq pingReq,
                                                                                   HttpServletResponse response) {

        log.info("pingDevice {}", pingReq.getManageIp());
        IcmpPingResponse icmpPingResponse = pingService.Ping(pingReq.getManageIp());
        InventoryVM.PingResp resp = new InventoryVM.PingResp();
        resp.setIcmpPingResponse(icmpPingResponse);
        return ResponseEntity.ok().body(resp);
    }

    @Operation(summary = "取得設備介面清單")
    @PostMapping(value = "/inventoryInterface", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<InventoryVM.SnmpWalkResp> inventoryInterface(@Valid @RequestBody InventoryVM.SnmpWalkReq snmpWalkReq,
                                                                                     @ParameterObject Pageable page,
                                                                         HttpServletResponse response) throws Exception {

        log.info("inventoryInterface {}", snmpWalkReq.getManageIp());//bulkwalk
        log.info("inventoryInterface req {}", snmpWalkReq);
        log.info("page {}", page);
        //         1.3.6.1.2.1.2.2.1
        //ifIndex .1.3.6.1.2.1.2.2.1.1
        //ifDescr .1.3.6.1.2.1.2.2.1.2
        //ifType  .1.3.6.1.2.1.2.2.1.3
        //ifSpeed .1.3.6.1.2.1.2.2.1.5
        //ifOperStatus .1.3.6.1.2.1.2.2.1.8
        //List<SnmpWalkDto> dtos =  snmpService.snmpTableUtils("127.0.0.1", ".1.3.6.1.2.1.2.2.1");
        InventoryVM.SnmpWalkResp resp = new InventoryVM.SnmpWalkResp();
        InventoryVM.SnmpWalk snmpWalk = new InventoryVM.SnmpWalk();
        List<InventoryVM.SnmpWalk> list = new ArrayList<>();
        List<SnmpWalkDto> dtos = snmpService.doSNMPBulkWalk(snmpWalkReq.getManageIp(), snmpWalkReq.getSnmpPort(), snmpWalkReq.getSnmpCommunity(), ".1.3.6.1.2.1.2.2.1.1", "bulkwalk");
        for(SnmpWalkDto dto : dtos) {
            snmpWalk = new InventoryVM.SnmpWalk();
            snmpWalk.setIfIndex(dto.getVariable());
            snmpWalk.setOid(".1.3.6.1.2.1.2.2.1.1." + dto.getVariable());
            list.add(snmpWalk);
        }

        dtos = snmpService.doSNMPBulkWalk(snmpWalkReq.getManageIp(), snmpWalkReq.getSnmpPort(), snmpWalkReq.getSnmpCommunity(), ".1.3.6.1.2.1.2.2.1.2", "bulkwalk");
        for(int i = 0 ; i < dtos.size() ; i++) {
            list.get(i).setIfDescr(dtos.get(i).getVariable());
        }

        dtos = snmpService.doSNMPBulkWalk(snmpWalkReq.getManageIp(), snmpWalkReq.getSnmpPort(), snmpWalkReq.getSnmpCommunity(), ".1.3.6.1.2.1.2.2.1.3", "bulkwalk");
        for(int i = 0 ; i < dtos.size() ; i++) {
            list.get(i).setIfType(dtos.get(i).getVariable());
        }
        dtos = snmpService.doSNMPBulkWalk(snmpWalkReq.getManageIp(), snmpWalkReq.getSnmpPort(), snmpWalkReq.getSnmpCommunity(), ".1.3.6.1.2.1.2.2.1.5", "bulkwalk");
        for(int i = 0 ; i < dtos.size() ; i++) {
            list.get(i).setIfSpeed(dtos.get(i).getVariable());
        }

        dtos = snmpService.doSNMPBulkWalk(snmpWalkReq.getManageIp(), snmpWalkReq.getSnmpPort(), snmpWalkReq.getSnmpCommunity(), ".1.3.6.1.2.1.2.2.1.8", "bulkwalk");
        for(int i = 0 ; i < dtos.size() ; i++) {
            list.get(i).setIfOperStatus(dtos.get(i).getVariable());
        }

        dtos = snmpService.doSNMPBulkWalk(snmpWalkReq.getManageIp(), snmpWalkReq.getSnmpPort(), snmpWalkReq.getSnmpCommunity(), ".1.3.6.1.2.1.31.1.1.1.1", "bulkwalk");
        for(int i = 0 ; i < dtos.size() ; i++) {
            list.get(i).setIfName(dtos.get(i).getVariable());
        }

        dtos = snmpService.doSNMPBulkWalk(snmpWalkReq.getManageIp(), snmpWalkReq.getSnmpPort(), snmpWalkReq.getSnmpCommunity(), ".1.3.6.1.2.1.31.1.1.1.18", "bulkwalk");
        for(int i = 0 ; i < dtos.size() ; i++) {
            list.get(i).setIfAlias(dtos.get(i).getVariable());
        }
        Page<InventoryVM.SnmpWalk> snmpWalks = PaginationUtil.generatePageFromList(list, page);
        resp.setSnmpWalk(snmpWalks);
        resp.setItemSelected(convertTo2DArray(list, page.getPageSize(), inventoryService.findIfIndexById(snmpWalkReq.getId())));


        //snmpService.snmpGet("127.0.0.1", "1.3.6.1.2.1.2.2.1.7");
        //IcmpPingResponse icmpPingResponse = pingService.Ping(pingReq.getManageIp());

        //resp.setIcmpPingResponse(icmpPingResponse);

        if (list.isEmpty()) {
            return ResponseEntity.ok().headers(HeaderUtils.createAlert(StatusCodes.APP_APP001W_0002_I())).body(resp);
        }

        return ResponseEntity.ok().body(resp);
    }


    public static List<List<Integer>> convertTo2DArray(List<InventoryVM.SnmpWalk> snmpWalks, int pageSize, List<String> inventoryInterfaces) {
        log.info("------>{}", inventoryInterfaces.size());
        for(String s : inventoryInterfaces){
            log.info("inventoryInterface {}", s);
        }
        List<List<Integer>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        int pageNumber = 0;
        int indexInPage = 0;

        log.info("pageSize {}", pageSize);
        for(int i = 0; i <= snmpWalks.size()  / pageSize; i++) {
            if (pageNumber <= snmpWalks.size() / pageSize) {
                result.add(new ArrayList<>());
                pageNumber++;
                indexInPage = 0;
            }
            for(int j = 0; j < pageSize; j++) {
                if( (i * pageSize) + j < snmpWalks.size()) {
                    log.info("pageNumber = {}, i = {} ==> j= {}, {} ==> {}" , pageNumber, i, j,
                            snmpWalks.get(i).getIfIndex(),
                            inventoryInterfaces.contains(snmpWalks.get((i * pageSize) + j).getIfIndex()));
                    if (inventoryInterfaces.contains(snmpWalks.get((i * pageSize) + j).getIfIndex())) {
                        result.get(i+1).add(j);
                    } else {
                        result.get(i+1).add(null);
                    }
                }

            }

            indexInPage++;
        }

        return result;
    }

}
