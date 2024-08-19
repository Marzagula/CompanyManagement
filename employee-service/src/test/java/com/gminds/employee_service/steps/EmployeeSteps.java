package com.gminds.employee_service.steps;

import com.gminds.employee_service.config.AuthResourceServerConfig;
import com.gminds.employee_service.model.Department;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.Job;
import com.gminds.employee_service.model.dtos.EmployeeDTO;
import com.gminds.employee_service.model.enums.AgreementStatus;
import com.gminds.employee_service.repository.EmployeeRepository;
import com.gminds.employee_service.repository.JobRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@ActiveProfiles("test")
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
@Import(AuthResourceServerConfig.class)
public class EmployeeSteps {

    private static final String BASE_URL = "http://localhost:8602";
    private static final String AUTH_URL = "http://localhost:9000";
    private static final String HEALTH_ENDPOINT = "/actuator/health";
    private static final String CREATE_EMPLOYEE_ENDPOINT = "/api/v1/employees";


    @Autowired
    JobRepository jobRepository;
    List<Job> jobs = new ArrayList<>();
    private String token;
    @Autowired
    private EmployeeRepository employeeRepository;
    private Long employeeId;

    @Autowired
    private DataSource dataSource;

    @Before
    public void init() {

        jobs = jobRepository.findAll();
    }


    @Then("the employee service is running")
    public void theEmployeeServiceIsRunning() {
        RestClient restClient = RestClient.create();
        String url = BASE_URL + HEALTH_ENDPOINT;

        try {
            ResponseEntity<String> response = restClient.get()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .toEntity(String.class);


            assertEquals(200, response.getStatusCode().value());
        } catch (RestClientException e) {
            fail("Service is not running: " + e.getMessage());
        }
    }

    @When("I create a new employee with name {string}")
    public void iCreateANewEmployeeWithNameAndPosition(String name) {
        String url = BASE_URL + CREATE_EMPLOYEE_ENDPOINT;

        Map<String, Object> employeeData = properEmployeeDataB2B(name);


        RestClient restClient = RestClient.create();
        try {
            ResponseEntity<EmployeeDTO> response = restClient.post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(employeeData)
                    .retrieve()
                    .toEntity(EmployeeDTO.class);

            employeeId = response.getBody().id();
            assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
        } catch (RestClientException e) {

            fail("Failed to create employee: " + e.getMessage());
        }
    }

    @Then("the employee should be added to the database with name {string}")
    @Transactional(readOnly = true)
    public void theEmployeeShouldBeAddedToTheDatabase(String name) {
        try {
            Employee employee = employeeRepository.findById(employeeId).orElse(null);
            assertTrue("Employee was not found in the database", employee != null);
            assertEquals(name, "John Doe");
        } catch (Exception e) {
            fail("An error occurred while verifying the employee in the database: " + e.getMessage());
        }
    }

    @Given("I am authenticated as {string} with password {string}")
    public void iAmAuthenticatedAsUserWithPassword(String username, String password) {
        RestClient client = RestClient.create();
        String tokenEndpoint = AUTH_URL + "/token"; // Upewnij się, że AUTH_URL jest poprawnie ustawione

        try {
            ResponseEntity<String> response = client.post()
                    .uri(tokenEndpoint)
                    .header(HttpHeaders.AUTHORIZATION, createBasicAuthHeader(username, password))
                    .retrieve()
                    .toEntity(String.class);

            HttpStatusCode statusCode = response.getStatusCode();
            assertEquals(HttpStatus.OK.value(), statusCode.value());

            // Ponieważ token jest płaskim stringiem, możemy go bezpośrednio przypisać
            token = response.getBody();

        } catch (Exception e) {
            fail("Failed to authenticate: " + e.getMessage());
        }
    }

