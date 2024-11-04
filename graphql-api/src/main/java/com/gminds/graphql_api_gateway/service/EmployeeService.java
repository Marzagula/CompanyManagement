package com.gminds.graphql_api_gateway.service;

import com.gminds.graphql_api_gateway.exceptions.ServiceException;
import com.gminds.graphql_api_gateway.model.dtos.EmployeeDTO;
import com.gminds.graphql_api_gateway.model.dtos.EmployeePage;
import com.gminds.graphql_api_gateway.service.utils.SecurityUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
public class EmployeeService {

    private final RestClient restClient;

    public EmployeeService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public EmployeeDTO getEmployeeById(Long id) {
        String url = "http://EMPLOYEE-SERVER" + "/api/v1/employees/" + id;

        try {
            return restClient.get()
                    .uri(url)
                    .headers(httpHeaders -> httpHeaders.addAll(SecurityUtils.createHeadersWithToken()))
                    .retrieve()
                    .body(EmployeeDTO.class);
        } catch (RestClientResponseException e) {
            throw new ServiceException("Error retrieving employee data: " + e.getMessage());
        }
    }


    public Page<EmployeeDTO> getAllEmployeesPaged(PageRequest pageRequest) {
        String url = "http://EMPLOYEE-SERVER/api/v1/employees";
        try {
            EmployeePage employeePage = restClient.get()
                    .uri(url)
                    .headers(httpHeaders -> httpHeaders.addAll(SecurityUtils.createHeadersWithToken()))
                    .retrieve()
                    .body(EmployeePage.class);

            return new PageImpl<>(employeePage.getContent(), PageRequest.of(employeePage.getNumber(), employeePage.getSize()), employeePage.getTotalElements());
        } catch (RestClientResponseException e) {
            throw new ServiceException("Error retrieving employee data: " + e.getMessage());
        }
    }
}
