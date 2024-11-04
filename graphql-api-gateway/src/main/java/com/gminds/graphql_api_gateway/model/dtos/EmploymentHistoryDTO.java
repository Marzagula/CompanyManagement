package com.gminds.graphql_api_gateway.model.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record EmploymentHistoryDTO(Long id,
                                   @NotNull @Length(max = 255) String companyName,
                                   @NotNull @Length(max = 255) String jobName,
                                   @NotNull LocalDate fromDate,
                                   @NotNull LocalDate toDate,
                                   @NotNull @Min(1) Long employeeId) {
}
