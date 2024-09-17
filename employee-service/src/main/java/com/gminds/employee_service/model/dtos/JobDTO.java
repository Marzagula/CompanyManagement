package com.gminds.employee_service.model.dtos;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record JobDTO(
        @NotNull @Min(1) Long id,
        @NotNull @Length(max = 255) String title,
        String description,
        @NotNull @Min(1) Long departmentId) {
}
