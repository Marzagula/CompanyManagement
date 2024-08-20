package com.gminds.employee_service;

import com.gminds.employee_service.model.*;
import com.gminds.employee_service.model.dtos.*;
import com.gminds.employee_service.model.enums.AgreementStatus;
import com.gminds.employee_service.model.enums.EmplAgreementType;
import com.gminds.employee_service.model.enums.EmploymentPaymentType;
import com.gminds.employee_service.service.utils.mappers.EmployeeMapper;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class EmployeeMapperTest {

    @Test
    public void shouldMapEmployeeToEmployeeDTO() {
        Employee employee = new Employee();
        employee.setName("John");
        employee.setSurname("Doe");
        employee.setId(1L);

        EmployeeDTO employeeDTO = EmployeeMapper.INSTANCE.toEmployeeDTO(employee);

        assertNotNull(employeeDTO);
        assertEquals(employee.getName(), employeeDTO.name());
        assertEquals(employee.getSurname(), employeeDTO.surname());
        assertEquals(employee.getId(), employeeDTO.id());
    }

    @Test
    public void shouldMapEmployeeToEmployeeDTOWithRelations() {
        EmployeeAgreementDTO employeeAgreementDTO = new EmployeeAgreementDTO(
                1L,
                8000.0,
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                AgreementStatus.ACTIVE,
                EmplAgreementType.B2B,
                EmploymentPaymentType.PER_MONTH,
                1L,
                null,
                null,
                null,
                null
        );
        JobDTO jobDTO = new JobDTO(2L, "Senior Developer", "Experienced software developer", 1L);
        DepartmentDTO departmentDTO = new DepartmentDTO(1L, "IT", "Information Technology department");
        EmployeeCertificateDTO employeeCertificateDTO = new EmployeeCertificateDTO(
                1L,
                "Cert Name",
                LocalDate.now().minus(100, ChronoUnit.DAYS),
                null,
                "Cert Company",
                1L,
                null,
                null,
                null,
                null
        );

        EmploymentHistoryDTO employmentHistoryDTO = new EmploymentHistoryDTO(
                1L,
                "Comp Name 2",
                "Some job 2",
                LocalDate.now().minus(3, ChronoUnit.YEARS),
                LocalDate.now().minus(30, ChronoUnit.DAYS),
                1L
        );
        EmployeeDTO employeeDTO = new EmployeeDTO(1L,
                "John",
                "Doe",
                jobDTO,
                departmentDTO,
                List.of(employeeAgreementDTO),
                Set.of(employeeCertificateDTO),
                List.of(employmentHistoryDTO)
        );

        Employee employee = EmployeeMapper.INSTANCE.toEmployee(employeeDTO);

        assertNotNull(employee);
        assertEquals(employeeDTO.name(), employee.getName());
        assertEquals(employeeDTO.surname(), employee.getSurname());
        assertEquals(employeeDTO.id(), employee.getId());
        assertJob(employeeDTO.job(), employee.getJob());
        assertDepartment(employeeDTO.department(), employee.getDepartment());
        assertAgreements(employeeDTO.agreements(), employee.getAgreements());
        assertCertificates(employeeDTO.certificates(), employee.getCertificates());
        assertEmploymentHistories(employeeDTO.employmentHistory(), employee.getEmploymentHistory());

    }

    void assertJob(JobDTO jobDTO, Job job) {
        assertEquals(job.getId(), jobDTO.id());
        assertEquals(job.getDepartment().getId(), jobDTO.departmentId());
        assertEquals(job.getTitle(), jobDTO.title());
        assertEquals(job.getDescription(), jobDTO.description());
    }

    void assertDepartment(DepartmentDTO departmentDTO, Department department) {
        assertEquals(departmentDTO.id(), department.getId());
        assertEquals(departmentDTO.description(), department.getDescription());
        assertEquals(departmentDTO.name(), department.getName());
    }

    void assertAgreements(List<EmployeeAgreementDTO> agreementDTOS, List<EmployeeAgreement> agreements) {

        Map<Long, EmployeeAgreement> agreementsMap = agreements.stream()
                .collect(Collectors.toMap(EmployeeAgreement::getId, agreement -> agreement));

        agreementDTOS.forEach(agreementDTO -> {
            EmployeeAgreement agreement = agreementsMap.get(agreementDTO.id());
            assertNotNull(agreement, "Agreement not found for id: " + agreementDTO.id());
            assertAgreement(agreementDTO, agreement);
        });
    }

    void assertAgreement(EmployeeAgreementDTO agreementDTO, EmployeeAgreement agreement) {
        assertEquals(agreementDTO.agreementType(), agreement.getAgreementType());
        assertEquals(agreementDTO.salary(), agreement.getSalary());
        assertEquals(agreementDTO.fromDate(), agreement.getFromDate());
        assertEquals(agreementDTO.toDate(), agreement.getToDate());
        assertEquals(agreementDTO.paymentType(), agreement.getPaymentType());
        assertEquals(agreementDTO.status(), agreement.getStatus());
        assertEquals(agreementDTO.employeeId(), agreement.getEmployee().getId());
    }

    void assertCertificates(Set<EmployeeCertificateDTO> certificateDTOS, Set<EmployeeCertificate> employeeCertificates) {
        Map<Long, EmployeeCertificate> certificateMap = employeeCertificates.stream()
                .collect(Collectors.toMap(EmployeeCertificate::getId, agreement -> agreement));

        certificateDTOS.forEach(certificateDTO -> {
            EmployeeCertificate certificate = certificateMap.get(certificateDTO.id());
            assertNotNull(certificate, "Certificate not found for id: " + certificateDTO.id());
            assertCertificate(certificateDTO, certificate);
        });
    }

    void assertCertificate(EmployeeCertificateDTO employeeCertificateDTO, EmployeeCertificate employeeCertificate) {
        assertEquals(employeeCertificateDTO.certificateName(), employeeCertificate.getCertificateName());
        assertEquals(employeeCertificateDTO.employeeId(), employeeCertificate.getEmployee().getId());
        assertEquals(employeeCertificateDTO.expiryDate(), employeeCertificate.getExpiryDate());
        assertEquals(employeeCertificateDTO.issueDate(), employeeCertificate.getIssueDate());
        assertEquals(employeeCertificateDTO.issuedBy(), employeeCertificate.getIssuedBy());
    }

    void assertEmploymentHistories(List<EmploymentHistoryDTO> employmentHistoryDTOS, List<EmploymentHistory> employmentHistories) {
        Map<Long, EmploymentHistory> certificateMap = employmentHistories.stream()
                .collect(Collectors.toMap(EmploymentHistory::getId, agreement -> agreement));

        employmentHistoryDTOS.forEach(employmentHistoryDTO -> {
            EmploymentHistory employmentHistory = certificateMap.get(employmentHistoryDTO.id());
            assertNotNull(employmentHistory, "Certificate not found for id: " + employmentHistoryDTO.id());
            assertEmploymentHistory(employmentHistoryDTO, employmentHistory);
        });
    }

    void assertEmploymentHistory(EmploymentHistoryDTO employmentHistoryDTO, EmploymentHistory employmentHistory) {
        assertEquals(employmentHistoryDTO.employeeId(), employmentHistory.getEmployee().getId());
        assertEquals(employmentHistoryDTO.fromDate(), employmentHistory.getFromDate());
        assertEquals(employmentHistoryDTO.toDate(), employmentHistory.getToDate());
        assertEquals(employmentHistoryDTO.companyName(), employmentHistory.getCompanyName());
        assertEquals(employmentHistoryDTO.jobName(), employmentHistory.getJobName());
    }
}
