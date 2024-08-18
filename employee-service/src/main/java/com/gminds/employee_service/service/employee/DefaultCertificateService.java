package com.gminds.employee_service.service.employee;

import com.gminds.employee_service.exceptions.DataValidationException;
import com.gminds.employee_service.exceptions.ResourceNotFoundException;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.EmployeeCertificate;
import com.gminds.employee_service.model.dtos.EmployeeCertificateDTO;
import com.gminds.employee_service.repository.EmployeeRepository;
import com.gminds.employee_service.service.utils.mappers.EmployeeCertificateMapper;
import com.gminds.employee_service.service.utils.validator.DateValidator;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.util.Collection;
import java.util.function.Consumer;

@Service
public class DefaultCertificateService implements CertificateService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultCertificateService.class);
    private final EmployeeRepository repository;

    public DefaultCertificateService(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public void addCertificateToEmployee(Long id, EmployeeCertificateDTO certificateDTO) {
        addValidatedCertificate(getEmployeeById(id), certificateDTO);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Can't find employee with id: {}", id);
                    return new ResourceNotFoundException("Can't find employee with id: " + id);
                });
    }

    protected void addValidatedCertificate(Employee employee, EmployeeCertificateDTO certificateDTO) {
        try {
            DateValidator.validateIfEarlierIsBeforeLater(certificateDTO.issueDate(), certificateDTO.expiryDate());
        } catch (DateTimeException e) {
            throw new DataValidationException("Invalid certificate date range: " + certificateDTO.issueDate() + " - " + certificateDTO.expiryDate(), e);
        }
        EmployeeCertificate newCertificate = EmployeeCertificateMapper.INSTANCE.toEmployeeCertificate(certificateDTO);
        addEntityToEmployeeCollection(employee, newCertificate, newCertificate::setEmployee, employee.getCertificates());
        EmployeeCertificateMapper.INSTANCE.toEmployeeCertificateDTO(newCertificate);
    }

    @Override
    public <E> void addEntityToEmployeeCollection(Employee employee, E entity, Consumer<Employee> employeeSetter, Collection<E> collection) {
        CertificateService.super.addEntityToEmployeeCollection(employee, entity, employeeSetter, collection);
    }
}
