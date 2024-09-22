package com.gminds.employee_service.service.utils.mappers;

import com.gminds.employee_service.model.EmployeeAgreementClause;

import com.gminds.employee_service.model.dtos.EmployeeAgreementClauseDTO;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

public interface EmployeeAgreementClauseMapper {
    EmployeeAgreementMapper INSTANCE = Mappers.getMapper(EmployeeAgreementMapper.class);

    @Mapping(source = "employeeAgreement.id", target = "employeeAgreementId")
    EmployeeAgreementClauseDTO toEmployeeAgreementDTO(EmployeeAgreementClause employeeAgreementClause);

    @Mapping(source = "employeeAgreementId", target = "employeeAgreement.id")
    EmployeeAgreementClause toEmployeeAgreement(EmployeeAgreementClauseDTO employeeAgreementClauseDTO);
}
