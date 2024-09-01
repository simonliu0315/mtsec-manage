package com.cht.network.monitoring.rest;

import com.cht.network.monitoring.domain.EventStatistics;
import com.cht.network.monitoring.dto.DomesticCircuitDto;
import com.cht.network.monitoring.dto.EventLogCnt;
import com.cht.network.monitoring.rest.vm.DomesticCircuitVM;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
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

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

        log.info("findAllRes {}  {}", findAllReq.getFilter(),page);
        Page<DomesticCircuitDto> findAllPage = domesticCircuitService.findAll(findAllReq.getFilter(), page);
        DomesticCircuitVM.FindAllRes res = new DomesticCircuitVM.FindAllRes();
        res.setDomesticCircuitDto(findAllPage);
        return ResponseEntity.ok().body(res);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @Operation(summary = "取得素材清單")
    @PostMapping(value = "/find/eventCnt", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<DomesticCircuitVM.FindEventRes> findEventCnt(@Valid @RequestBody DomesticCircuitVM.FindEventReq findEventReq,
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

    @CrossOrigin(origins = "http://localhost:5173")
    @Operation(summary = "取得素材清單")
    @PostMapping(value = "/find/eventCntHistory", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<DomesticCircuitVM.FindEventHistoryRes> findEventCntHistory(@Valid @RequestBody DomesticCircuitVM.FindEventHistoryReq findEventHistoryReq,
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

        return ResponseEntity.ok().body(res);
    }
}
