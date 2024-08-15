package com.gminds.employee_service.model.dtos;

import java.time.LocalDate;

public record EmployeeCertificateDTO(Long id,
                                     String certificateName,
                                     LocalDate issueDate,
                                     LocalDate expiryDate,
                                     String issuedBy) {
}
