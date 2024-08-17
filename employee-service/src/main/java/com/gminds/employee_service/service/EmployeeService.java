package com.gminds.employee_service.service;

import com.gminds.employee_service.exceptions.ResourceNotFoundException;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.dtos.EmployeeDTO;
import com.gminds.employee_service.repository.EmployeeRepository;
import com.gminds.employee_service.service.utils.mappers.EmployeeMapper;
import com.gminds.employee_service.service.validation.DateValidatorImpl;
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
    private final DateValidatorImpl datesValidator;

    public EmployeeService(EmployeeRepository employeeRepository, DateValidatorImpl datesValidator) {
        this.employeeRepository = employeeRepository;
        this.datesValidator = datesValidator;
    }

    public Page<EmployeeDTO> findAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(EmployeeMapper.INSTANCE::toEmployeeDTO);
    }

    public void addEmployee(EmployeeDTO employeeDTO) {
        Employee newEmployee = EmployeeMapper.INSTANCE.toEmployee(employeeDTO);
        validateEmployeeCollectionsDates(newEmployee);
        this.employeeRepository.save(newEmployee);
    }


    private Employee findEmployeeById(Long id) {
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

    /**
     * Check that employment history, contracts, and certifications of employee have logically correct dates.
     *
     * @param employee is employee whose collections are validated.
     */
    private void validateEmployeeCollectionsDates(Employee employee) {
        employee.getEmploymentHistory().forEach(eH -> datesValidator.validateIfEarlierIsBeforeLater(eH.getFromDate(), eH.getToDate()));
        employee.getAgreements().forEach(agr -> datesValidator.validateIfEarlierIsBeforeLater(agr.getFromDate(), agr.getToDate()));
        employee.getCertificates().forEach(cert -> datesValidator.validateIfEarlierIsBeforeLater(cert.getIssueDate(), cert.getExpiryDate()));
    }
}
