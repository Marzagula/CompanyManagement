package com.gminds.employee_service.util.mappers;

import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.dtos.EmployeeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(target = "id", source = "employee.id")
    EmployeeDTO toEmployeeDTO(Employee employee);

    @Mapping(target = "id", ignore = true)
    Employee toEmployee(EmployeeDTO employeeDTO);
}