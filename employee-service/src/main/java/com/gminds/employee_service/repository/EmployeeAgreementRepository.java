package com.gminds.employee_service.repository;

import com.gminds.employee_service.model.EmployeeAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeAgreementRepository extends JpaRepository<EmployeeAgreement,Long> {
}
