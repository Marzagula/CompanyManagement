package org.gminds.accounting_service.service;

import com.netflix.discovery.EurekaClient;
import org.gminds.accounting_service.model.dtos.EmployeeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.RestClient;

import java.util.List;

public class CachedEmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(CachedEmployeeService.class);
    EurekaClient eurekaClient;
    private long lastUpdateTimestamp;
    private List<EmployeeDTO> cachedEmployees;
    @Value("${cache.refresh.interval}")
    private Long refreshRate;

    public CachedEmployeeService(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    public synchronized void refreshCache() {
        RestClient client = RestClient.create();
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String token = String.valueOf(authentication.getToken().getTokenValue());
        //InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("accounting-service", false);
        //String accountingServiceUrl = instanceInfo.getHostName();
        String accountingServiceUrl = "http://localhost:8082";
        String url = accountingServiceUrl + "/api/v1/employees";

        this.cachedEmployees = client.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        this.lastUpdateTimestamp = System.currentTimeMillis();
        logger.info("Cache of CachedPaymentRangeService has been refreshed.");
    }

    public List<EmployeeDTO> getCachedEmployees() {
        if (isCacheStale()) {
            refreshCache();
        }
        return cachedEmployees;
    }

    private boolean isCacheStale() {
        long currentTime = System.currentTimeMillis();
        long cacheAge = currentTime - lastUpdateTimestamp;
        long cacheExpiry = refreshRate;
        return cacheAge > cacheExpiry;
    }
}
