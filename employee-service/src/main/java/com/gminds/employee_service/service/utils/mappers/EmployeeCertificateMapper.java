package com.gminds.employee_service.service.utils.mappers;

import com.gminds.employee_service.model.EmployeeCertificate;
import com.gminds.employee_service.model.dtos.EmployeeCertificateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeCertificateMapper {
    EmployeeCertificateMapper INSTANCE = Mappers.getMapper(EmployeeCertificateMapper.class);

    @Mapping(source = "employee.id", target = "employeeId")
    EmployeeCertificateDTO toEmployeeCertificateDTO(EmployeeCertificate employeeCertificate);

    @Mapping(target = "employee", ignore = true)
    EmployeeCertificate toEmployeeCertificate(EmployeeCertificateDTO employeeCertificateDTO);
}
