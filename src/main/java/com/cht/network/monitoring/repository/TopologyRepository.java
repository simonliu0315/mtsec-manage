package com.cht.network.monitoring.repository;

import com.cht.network.monitoring.domain.Topology;
import com.cht.network.monitoring.domain.TopologyPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopologyRepository extends JpaRepository<Topology, TopologyPK> {

    public Topology findByName(String name);
}
