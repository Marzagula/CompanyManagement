package com.gminds.employee_service.service.employee;

import com.gminds.employee_service.exceptions.ResourceNotFoundException;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.EmployeeCertificate;
import com.gminds.employee_service.model.dtos.EmployeeCertificateDTO;
import com.gminds.employee_service.repository.EmployeeRepository;
import com.gminds.employee_service.service.employee.strategy.CertificateCollectionStrategy;
import com.gminds.employee_service.service.utils.mappers.EmployeeCertificateMapper;
import com.gminds.employee_service.service.utils.validator.DateValidator;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;

@Service
public class DefaultCertificateService extends CertificateCollectionStrategy implements CertificateService {

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
            logger.error("Invalid certificate date range: {} - {}", certificateDTO.issueDate(), certificateDTO.expiryDate());
            throw new DateTimeException("Invalid certificate date range: " + certificateDTO.issueDate() + " - " + certificateDTO.expiryDate(), e);
        }
        EmployeeCertificate newCertificate = EmployeeCertificateMapper.INSTANCE.toEmployeeCertificate(certificateDTO);
        addToCollection(employee, newCertificate);
    }


}
