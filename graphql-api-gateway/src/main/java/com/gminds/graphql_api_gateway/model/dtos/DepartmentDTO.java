package com.gminds.graphql_api_gateway.model.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record DepartmentDTO(
        @NotNull @Min(1) Long id,
        @NotNull @Length(max = 255) String name,
        String description) {
}
