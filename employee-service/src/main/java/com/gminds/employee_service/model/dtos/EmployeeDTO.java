package com.gminds.employee_service.model.dtos;

import java.util.List;
import java.util.Set;

public record EmployeeDTO(Long id,
                          String name,
                          String surname,
                          JobDTO job,
                          DepartmentDTO department,
                          List<EmployeeAgreementDTO> agreements,
                          Set<EmployeeCertificateDTO> certificates,
                          List<EmploymentHistoryDTO> employmentHistory) {
}