    private String createBasicAuthHeader(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedAuth);
    }

    Map<String, Object> properEmployeeDataB2B(String name) {

        String[] fullName = name.split(" ");

        Map<String, Object> employeeData = new HashMap<>();
        employeeData.put("id", null);
        employeeData.put("name", fullName[0]);
        employeeData.put("surname", fullName[1]);

// Job details
        Map<String, Object> jobData = new HashMap<>();
        jobData.put("id", 1);
        jobData.put("title", "Software Engineer");
        jobData.put("description", "Develops and maintains software applications.");
        jobData.put("departmentId", 2);
        employeeData.put("job", jobData);

// Department details
        Map<String, Object> departmentData = new HashMap<>();
        departmentData.put("id", 2);
        departmentData.put("name", "IT");
        departmentData.put("description", "Information Technology department.");
        employeeData.put("department", departmentData);

// Agreements
        Map<String, Object> agreementData = new HashMap<>();
        agreementData.put("id", null);
        agreementData.put("salary", 5500.0);
        agreementData.put("fromDate", "2024-09-01");
        agreementData.put("toDate", "2025-08-31");
        agreementData.put("status", "ACTIVE");
        agreementData.put("agreementType", "B2B");
        agreementData.put("paymentType", "PER_MONTH");
        agreementData.put("employeeId", null);
        agreementData.put("createdBy", null);
        agreementData.put("createdDate", null);
        agreementData.put("lastModifiedBy", null);
        agreementData.put("lastModifiedDate", null);
        employeeData.put("agreements", Collections.singletonList(agreementData));

// Certificates
        Map<String, Object> certificateData = new HashMap<>();
        certificateData.put("id", null);
        certificateData.put("certificateName", "Java Professional");
        certificateData.put("issueDate", "2022-03-15");
        certificateData.put("expiryDate", "2027-03-14");
        certificateData.put("issuedBy", "Oracle");
        certificateData.put("employeeId", null);
        certificateData.put("createdBy", null);
        certificateData.put("createdDate", null);
        certificateData.put("lastModifiedBy", null);
        certificateData.put("lastModifiedDate", null);
        employeeData.put("certificates", Collections.singletonList(certificateData));

// Employment History
        Map<String, Object> employmentHistoryData = new HashMap<>();
        employmentHistoryData.put("id", null);
        employmentHistoryData.put("companyName", "Amsterdam Tech Solutions");
        employmentHistoryData.put("jobName", "Junior Software Engineer");
        employmentHistoryData.put("fromDate", "2022-07-01");
        employmentHistoryData.put("toDate", "2024-08-31");
        employmentHistoryData.put("employeeId", null);
        employeeData.put("employmentHistory", Collections.singletonList(employmentHistoryData));

        return employeeData;
    }


    @When("I create a new employee with the following details:")
    public void iCreateANewEmployeeWithDetails(DataTable dataTable) {
        Map<String, String> employeeDetails = dataTable.asMap(String.class, String.class);

        String url = BASE_URL + CREATE_EMPLOYEE_ENDPOINT;

        // Tworzenie struktury EmployeeDTO na podstawie przekazanych danych
        Map<String, Object> employeeData = new HashMap<>();
        employeeData.put("id", null);
        employeeData.put("name", employeeDetails.get("name"));
        employeeData.put("surname", employeeDetails.get("surname"));

        // Job details
        Map<String, Object> jobData = findOrCreateJob(employeeDetails.get("jobTitle"), employeeDetails.get("departmentName"));
        employeeData.put("job", jobData);

        // Department details
        Map<String, Object> departmentData = findOrCreateDepartment(employeeDetails.get("departmentName"));
        employeeData.put("department", departmentData);

        // Agreements
        Map<String, Object> agreementData = createAgreement(employeeDetails.get("salary"), employeeDetails.get("agreementType"));
        employeeData.put("agreements", Collections.singletonList(agreementData));

        // Certificates
        Map<String, Object> certificateData = createCertificate(employeeDetails.get("certificateName"));
        employeeData.put("certificates", Collections.singletonList(certificateData));

        // Employment History
        Map<String, Object> employmentHistoryData = createEmploymentHistory(employeeDetails.get("companyName"), jobData.get("title"));
        employeeData.put("employmentHistory", Collections.singletonList(employmentHistoryData));

        // Wysyłanie żądania do serwera
        RestClient restClient = RestClient.create();
        try {
            ResponseEntity<EmployeeDTO> response = restClient.post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(employeeData)
                    .retrieve()
                    .toEntity(EmployeeDTO.class);

            assertNotNull(response.getBody().id(), "Employee ID should not be null");
            employeeId = response.getBody().id();
            assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
        } catch (RestClientException e) {
            fail("Failed to create employee: " + e.getMessage());
        }
    }

    @Then("the employee should be added to the database with name {string} and surname {string}")
    public void the_employee_should_be_added_to_the_database_with_name_and_surname(String name, String surname) {
        try {
            Employee employee = employeeRepository.findById(employeeId).orElse(null);
            assertTrue("Employee was not found in the database", employee != null);
            assertEquals(name, employee.getName());
            assertEquals(surname, employee.getSurname());
        } catch (Exception e) {
            fail("An error occurred while verifying the employee in the database: " + e.getMessage());
        }
    }

    private Map<String, Object> findOrCreateJob(String jobTitle, String departmentName) {
        Job job = jobs.stream()
                .filter(j -> j.getTitle().equals(jobTitle)
                        && j.getDepartment().getName().equals(departmentName))
                .findFirst().orElseThrow();
        Map<String, Object> jobData = new HashMap<>();
        jobData.put("id", job.getId()); // Zakładane ID stanowiska pracy
        jobData.put("title", job.getTitle());
        jobData.put("description", job.getDescription());
        jobData.put("departmentId", job.getDepartment().getId());
        return jobData;
    }

    private Map<String, Object> findOrCreateDepartment(String departmentName) {
        Department department = jobs.stream()
                .filter(job -> job.getDepartment().getName().equals(departmentName))
                .findFirst().orElseThrow().getDepartment();
        // Logika znajdowania lub tworzenia działu
        Map<String, Object> departmentData = new HashMap<>();
        departmentData.put("id", department.getId()); // Zakładane ID działu
        departmentData.put("name", department.getName());
        departmentData.put("description", department.getDescription());
        return departmentData;
    }

    private Map<String, Object> createAgreement(String salary, String agreementType) {
        Map<String, Object> agreementData = new HashMap<>();
        agreementData.put("id", null);
        agreementData.put("salary", Double.valueOf(salary));
        agreementData.put("fromDate", LocalDate.now());
        agreementData.put("toDate", LocalDate.now().plus(364, ChronoUnit.DAYS));
        agreementData.put("status", "ACTIVE");
        agreementData.put("agreementType", agreementType);
        agreementData.put("paymentType", "PER_MONTH");
        return agreementData;
    }

    private Map<String, Object> createAgreement(String salary, String agreementType, Long id) {
        Map<String, Object> agreementData = new HashMap<>();
        agreementData.put("id", null);
        agreementData.put("salary", Double.valueOf(salary));
        agreementData.put("fromDate", LocalDate.now());
        agreementData.put("toDate", LocalDate.now().plus(364, ChronoUnit.DAYS));
        agreementData.put("status", "ACTIVE");
        agreementData.put("agreementType", agreementType);
        agreementData.put("paymentType", "PER_MONTH");
        agreementData.put("employeeId", id);
        return agreementData;
    }


    private Map<String, Object> createAgreement(String salary, String agreementType, LocalDate startDate, LocalDate endDate) {
        Map<String, Object> agreementData = new HashMap<>();
        agreementData.put("id", null);
        agreementData.put("salary", Double.valueOf(salary));
        agreementData.put("fromDate", startDate);
        agreementData.put("toDate", endDate);
        agreementData.put("status", "ACTIVE");
        agreementData.put("agreementType", agreementType);
        agreementData.put("paymentType", "PER_MONTH");
        return agreementData;
    }

    private Map<String, Object> createCertificate(String certificateName) {
        Map<String, Object> certificateData = new HashMap<>();
        certificateData.put("id", null);
        certificateData.put("certificateName", certificateName);
        certificateData.put("issueDate", LocalDate.now().minus(3, ChronoUnit.YEARS));
        certificateData.put("expiryDate", LocalDate.now().plus(3, ChronoUnit.YEARS));
        certificateData.put("issuedBy", "Certifying Body");
        return certificateData;
    }

    private Map<String, Object> createEmploymentHistory(String companyName, Object jobName) {
        Map<String, Object> employmentHistoryData = new HashMap<>();
        employmentHistoryData.put("id", null);
        employmentHistoryData.put("companyName", companyName);
        employmentHistoryData.put("jobName", jobName);
        employmentHistoryData.put("fromDate", LocalDate.now().minus(2, ChronoUnit.YEARS));
        employmentHistoryData.put("toDate", LocalDate.now().minus(10, ChronoUnit.DAYS));
        return employmentHistoryData;
    }


    @And("I update the agreements for these employees with the following details:")
    public void iUpdateTheAgreementsForTheseEmployeesWithTheFollowingDetails(DataTable dataTable) {
        List<Map<String, String>> agreements = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> agreementDetails : agreements) {
            Employee employee = employeeRepository
                    .findByNameAndSurnameWithAgreements(
                            agreementDetails.get("name"),
                            agreementDetails.get("surname"))
                    .orElse(null);

            String updateAgreementEndpoint = "/api/v1/employees/{employeeId}/agreement";
            if (employee != null) {

                String url = BASE_URL + updateAgreementEndpoint.replace("{employeeId}", employee.getId().toString());

                Map<String, Object> agreementData = createAgreement(
                        agreementDetails.get("newSalary"),
                        employee.getAgreements()
                                .getFirst()
                                .getAgreementType().name(),
                        employee.getId()
                );

                RestClient restClient = RestClient.create();
                try {
                    restClient.post()
                            .uri(url)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .body(agreementData)
                            .retrieve()
                            .toEntity(Void.class);
                    fail("Expected HttpStatusCodeException not thrown");
                } catch (HttpStatusCodeException ex) {
                    assertEquals(HttpStatus.BAD_REQUEST.value(), ex.getStatusCode().value());
                }
            } else {
                fail("Employee not found: " + agreementDetails.get("name") + " " + agreementDetails.get("surname"));
            }
        }
    }

    @Then("New agreement for {string} {string} with salary {string} should be prevented")
    public void newAgreementWithWithSalaryShouldBePrevented(String name, String surname, String salary) {
        Employee employee = employeeRepository.findByNameAndSurnameWithAgreements(name, surname).orElse(null);
        assertNotNull(employee, "Employee should exist in the database");

        // Assert that there is no agreement with the specified salary for the given employee
        long count = employee.getAgreements().stream()
                .filter(
                        agr -> agr.getSalary() == Double.valueOf(salary)
                                && (agr.getStatus().equals(AgreementStatus.ACTIVE) || agr.getStatus().equals(AgreementStatus.FUTURE))
                ).count();
        assertEquals(0, count);
    }
}



