package com.gminds.employee_service.service.employee;

import com.gminds.employee_service.model.dtos.EmployeeCertificateDTO;
import com.gminds.employee_service.service.utils.EmployeeCollections;

public interface CertificateService extends EmployeeCollections {
    void addCertificateToEmployee(Long id, EmployeeCertificateDTO certificateDTO);
}
