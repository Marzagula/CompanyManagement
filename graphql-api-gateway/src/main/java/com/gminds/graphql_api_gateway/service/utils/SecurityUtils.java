package com.gminds.graphql_api_gateway.service.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Objects;
import java.util.Optional;

public class SecurityUtils {

    public static HttpHeaders createHeadersWithToken() {
        HttpHeaders headers = new HttpHeaders();
        String accessToken = Objects.requireNonNull(getAccessToken()).orElseThrow(() -> new RuntimeException("Authentication failed at the API Gateway: missing valid access token. Ensure your session is active and retry."));

        if (accessToken != null) {
            headers.set("Authorization", "Bearer " + accessToken);
        }

        return headers;
    }

    private static Optional<String> getAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getTokenValue().describeConstable();
        }

        return Optional.empty();
    }
}