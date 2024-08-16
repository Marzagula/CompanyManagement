package com.gminds.employee_service.model.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record EmployeeCertificateDTO(Long id,
                                     @NotNull @Length(max = 255) String certificateName,
                                     @NotNull LocalDate issueDate,
                                     LocalDate expiryDate,
                                     @Length(max = 255) String issuedBy,
                                     @NotNull @Min(1) Long employeeId) {
}
