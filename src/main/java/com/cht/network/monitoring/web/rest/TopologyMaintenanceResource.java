package com.cht.network.monitoring.web.rest;


import com.cht.network.monitoring.StatusCodes;
import com.cht.network.monitoring.domain.OperationTeam;
import com.cht.network.monitoring.domain.Topology;
import com.cht.network.monitoring.dto.OperationTeamDto;
import com.cht.network.monitoring.dto.TopologyDto;
import com.cht.network.monitoring.service.TopologyService;
import com.cht.network.monitoring.web.rest.vm.OperationTeamVM;
import com.cht.network.monitoring.web.rest.vm.TopologyVM;
import com.cht.network.monitoring.web.util.HeaderUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "TopologyMaintenance", description = "網路拓樸圖資料維護")
@RestController
@RequestMapping("api/topologyMaintenance")
public class TopologyMaintenanceResource {

    private static final Logger log = LoggerFactory.getLogger(TopologyMaintenanceResource.class);

    private final TopologyService topologyService;

    public TopologyMaintenanceResource(TopologyService topologyService) {
        this.topologyService = topologyService;
    }

    @Operation(summary = "取得所有的網路拓樸圖資料")
    @PostMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<TopologyVM.FindAllRes> findTopologyMaintenanceAllTopology(@Valid @RequestBody TopologyVM.FindAllReq findAllReq,
                                                                                                            @ParameterObject Pageable page, HttpServletResponse response) {
        log.info("filter: {}, page: {}", findAllReq.getFilter(), page);
        Page<TopologyDto> findAllPage = topologyService.findAll(findAllReq.getFilter(), page);
        TopologyVM.FindAllRes res = new TopologyVM.FindAllRes();
        res.setTopologyDto(findAllPage);
        return ResponseEntity.ok().body(res);
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
        TopologyVM.FindOneRes res = new TopologyVM.FindOneRes();
        res.setTopologyDto(null);
        return ResponseEntity.ok().headers(HeaderUtils.createAlert(StatusCodes.NET_INSERT_0001_S())).body(res);
    }

    @Operation(summary = "取得單一網路拓樸圖")
    @PostMapping(value = "/find/one", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<TopologyVM.FindOneRes> findTopologyMaintenanceOneTopology(
            @Valid @RequestBody TopologyVM.FindOneReq findOneReq,
            HttpServletResponse response) {
        log.info("findOneRes {}", findOneReq.getId());
        TopologyDto dto = topologyService.findOne(findOneReq.getId(), "");
        TopologyVM.FindOneRes res = new TopologyVM.FindOneRes();
        res.setTopologyDto(dto);
        return ResponseEntity.ok().body(res);
    }
}
