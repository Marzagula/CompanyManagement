package com.gminds.employee_service.service;

import com.gminds.employee_service.exceptions.ResourceNotFoundException;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.EmployeeCertificate;
import com.gminds.employee_service.model.dtos.EmployeeCertificateDTO;
import com.gminds.employee_service.repository.EmployeeRepository;
import com.gminds.employee_service.service.validation.DateValidatorImpl;
import com.gminds.employee_service.util.mappers.EmployeeCertificateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CertificateService extends AbstractTransactionalService<EmployeeCertificateDTO> {

    private static final Logger logger = LoggerFactory.getLogger(CertificateService.class);
    private final EmployeeRepository repository;
    private final DateValidatorImpl datesValidator;

    public CertificateService(EmployeeRepository repository,
                              DateValidatorImpl datesValidator) {
        this.repository = repository;
        this.datesValidator = datesValidator;
    }

    public void addCertificateToEmployee(Long id, EmployeeCertificateDTO certificateDTO) {
        datesValidator.validateIfEarlierIsBeforeLater(certificateDTO.issueDate(), certificateDTO.expiryDate());
        executeChanges(id, certificateDTO);
    }

    @Override
    protected Employee findEmployeeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Can't find employee with id: {}", id);
                    return new ResourceNotFoundException("Can't find employee with id: " + id);
                });
    }

    @Override
    protected EmployeeCertificateDTO processTransaction(Employee employee, EmployeeCertificateDTO certificateDTO) {
        EmployeeCertificate newCertificate = EmployeeCertificateMapper.INSTANCE.toEmployeeCertificate(certificateDTO);
        addEntityToEmployeeCollection(employee, newCertificate, newCertificate::setEmployee, employee.getCertificates());
        return EmployeeCertificateMapper.INSTANCE.toEmployeeCertificateDTO(newCertificate);
    }
}
