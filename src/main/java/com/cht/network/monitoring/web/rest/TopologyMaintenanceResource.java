package com.cht.network.monitoring.web.rest;


import com.cht.network.monitoring.StatusCodes;
import com.cht.network.monitoring.domain.Inventory;
import com.cht.network.monitoring.domain.InventoryInterface;
import com.cht.network.monitoring.domain.OperationTeam;
import com.cht.network.monitoring.domain.Topology;
import com.cht.network.monitoring.dto.OperationTeamDto;
import com.cht.network.monitoring.dto.TopologyDto;
import com.cht.network.monitoring.service.InventoryService;
import com.cht.network.monitoring.service.TopologyService;
import com.cht.network.monitoring.web.rest.vm.OperationTeamVM;
import com.cht.network.monitoring.web.rest.vm.TopologyVM;
import com.cht.network.monitoring.web.util.HeaderUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Tag(name = "TopologyMaintenance", description = "網路拓樸圖資料維護")
@RestController
@PreAuthorize("hasAnyAuthority('AUTH_TopologyMaintenance')")
@RequestMapping("api/topologyMaintenance")
public class TopologyMaintenanceResource {

    private static final Logger log = LoggerFactory.getLogger(TopologyMaintenanceResource.class);

    @Value("${cht.network.upload.path}")
    private String uploadPath;

    private final TopologyService topologyService;

    private final InventoryService inventoryService;

    public TopologyMaintenanceResource(TopologyService topologyService, InventoryService inventoryService) {
        this.topologyService = topologyService;
        this.inventoryService = inventoryService;
    }

    @Operation(summary = "取得所有的網路拓樸圖資料")
    @PostMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<TopologyVM.FindAllRes> findTopologyMaintenanceAllTopology(@Valid @RequestBody TopologyVM.FindAllReq findAllReq,
                                                                                                            @ParameterObject Pageable page, HttpServletResponse response) {
        log.info("filter: {}, page: {}", findAllReq.getFilter(), page);
        Page<TopologyDto> findAllPage = topologyService.findAll(findAllReq.getFilter(), page);
        TopologyVM.FindAllRes resp = new TopologyVM.FindAllRes();
        resp.setTopologyDto(findAllPage);
        return ResponseEntity.ok().body(resp);
    }

    @Operation(summary = "修改網路拓樸圖")
    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<TopologyVM.FindOneRes> updateTopologyMaintenanceTopology(
            @Valid @RequestBody TopologyVM.UpdateOneReq updateOneReq, HttpServletResponse response) {

        log.info("update {}", updateOneReq);
        Topology topology = topologyService.save(
                updateOneReq.getName(),updateOneReq.getConfigs(),
                updateOneReq.getNodes(),updateOneReq.getEdges(),
                updateOneReq.getLayouts()
               );
        TopologyVM.FindOneRes resp = new TopologyVM.FindOneRes();
        resp.setTopologyDto(null);
        return ResponseEntity.ok().headers(HeaderUtils.createAlert(StatusCodes.NET_INSERT_0001_S())).body(resp);
    }

    @Operation(summary = "取得單一網路拓樸圖")
    @PostMapping(value = "/find/one", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<TopologyVM.FindOneRes> findTopologyMaintenanceOneTopology(
            @Valid @RequestBody TopologyVM.FindOneReq findOneReq,
            HttpServletResponse response) {
        log.info("findOneRes {}", findOneReq.getId());
        TopologyDto dto = topologyService.findOne(findOneReq.getId(), "");
        TopologyVM.FindOneRes resp = new TopologyVM.FindOneRes();
        resp.setTopologyDto(dto);
        return ResponseEntity.ok().body(resp);
    }

    @Operation(summary = "取得設備清單")
    @PostMapping(value = "/find/inventory/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<TopologyVM.InventoryResp> findTopologyMaintenanceInventoryList(
            @Valid @RequestBody TopologyVM.InventoryReq inventoryReq,
            HttpServletResponse response) {
        log.info("findTopologyMaintenanceInventoryList {}", inventoryReq.getId());
        List<Inventory> inventories = inventoryService.getInventories(inventoryReq.getDeviceType(), inventoryReq.getLocation());
        TopologyVM.InventoryResp resp = new TopologyVM.InventoryResp();
        resp.setInventories(inventories);
        return ResponseEntity.ok().body(resp);
    }

    @Operation(summary = "取得設備的介面清單")
    @PostMapping(value = "/find/inventory/interface/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<TopologyVM.InventoryResp> findTopologyMaintenanceInventoryInterfaceList(
            @Valid @RequestBody TopologyVM.InventoryReq inventoryReq,
            HttpServletResponse response) {
        log.info("findTopologyMaintenanceInventoryInterfaceList {}", inventoryReq);
        List<InventoryInterface> interfaces = inventoryService.findInventoryInterfaceByDeviceName(inventoryReq.getDeviceName());
        log.info("interfaces {}", interfaces.size());
        TopologyVM.InventoryResp resp = new TopologyVM.InventoryResp();
        resp.setInterfaces(interfaces);
        return ResponseEntity.ok().body(resp);
    }

    @Operation(summary = "使用設備名稱取得單一設備")
    @PostMapping(value = "/find/inventory/one", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<TopologyVM.InventoryResp> findTopologyMaintenanceInventoryOne(
            @Valid @RequestBody TopologyVM.InventoryReq inventoryReq,
            HttpServletResponse response) {
        log.info("findTopologyMaintenanceInventoryOne {}", inventoryReq.getDeviceName());
        List<Inventory> inventories = inventoryService.findInventoryeByDeviceName(inventoryReq.getDeviceName());
        TopologyVM.InventoryResp resp = new TopologyVM.InventoryResp();
        resp.setInventories(inventories);
        return ResponseEntity.ok().body(resp);
    }

    @Operation(summary = "底圖的上傳")
    @PostMapping(value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveTopologyMaintenanceUploadFile(@RequestParam("file") MultipartFile file,
                                                                    @RequestParam("width") Integer width,
                                                                    @RequestParam("height") Integer height,
                                                                    @RequestParam("id") String id) throws IOException {
        log.info("**********saveTopologyMaintenanceUploadFile***********");
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("請選擇檔案");
        }
        TopologyDto dto = topologyService.findOne(id, "");
        if (dto == null) {
            log.error("查無 Topology資料");
            return ResponseEntity.ok().headers(HeaderUtils.createAlert(StatusCodes.NET_SELECT_0001_E())).body("");
        }
        log.info("width: {}, height: {}", width, height);
        topologyService.saveImage(dto.getId(), file.getOriginalFilename(), width.toString(), height.toString());
        String originalFilename = file.getOriginalFilename();
        file.transferTo(new File(uploadPath + "/" + originalFilename));

        return ResponseEntity.ok().headers(HeaderUtils.createAlert(StatusCodes.NET_UPDATE_0001_S())).body("");
    }

    @GetMapping(value = "/images/{imageName:.+}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) throws IOException {
        log.info("getImage {}", imageName);
        File file = new File(uploadPath + "/" + imageName);
        if (!file.exists()) {
            log.info("檔案不存在，路徑 {}", uploadPath + "/" + imageName);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(Base64.getEncoder().encode(IOUtils.toByteArray(new FileInputStream(file))));
    }
}
