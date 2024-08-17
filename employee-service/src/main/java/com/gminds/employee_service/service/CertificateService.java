package com.gminds.employee_service.service;

import com.gminds.employee_service.model.dtos.EmployeeCertificateDTO;

public interface CertificateService extends EmployeeCollections {
    void addCertificateToEmployee(Long id, EmployeeCertificateDTO certificateDTO);
}
