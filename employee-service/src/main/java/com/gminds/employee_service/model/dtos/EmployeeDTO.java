package com.gminds.employee_service.model.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Set;

public record EmployeeDTO(Long id,
                          @NotNull @Length(max = 255) String name,
                          @NotNull @Length(max = 255) String surname,
                          @NotNull JobDTO job,
                          @NotNull DepartmentDTO department,
                          @NotNull @NotEmpty List<EmployeeAgreementDTO> agreements,
                          Set<EmployeeCertificateDTO> certificates,
                          @NotNull @NotEmpty List<EmploymentHistoryDTO> employmentHistory) {
}
