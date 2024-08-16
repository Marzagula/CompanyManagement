package com.gminds.employee_service.service;

import com.gminds.employee_service.exceptions.ResourceNotFoundException;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.Job;
import com.gminds.employee_service.model.dtos.JobDTO;
import com.gminds.employee_service.repository.EmployeeRepository;
import com.gminds.employee_service.repository.JobRepository;
import com.gminds.employee_service.util.mappers.JobMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JobService extends AbstractTransactionalService<JobDTO> {

    private static final Logger logger = LoggerFactory.getLogger(JobService.class);
    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;

    public JobService(EmployeeRepository employeeRepository, JobRepository jobRepository) {
        this.employeeRepository = employeeRepository;
        this.jobRepository = jobRepository;
    }

    public JobDTO updateEmployeeJob(Long id, @Valid JobDTO jobDTO) {
        return executeChanges(id, jobDTO);
    }

    @Override
    protected Employee findEmployeeById(Long id) {
        return employeeRepository.findByIdWithJobAndDepartment(id)
                .orElseThrow(() -> new ResourceNotFoundException("Can't find employee with id: " + id));
    }

    @Override
    protected JobDTO processTransaction(Employee employee, JobDTO jobDTO) {
        Job newJob = jobRepository.findById(jobDTO.id())
                .orElseThrow(() -> new ResourceNotFoundException("Can't find job with id: " + jobDTO.id()));
        employee.setDepartment(newJob.getDepartment());
        employee.setJob(newJob);
        return JobMapper.INSTANCE.toJobDTO(newJob);
    }
}
