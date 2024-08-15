package com.gminds.employee_service.util.mappers;

import com.gminds.employee_service.model.Department;
import com.gminds.employee_service.model.dtos.DepartmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DepartmentMapper {
    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    DepartmentDTO toDepartmentDTO(Department department);

    @Mapping(target = "id", ignore = true)
    Department toDepartment(DepartmentDTO jobDTO);
}
