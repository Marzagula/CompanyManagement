package com.gminds.graphql_api_gateway;

import com.gminds.graphql_api_gateway.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(RsaKeyProperties.class)
public class GraphqlApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqlApiGatewayApplication.class, args);
    }

}
