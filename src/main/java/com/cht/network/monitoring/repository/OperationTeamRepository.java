package com.cht.network.monitoring.repository;

import com.cht.network.monitoring.domain.OperationTeam;
import com.cht.network.monitoring.domain.OperationTeamPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationTeamRepository extends JpaRepository<OperationTeam, OperationTeamPK> {

    Page<OperationTeam> findOperationTeamsByNameIsContainingOrCompanyIsContainingOrJobTitleIsContainingOrMobileIsContainingOrTelephoneIsContainingOrFaxIsContainingOrEmailIsContainingOrderByUpdatedAtDesc(
            String name, String company, String jobTitle, String mobile, String telephone, String fax, String email, Pageable pageable);
}
