package com.cht.network.monitoring.service;

import com.cht.network.monitoring.dto.DomesticCircuitDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DomesticCircuitService {

    private static final Logger log = LoggerFactory.getLogger(DomesticCircuitService.class);


    public Page<DomesticCircuitDto> findAll(String filter, Pageable pageable) {

        log.info("findAll");
        DomesticCircuitDto dto = new DomesticCircuitDto();
        List<DomesticCircuitDto> list = new ArrayList<>();
        dto.setDeviceName("TWAREN-TP-ASR9006-01");
        dto.setDeviceInterface("Te0/0/0/5.3");
        dto.setCheckTime(new Date());
        dto.setInterfaceDescription("INT#20_TWAREN-TP-ASR9006-01 to CHI-4801l 10GE (2671UD80004)");
        list.add(dto);
        dto = new DomesticCircuitDto();
        dto.setDeviceName("TWAREN-TP-ASR9006-01");
        dto.setDeviceInterface("Te0/0/0/5.3");
        dto.setCheckTime(new Date());
        dto.setInterfaceDescription("INT#20_TWAREN-TP-ASR9006-01 to CHI-4801l 10GE (2671UD80004)");
        list.add(dto);
        return new PageImpl<DomesticCircuitDto>(list, pageable, list.size());
    }
}
