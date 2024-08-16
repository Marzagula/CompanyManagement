package com.gminds.employee_service.util.mappers;

import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.dtos.EmployeeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {JobMapper.class, EmployeeAgreementMapper.class, EmployeeCertificateMapper.class, EmploymentHistoryMapper.class, DepartmentMapper.class})
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(source = "job.department.id", target = "job.departmentId")
    EmployeeDTO toEmployeeDTO(Employee employee);

    @Mapping(source = "job.departmentId", target = "job.department.id")
    Employee toEmployee(EmployeeDTO employeeDTO);
}