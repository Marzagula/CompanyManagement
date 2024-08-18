package com.gminds.employee_service.steps;

import com.gminds.employee_service.config.AuthResourceServerConfig;
import com.gminds.employee_service.model.Employee;
import com.gminds.employee_service.model.dtos.EmployeeDTO;
import com.gminds.employee_service.repository.EmployeeRepository;
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
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ActiveProfiles("test")
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(AuthResourceServerConfig.class)
public class EmployeeSteps {

    private static final String BASE_URL = "http://localhost:8602";
    private static final String AUTH_URL = "http://localhost:9000";
    private static final String HEALTH_ENDPOINT = "/actuator/health";
    private static final String CREATE_EMPLOYEE_ENDPOINT = "/api/v1/employees";
    private String token;

    @Autowired
    private EmployeeRepository employeeRepository;
    private Long employeeId;




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


            assertEquals(200, response.getStatusCodeValue(), "Service is not running");
        } catch (RestClientException e) {
            fail("Service is not running: " + e.getMessage());
        }
    }

    @When("I create a new employee with name {string} and position {string}")
    public void iCreateANewEmployeeWithNameAndPosition(String name, String position) {
        String url = BASE_URL + CREATE_EMPLOYEE_ENDPOINT;

        Map<String, Object> employeeData = properEmployeeDataB2B();


        RestClient restClient = RestClient.create();
        try {
            ResponseEntity<EmployeeDTO> response = restClient.post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body(employeeData)
                    .retrieve()
                    .toEntity(EmployeeDTO.class);

            employeeId = response.getBody().id();
            assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Employee was not created successfully");
        } catch (RestClientException e) {

            fail("Failed to create employee: " + e.getMessage());
        }
    }

    @Then("the employee should be added to the database")
    @Transactional(readOnly = true)
    public void theEmployeeShouldBeAddedToTheDatabase() {
        // Zakładam, że pracownik ma unikalną nazwę i nazwisko
        try {
            Employee employee = employeeRepository.findById(employeeId).orElse(null);
            assertTrue("Employee was not found in the database", employee != null);
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
            assertEquals(HttpStatus.OK, statusCode, "Authentication failed!");

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

    Map<String, Object> properEmployeeDataB2B() {

        Map<String, Object> employeeData = new HashMap<>();
        employeeData.put("id", null);
        employeeData.put("name", "Jeroen");
        employeeData.put("surname", "van Dijk");

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
}
