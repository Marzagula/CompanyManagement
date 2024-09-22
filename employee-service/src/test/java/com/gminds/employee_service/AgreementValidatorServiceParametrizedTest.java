package com.gminds.employee_service;

import com.gminds.employee_service.exceptions.EmployeeAgreementException;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.EmployeeAgreement;
import com.gminds.employee_service.model.Job;
import com.gminds.employee_service.model.PaymentRange;
import com.gminds.employee_service.model.dtos.*;
import com.gminds.employee_service.model.enums.AgreementStatus;
import com.gminds.employee_service.model.enums.EmplAgreementType;
import com.gminds.employee_service.model.enums.EmploymentPaymentType;
import com.gminds.employee_service.service.agreement.CachedPaymentRangeService;
import com.gminds.employee_service.service.agreement.validator.AgreementValidatorService;
import com.gminds.employee_service.service.utils.mappers.DepartmentMapper;
import com.gminds.employee_service.service.utils.mappers.EmployeeAgreementMapper;
import com.gminds.employee_service.service.utils.mappers.EmployeeMapper;
import com.gminds.employee_service.service.utils.mappers.JobMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class AgreementValidatorServiceParametrizedTest {

    @Parameterized.Parameter(0)
    public double salary;
    @Parameterized.Parameter(1)
    public double minSalary;
    @Parameterized.Parameter(2)
    public double maxSalary;
    @Parameterized.Parameter(3)
    public EmplAgreementType agreementType;
    @Parameterized.Parameter(4)
    public Class<? extends Throwable> expectedException;
    @Mock
    private CachedPaymentRangeService cachedPaymentRangeService;
    private AgreementValidatorService agreementValidatorService;

    @Parameterized.Parameters(name = "{index}: Test with salary={0}, minSalary={1}, maxSalary={2},agreementType={3}, expectedException={4}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {3000.0, 1000.0, 4300.0, EmplAgreementType.B2B, null}, // salary within range
                {500.0, 1000.0, 4300.0, EmplAgreementType.B2B, EmployeeAgreementException.class}, // salary below range
                {5000.0, 1000.0, 4300.0, EmplAgreementType.B2B, EmployeeAgreementException.class}, // salary over range
                {1500.0, 1000.0, 4300.0, EmplAgreementType.EMPLOYMENT, null}, // salary within range
                {500.0, 1000.0, 4300.0, EmplAgreementType.EMPLOYMENT, EmployeeAgreementException.class}, // salary below range
                {6000.0, 1000.0, 4300.0, EmplAgreementType.EMPLOYMENT, EmployeeAgreementException.class} // salary over range
        });
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.agreementValidatorService = new AgreementValidatorService(cachedPaymentRangeService);
    }

    @Test
    public void shouldValidateAgreementBasedOnSalaryRange() {
        // Given
        JobDTO jobDTO = new JobDTO(2L, "Senior Developer", "Experienced software developer", 1L);
        Job job = JobMapper.INSTANCE.toJob(jobDTO);
        DepartmentDTO departmentDTO = DepartmentMapper.INSTANCE.toDepartmentDTO(job.getDepartment());

        EmployeeAgreementDTO employeeAgreementDTO = new EmployeeAgreementDTO(
                1L,
                salary,
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                AgreementStatus.ACTIVE,
                agreementType,
                EmploymentPaymentType.PER_MONTH,
                1L,
                List.of(new EmployeeAgreementClauseDTO(1L,1L, LocalDate.now(),  LocalDate.now().plus(14, ChronoUnit.MONTHS),1L)),
                null,
                null,
                null,
                null
        );

        EmployeeAgreement agreement = EmployeeAgreementMapper.INSTANCE.toEmployeeAgreement(employeeAgreementDTO);
        EmployeeDTO employeeDTO = new EmployeeDTO(
                1L,
                "TestName",
                "TestSurname",
                jobDTO,
                departmentDTO,
                List.of(employeeAgreementDTO),
                new HashSet<>(),
                new ArrayList<>()
        );
        Employee employee = EmployeeMapper.INSTANCE.toEmployee(employeeDTO);

        // Set dependencies between objects
        agreement.setEmployee(employee);
        employee.getAgreements().add(agreement);
        employee.setJob(job);

        // Mock PaymentRange and cachedPaymentRangeService behavior
        PaymentRange paymentRange = new PaymentRange();
        paymentRange.setJob(job);
        paymentRange.setEmplAgreementType(agreementType);
        paymentRange.setMinSalary(minSalary);
        paymentRange.setMaxSalary(maxSalary);
        paymentRange.setFiscalYear(2024);

        when(cachedPaymentRangeService.getCachedPaymentRanges()).thenReturn(List.of(paymentRange));

        if (expectedException != null) {
            // Then
            assertThrows(expectedException, () -> {
                agreementValidatorService.validateAgreement(agreement);
            });
        } else {
            // When/Then - without exception (salary between range)
            agreementValidatorService.validateAgreement(agreement);
        }
    }
}
