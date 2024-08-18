package com.gminds.employee_service.service.employee;

import com.gminds.employee_service.exceptions.ResourceNotFoundException;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.dtos.EmployeeDTO;
import com.gminds.employee_service.repository.EmployeeRepository;
import com.gminds.employee_service.service.agreement.AgreementManagementService;
import com.gminds.employee_service.service.employee.validator.EmployeeValidator;
import com.gminds.employee_service.service.utils.mappers.EmployeeMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepository employeeRepository;
    private final EmployeeValidator employeeValidator;

    private final AgreementManagementService agreementManagementService;

    public EmployeeService(EmployeeValidator employeeValidator,
                           EmployeeRepository employeeRepository,
                           AgreementManagementService agreementManagementService) {
        this.employeeRepository = employeeRepository;
        this.agreementManagementService = agreementManagementService;
        this.employeeValidator = employeeValidator;
    }

    public Page<EmployeeDTO> findAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(EmployeeMapper.INSTANCE::toEmployeeDTO);
    }

    @Transactional
    public void addEmployee(EmployeeDTO employeeDTO) {
        Employee newEmployee = EmployeeMapper.INSTANCE.toEmployee(employeeDTO);

        newEmployee.getCertificates().forEach(cert -> cert.setEmployee(newEmployee));
        newEmployee.getAgreements().forEach(agr -> {
            agr.setEmployee(newEmployee);
            agr.setStatus(agreementManagementService.determineAgreementStatus(agr));
        });
        newEmployee.getEmploymentHistory().forEach(hist -> hist.setEmployee(newEmployee));
        employeeValidator.validate(newEmployee);

        employeeRepository.saveAndFlush(newEmployee);
    }


    public Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Can't find employee with id: {}", id);
                    return new ResourceNotFoundException("Can't find employee with id: " + id);
                });
    }

    @Transactional
    public EmployeeDTO updatePersonalInformation(EmployeeDTO employeeDTO) {
        Employee employee = findEmployeeById(employeeDTO.id());
        employee.setName(employeeDTO.name());
        employee.setSurname(employeeDTO.surname());
        return EmployeeMapper.INSTANCE.toEmployeeDTO(employee);
    }

}
