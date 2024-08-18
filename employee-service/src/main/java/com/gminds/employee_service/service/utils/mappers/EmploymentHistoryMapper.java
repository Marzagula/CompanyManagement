package com.gminds.employee_service.service.utils.mappers;

import com.gminds.employee_service.model.EmploymentHistory;
import com.gminds.employee_service.model.dtos.EmploymentHistoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmploymentHistoryMapper {
    EmploymentHistoryMapper INSTANCE = Mappers.getMapper(EmploymentHistoryMapper.class);

    @Mapping(source = "employee.id", target = "employeeId")
    EmploymentHistoryDTO toEmploymentHistoryDTO(EmploymentHistory employmentHistory);

    @Mapping(source = "employeeId", target = "employee.id")
    EmploymentHistory toEmploymentHistory(EmploymentHistoryDTO employmentHistoryDTO);
}
