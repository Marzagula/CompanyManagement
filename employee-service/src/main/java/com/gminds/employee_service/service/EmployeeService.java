package com.gminds.employee_service.service;

import com.gminds.employee_service.exceptions.EmployeeException;
import com.gminds.employee_service.exceptions.JobException;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.Job;
import com.gminds.employee_service.model.dtos.EmployeeDTO;
import com.gminds.employee_service.repository.EmployeeRepository;
import com.gminds.employee_service.repository.JobRepository;
import com.gminds.employee_service.util.mappers.EmployeeMapper;
import com.gminds.employee_service.util.mappers.EmployeeMapperImpl;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;

    public EmployeeService(EmployeeRepository employeeRepository, JobRepository jobRepository) {
        this.employeeRepository = employeeRepository;
        this.jobRepository = jobRepository;
    }

    public Page<EmployeeDTO> findAllEmployees(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepository.findAll(pageable).map(EmployeeMapper.INSTANCE::toEmployeeDTO);
    }

    public void addEmployee(Employee employee) {

        employee.getAgreements().forEach(agreement -> agreement.setEmployee(employee));
        employee.getCertificates().forEach(employeeCertificate -> employeeCertificate.setEmployee(employee));
        employee.getEmploymentHistory().forEach(employmentHistory -> employmentHistory.setEmployee(employee));
        this.employeeRepository.save(employee);
    }

    @Transactional
    public EmployeeDTO updateEmployeeJob(Long id, Job newJob) throws EmployeeException, JobException {
        Employee employee = employeeRepository.findByIdWithJobAndDepartment(id).orElseThrow(()->new EmployeeException("Can't find employee with id: " + id));
        Long jobId = newJob.getId();
        newJob = jobRepository.findById(jobId).orElseThrow(()->new JobException("Can't find job with id: " + jobId));
        employee.setDepartment(newJob.getDepartment());
        employee.setJob(newJob);
        return EmployeeMapperImpl.INSTANCE.toEmployeeDTO(employee);
    }
}
