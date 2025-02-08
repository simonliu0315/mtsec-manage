package com.cht.network.monitoring.repository;

import com.cht.network.monitoring.domain.RoleFunctional;
import com.cht.network.monitoring.domain.RoleFunctionalPK;
import com.cht.network.monitoring.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleFunctionalRepository extends JpaRepository<RoleFunctional, RoleFunctionalPK> {

    List<RoleFunctional> findRoleFunctionalByRole(String role);
}
