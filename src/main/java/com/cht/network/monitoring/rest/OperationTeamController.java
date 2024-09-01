package com.cht.network.monitoring.rest;

import com.cht.network.monitoring.domain.OperationTeam;
import com.cht.network.monitoring.dto.OperationTeamDto;
import com.cht.network.monitoring.rest.vm.OperationTeamVM;
import com.cht.network.monitoring.service.OperationTeamService;
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
@RequestMapping("operationTeam")
public class OperationTeamController {

    private static final Logger log = LoggerFactory.getLogger(OperationTeamController.class);

    private final OperationTeamService operationTeamService;

    public OperationTeamController(OperationTeamService operationTeamService) {
        this.operationTeamService = operationTeamService;
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @Operation(summary = "修改資產")
    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<OperationTeamVM.FindOneRes> updateOperationTeam(@Valid @RequestBody OperationTeamVM.UpdateOneReq updateOneReq,
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

    @CrossOrigin(origins = "http://localhost:5173")
    @Operation(summary = "刪除資產")
    @PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<OperationTeamVM.FindOneRes> deleteOneOperationTeam(@Valid @RequestBody OperationTeamVM.DeleteOneReq deleteOneReq,
                                                                          HttpServletResponse response) {

        log.info("deleteOneReq {}", deleteOneReq);
        operationTeamService.delete(deleteOneReq.getId());
        OperationTeamVM.FindOneRes res = new OperationTeamVM.FindOneRes();
        return ResponseEntity.ok().body(res);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @Operation(summary = "取得資產")
    @PostMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<OperationTeamVM.FindAllRes> findAllOperationTeam(@Valid @RequestBody OperationTeamVM.FindAllReq findAllReq,
                                                                           @ParameterObject Pageable page, HttpServletResponse response) {
        log.info("filter {}", findAllReq.getFilter());
        Page<OperationTeamDto> findAllPage = operationTeamService.findAll(findAllReq.getFilter(), page);
        OperationTeamVM.FindAllRes res = new OperationTeamVM.FindAllRes();
        res.setOperationTeamDto(findAllPage);
        return ResponseEntity.ok().body(res);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @Operation(summary = "取得資產")
    @PostMapping(value = "/find/one", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<OperationTeamVM.FindOneRes> findOneOperationTeam(@Valid @RequestBody OperationTeamVM.FindOneReq findOneReq,
                                                                           HttpServletResponse response) {

        log.info("findOneRes {}", findOneReq.getId());
        OperationTeamDto dto = operationTeamService.findOne(findOneReq.getId(), "");
        OperationTeamVM.FindOneRes res = new OperationTeamVM.FindOneRes();
        res.setOperationTeamDto(dto);
        return ResponseEntity.ok().body(res);
    }
}
