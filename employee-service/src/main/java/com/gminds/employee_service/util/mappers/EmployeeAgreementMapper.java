package com.gminds.employee_service.util.mappers;

import com.gminds.employee_service.model.EmployeeAgreement;
import com.gminds.employee_service.model.dtos.EmployeeAgreementDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeAgreementMapper {
    EmployeeAgreementMapper INSTANCE = Mappers.getMapper(EmployeeAgreementMapper.class);

    @Mapping(source = "employee.id", target = "employeeId")
    EmployeeAgreementDTO toEmployeeAgreementDTO(EmployeeAgreement employeeAgreement);

    @Mapping(source = "employeeId", target = "employee.id")
    EmployeeAgreement toEmployeeAgreement(EmployeeAgreementDTO employeeAgreementDTO);
}
