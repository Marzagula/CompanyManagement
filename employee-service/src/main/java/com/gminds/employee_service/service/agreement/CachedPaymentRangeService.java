package com.gminds.employee_service.service.agreement;

import com.gminds.employee_service.model.dtos.PaymentRangeDTO;
import com.netflix.discovery.EurekaClient;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class CachedPaymentRangeService {
    private static final Logger logger = LoggerFactory.getLogger(CachedPaymentRangeService.class);
    EurekaClient eurekaClient;
    private List<PaymentRangeDTO> cachedPaymentRanges;
    private long lastUpdateTimestamp;
    @Value("${cache.refresh.interval}")
    private Long refreshRate;

    public CachedPaymentRangeService(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    @PostConstruct
    public void init() {
    }

    /*TODO wykorzystac zuul do pobierania poprawnego adresu serwisu*/
    public synchronized void refreshCache() {
        RestClient client = RestClient.create();
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String token = String.valueOf(authentication.getToken().getTokenValue());
        //InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("accounting-service", false);
        //String accountingServiceUrl = instanceInfo.getHostName();
        String accountingServiceUrl = "http://localhost:8083";
        String url = accountingServiceUrl + "/api/v1/employee/paymentRanges";

        this.cachedPaymentRanges = client.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        this.lastUpdateTimestamp = System.currentTimeMillis();
        logger.info("Cache of CachedPaymentRangeService has been refreshed.");
    }

    public List<PaymentRangeDTO> getCachedPaymentRanges() {
        if (isCacheStale()) {
            refreshCache();
        }
        return cachedPaymentRanges;
    }

    private boolean isCacheStale() {
        long currentTime = System.currentTimeMillis();
        long cacheAge = currentTime - lastUpdateTimestamp;
        long cacheExpiry = refreshRate;
        return cacheAge > cacheExpiry;
    }
}

