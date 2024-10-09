package com.cht.network.monitoring.service;

import com.cht.network.monitoring.domain.OperationTeam;
import com.cht.network.monitoring.domain.Topology;
import com.cht.network.monitoring.domain.TopologyPK;
import com.cht.network.monitoring.dto.OperationTeamDto;
import com.cht.network.monitoring.dto.TopologyDto;
import com.cht.network.monitoring.repository.OperationTeamRepository;
import com.cht.network.monitoring.repository.TopologyRepository;
import com.cht.network.monitoring.security.SecurityUtils;
import com.cht.network.monitoring.security.UserInfo;
import com.cht.network.monitoring.web.rest.vm.UserInfoVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TopologyService {

    private static final Logger log = LoggerFactory.getLogger(TopologyService.class);

    private final TopologyRepository topologyRepository;

    public TopologyService(TopologyRepository topologyRepository) {
        this.topologyRepository = topologyRepository;
    }

    public Page<TopologyDto> findAll(String filter, Pageable pageable) {
        Pageable firstPageWithTwoElements = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<Topology> page = topologyRepository.findAll(firstPageWithTwoElements);

        List<TopologyDto> dtos = new ArrayList<>();
        for(Topology topology : page) {
            TopologyDto dto = new TopologyDto();
            dto.setId(topology.getId());
            dto.setName(topology.getName());
            dto.setCreatedBy(topology.getCreatedBy());
            dto.setCreatedAt(topology.getCreatedAt());
            dto.setUpdatedAt(topology.getUpdatedAt());
            dtos.add(dto);
        }
        return new PageImpl<TopologyDto>(dtos, pageable, page.getTotalElements());
    }

    public TopologyDto findOne(String id, String filter) {
        TopologyPK topologyPK = new TopologyPK();
        topologyPK.setId(id);
        Topology topology = topologyRepository.findById(topologyPK).get();
        TopologyDto dto = new TopologyDto();
        dto.setId(id);
        dto.setName(topology.getName());
        dto.setConfigs(topology.getConfigs());
        dto.setNodes(topology.getNodes());
        dto.setEdges(topology.getEdges());
        dto.setLayouts(topology.getLayouts());
        dto.setCreatedAt(topology.getCreatedAt());
        dto.setUpdatedAt(topology.getUpdatedAt());
        return dto;
    }
    public Topology save(String name, String configs, String nodes, String edges, String layouts) {
        Topology topology = topologyRepository.findByName(name);
        if (topology == null) {
            topology = new Topology();
            topology.setId(UUID.randomUUID().toString());
        }
        log.info("configs {}, nodes {}, edges {}", configs.length(), nodes.length(), edges.length());
        topology.setName(name);
        topology.setConfigs(configs);
        topology.setNodes(nodes);
        topology.setEdges(edges);
        topology.setLayouts(layouts);
        topology.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        topology.setCreatedBy("TEST");
        topology.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        topology.setUpdatedBy("TEST");
        return topologyRepository.saveAndFlush(topology);
    }
}
