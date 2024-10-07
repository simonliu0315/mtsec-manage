package com.cht.network.monitoring.web.rest;


import com.cht.network.monitoring.domain.EventLog;
import com.cht.network.monitoring.dto.EventLogCnt;
import com.cht.network.monitoring.dto.EventLogDto;
import com.cht.network.monitoring.security.SecurityUtils;
import com.cht.network.monitoring.service.DomesticCircuitService;
import com.cht.network.monitoring.service.DomesticDashboardService;
import com.cht.network.monitoring.web.rest.vm.DomesticCircuitVM;
import com.cht.network.monitoring.web.rest.vm.DomesticDashboardVM;
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


@Tag(name = "DomesticDashboard", description = "國網儀錶板")
@RestController
@RequestMapping("api/domesticDashboard")
public class DomesticDashboardResource {

    private static final Logger log = LoggerFactory.getLogger(DomesticDashboardResource.class);

    private final DomesticDashboardService domesticDashboardService;

    public DomesticDashboardResource(DomesticDashboardService domesticDashboardService) {
        this.domesticDashboardService = domesticDashboardService;
    }

    @Operation(summary = "取得截至今日尚未結案事件單")
    @PostMapping(value = "/find/innerEventLogNotClosed", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<DomesticDashboardVM.FindEventNotClosedRes> findDomesticDashboardInnerEventLogNotClosed(@Valid @RequestBody DomesticCircuitVM.FindEventReq findEventReq,
                                                                                     @ParameterObject Pageable page,
                                                                                     HttpServletResponse response) {

        log.info("findEventReq {}  ", findEventReq.getFilter());
        log.info("get User {}  ", SecurityUtils.getCurrentUser());
        Page<EventLog> eventLog = domesticDashboardService.getInnerEventLogsNotClosed(page);
        int criticalCnt = domesticDashboardService.getInnerEventLogsCount("Critical");
        int minorCnt = domesticDashboardService.getInnerEventLogsCount("Minor");
        int normalCnt = domesticDashboardService.getInnerEventLogsCount("Normal");
        DomesticDashboardVM.FindEventNotClosedRes res = new DomesticDashboardVM.FindEventNotClosedRes();
        res.setEventLog(eventLog);
        res.setCriticalCnt(criticalCnt);
        res.setMinorCnt(minorCnt);
        res.setNormalCnt(normalCnt);
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "取得截至外部尚未結案事件單")
    @PostMapping(value = "/find/outterEventLogNotClosed", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<DomesticDashboardVM.FindEventNotClosedRes> findDomesticDashboardOutterEventLogNotClosed(@Valid @RequestBody DomesticCircuitVM.FindEventReq findEventReq,
                                                                                                         @ParameterObject Pageable page,
                                                                                                         HttpServletResponse response) {

        log.info("findEventReq {}  ", findEventReq.getFilter());
        Page<EventLog> eventLog = domesticDashboardService.getOutterEventLogsNotClosed(page);
        int criticalCnt = domesticDashboardService.getOutterEventLogsCount("Critical");
        int minorCnt = domesticDashboardService.getOutterEventLogsCount("Minor");
        int normalCnt = domesticDashboardService.getOutterEventLogsCount("Normal");
        DomesticDashboardVM.FindEventNotClosedRes res = new DomesticDashboardVM.FindEventNotClosedRes();
        res.setEventLog(eventLog);
        res.setCriticalCnt(criticalCnt);
        res.setMinorCnt(minorCnt);
        res.setNormalCnt(normalCnt);
        return ResponseEntity.ok().body(res);
    }
}
