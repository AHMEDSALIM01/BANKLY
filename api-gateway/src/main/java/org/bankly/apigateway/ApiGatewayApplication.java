package org.bankly.apigateway;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import org.bankly.apigateway.security.JwtAuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class,args);
    }
    @Bean
    public JwtParser jwtParser() {
        return Jwts.parser().setSigningKey("monSecret926600".getBytes());
    }

    @Bean
    public JwtAuthFilter jwtFilter(JwtParser jwtParser) {
        return new JwtAuthFilter(jwtParser);
    }
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("wallet-service", r -> r.path("/api/wallets/**")
                        .filters(f -> f.filter(jwtFilter(jwtParser())))
                        .uri("lb://wallet-service"))
                .route("transaction-service", r -> r.path("/api/transactions/**")
                        .filters(f -> f.filter(jwtFilter(jwtParser())))
                        .uri("lb://transaction-service"))
                .build();
    }
}
