package com.cht.network.monitoring.web.rest;

import com.cht.network.monitoring.StatusCodes;
import com.cht.network.monitoring.domain.EventStatistics;
import com.cht.network.monitoring.dto.DomesticCircuitDto;
import com.cht.network.monitoring.dto.EventLogCnt;
import com.cht.network.monitoring.statuscode.StatusCode;
import com.cht.network.monitoring.web.rest.vm.DomesticCircuitVM;
import com.cht.network.monitoring.web.util.HeaderUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import com.cht.network.monitoring.service.DomesticCircuitService;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

@Tag(name = "DomesticCircuit", description = "國內骨幹電路")
@RestController
@RequestMapping("api/domesticCircuit")
public class DomesticCircuitResource {

    private static final Logger log = LoggerFactory.getLogger(DomesticCircuitResource.class);

    private final DomesticCircuitService domesticCircuitService;

    public DomesticCircuitResource(DomesticCircuitService domesticCircuitService) {
        this.domesticCircuitService = domesticCircuitService;
    }

    //@CrossOrigin(origins = "http://localhost:5173", exposedHeaders = {"Authorization","x-network-traceid","x-network-alert","x-network-params","x-network-dismiss-alert","content-disposition"})
    @Operation(summary = "國內骨幹電路-取得所有設備")
    @PostMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<DomesticCircuitVM.FindAllRes> findDomesticCircuitAllRes(@Valid @RequestBody DomesticCircuitVM.FindAllReq findAllReq,
                                                                                 @ParameterObject Pageable page, HttpServletResponse response) {

        log.info("findAllRes {}  {}", findAllReq.getFilter(),page);

        Page<DomesticCircuitDto> findAllPage = domesticCircuitService.findAll(findAllReq.getFilter(), page);
        DomesticCircuitVM.FindAllRes res = new DomesticCircuitVM.FindAllRes();
        res.setDomesticCircuitDto(findAllPage);
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "國內骨幹電路-取得事件總計")
    @PostMapping(value = "/find/eventCnt", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<DomesticCircuitVM.FindEventRes> findDomesticCircuitEventCnt(@Valid @RequestBody DomesticCircuitVM.FindEventReq findEventReq,
                                                                                 HttpServletResponse response) {

        log.info("findEventReq {}  ", findEventReq.getFilter());
        List<EventLogCnt> eventLogCnts = domesticCircuitService.getEventLogCnt(findEventReq.getFilter());
        DomesticCircuitVM.FindEventRes res = new DomesticCircuitVM.FindEventRes();
        for(EventLogCnt cnt: eventLogCnts) {
            switch(cnt.getLevel()) {
                case "Critical": res.setCriticalCnt(cnt.getCnt());
                    break;
                case "Minor": res.setMinorCnt(cnt.getCnt());
                    break;
                case "Normal": res.setNormalCnt(cnt.getCnt());
                    break;
                default: res.setNormalCnt(cnt.getCnt());
                    break;
            }
        }
        return ResponseEntity.ok().body(res);
    }

   @Operation(summary = "國內骨幹電路-取得事件歷史資料")
    @PostMapping(value = "/find/eventCntHistory", produces = MediaType.APPLICATION_JSON_VALUE)
   public @ResponseBody ResponseEntity<DomesticCircuitVM.FindEventHistoryRes> findDomesticCircuitEventCntHistory(@Valid @RequestBody DomesticCircuitVM.FindEventHistoryReq findEventHistoryReq,
                                                                                     HttpServletResponse response) {

        log.info("findEventCntHistory {} , interval {}", findEventHistoryReq.getFilter(), findEventHistoryReq.getTimeInterval());
        List<EventStatistics> eventStatistics = domesticCircuitService.getEventLogHistoryCnt(
                findEventHistoryReq.getFilter(), findEventHistoryReq.getTimeInterval(), 0, findEventHistoryReq.getTimeInterval()/5);
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        DomesticCircuitVM.FindEventHistoryRes res = new DomesticCircuitVM.FindEventHistoryRes();
        eventStatistics.sort(Comparator.comparing(EventStatistics::getCheckTime, Comparator.naturalOrder()));


        //.sorted(Comparator.naturalOrder())
        res.setCheckTime(eventStatistics.stream().map(eventStat -> sdf.format(eventStat.getCheckTime())).toArray(String[]::new));
        res.setCriticalCnt(eventStatistics.stream().mapToInt(EventStatistics::getCriticalCount).toArray());
        res.setMinorCnt(eventStatistics.stream().mapToInt(EventStatistics::getMinorCount).unordered().toArray());
        res.setNormalCnt(eventStatistics.stream().mapToInt(EventStatistics::getNormalCount).unordered().toArray());

        //return ResponseEntity.ok().headers(HeaderUtils.createAlert("APP-APP001I-0001-S")).body(res);
       return ResponseEntity.ok().headers(HeaderUtils.createAlert(StatusCodes.NET_SELECT_0001_S())).body(res);
    }
}
