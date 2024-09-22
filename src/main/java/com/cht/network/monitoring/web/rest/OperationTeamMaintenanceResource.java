package com.cht.network.monitoring.web.rest;

import com.cht.network.monitoring.domain.OperationTeam;
import com.cht.network.monitoring.dto.OperationTeamDto;
import com.cht.network.monitoring.web.rest.vm.OperationTeamVM;
import com.cht.network.monitoring.service.OperationTeamService;
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

@Tag(name = "OperationTeamMaintenance", description = "維運團隊資料維護")
@RestController
@RequestMapping("api/operationTeamMaintenance")
public class OperationTeamMaintenanceResource {

    private static final Logger log = LoggerFactory.getLogger(OperationTeamMaintenanceResource.class);

    private final OperationTeamService operationTeamService;

    public OperationTeamMaintenanceResource(OperationTeamService operationTeamService) {
        this.operationTeamService = operationTeamService;
    }

    @Operation(summary = "修改資產")
    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<OperationTeamVM.FindOneRes> updateOperationTeamMaintenanceOperationTeam(@Valid @RequestBody OperationTeamVM.UpdateOneReq updateOneReq,
                                                                       HttpServletResponse response) {

        log.info("update {}", updateOneReq);
        OperationTeam operationTeam = operationTeamService.save(updateOneReq.getId(),
                updateOneReq.getName(),
                updateOneReq.getCompany(),
                updateOneReq.getJobTitle(),
                updateOneReq.getMobile(),
                updateOneReq.getTelephone(),
                updateOneReq.getFax(),
                updateOneReq.getEmail(),
                updateOneReq.getRemark());
        OperationTeamVM.FindOneRes res = new OperationTeamVM.FindOneRes();
        res.setOperationTeamDto(null);
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "刪除資產")
    @PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<OperationTeamVM.FindOneRes> deleteOperationTeamMaintenanceOneOperationTeam(@Valid @RequestBody OperationTeamVM.DeleteOneReq deleteOneReq,
                                                                          HttpServletResponse response) {

        log.info("deleteOneReq {}", deleteOneReq);
        operationTeamService.delete(deleteOneReq.getId());
        OperationTeamVM.FindOneRes res = new OperationTeamVM.FindOneRes();
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "取得所有的資產")
    @PostMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<OperationTeamVM.FindAllRes> findOperationTeamMaintenanceAllOperationTeam(@Valid @RequestBody OperationTeamVM.FindAllReq findAllReq,
                                                                           @ParameterObject Pageable page, HttpServletResponse response) {
        log.info("filter: {}, page: {}", findAllReq.getFilter(), page);
        Page<OperationTeamDto> findAllPage = operationTeamService.findAll(findAllReq.getFilter(), page);
        OperationTeamVM.FindAllRes res = new OperationTeamVM.FindAllRes();
        res.setOperationTeamDto(findAllPage);
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "取得單一資產")
    @PostMapping(value = "/find/one", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<OperationTeamVM.FindOneRes> findOperationTeamMaintenanceOneOperationTeam(@Valid @RequestBody OperationTeamVM.FindOneReq findOneReq,
                                                                           HttpServletResponse response) {

        log.info("findOneRes {}", findOneReq.getId());
        OperationTeamDto dto = operationTeamService.findOne(findOneReq.getId(), "");
        OperationTeamVM.FindOneRes res = new OperationTeamVM.FindOneRes();
        res.setOperationTeamDto(dto);
        return ResponseEntity.ok().body(res);
    }
}
