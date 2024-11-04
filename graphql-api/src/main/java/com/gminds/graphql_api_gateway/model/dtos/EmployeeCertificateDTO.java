package com.gminds.graphql_api_gateway.model.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeeCertificateDTO(Long id,
                                     @NotNull @Length(max = 255) String certificateName,
                                     @NotNull LocalDate issueDate,
                                     LocalDate expiryDate,
                                     @Length(max = 255) String issuedBy,
                                     @NotNull @Min(1) Long employeeId,
                                     String createdBy,
                                     LocalDateTime createdDate,
                                     String lastModifiedBy,
                                     LocalDateTime lastModifiedDate) {
}
