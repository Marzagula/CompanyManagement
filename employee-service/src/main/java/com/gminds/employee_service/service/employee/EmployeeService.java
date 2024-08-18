package com.gminds.employee_service.service.employee;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.exceptions.ResourceNotFoundException;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.dtos.EmployeeDTO;
import com.gminds.employee_service.repository.EmployeeRepository;
import com.gminds.employee_service.service.agreement.validator.AgreementValidatorService;
import com.gminds.employee_service.service.utils.mappers.EmployeeMapper;
import com.gminds.employee_service.service.utils.validator.DateValidator;
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

    private final AgreementValidatorService agreementValidatorService;

    public EmployeeService(EmployeeRepository employeeRepository,
                           AgreementValidatorService agreementValidatorService) {
        this.employeeRepository = employeeRepository;

        this.agreementValidatorService = agreementValidatorService;
    }

    public Page<EmployeeDTO> findAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(EmployeeMapper.INSTANCE::toEmployeeDTO);
    }

    @Transactional
    public void addEmployee(EmployeeDTO employeeDTO) {
        Employee newEmployee = EmployeeMapper.INSTANCE.toEmployee(employeeDTO);

        newEmployee.getCertificates().forEach(cert -> cert.setEmployee(newEmployee));
        newEmployee.getAgreements().forEach(agr -> agr.setEmployee(newEmployee));
        newEmployee.getEmploymentHistory().forEach(hist -> hist.setEmployee(newEmployee));
        validateEmployeeCollectionsDates(newEmployee);

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

    /**
     * Check that employment history, contracts, and certifications of employee have logically correct dates.
     *
     * @param employee is employee whose collections are validated.
     */
    /*TODO przeniesc validacje do osobnej klasy zajmujacej sie tylko tym**/
    private void validateEmployeeCollectionsDates(Employee employee){
        employee.getEmploymentHistory().forEach(eH -> DateValidator.validateIfEarlierIsBeforeLater(eH.getFromDate(), eH.getToDate()));
        employee.getAgreements().forEach(agr -> {
            try {
                agreementValidatorService.validateAgreement(agr);
            } catch (EmployeeAgreementException e) {
                throw new RuntimeException(e);
            }
        });
        employee.getCertificates().forEach(cert -> DateValidator.validateIfEarlierIsBeforeLater(cert.getIssueDate(), cert.getExpiryDate()));
    }
}
