package com.gminds.employee_service.model.dtos;

import java.time.LocalDate;

public record EmploymentHistoryDTO(Long id,
                                   String companyName,
                                   String jobName,
                                   LocalDate fromDate,
                                   LocalDate toDate) {
}
