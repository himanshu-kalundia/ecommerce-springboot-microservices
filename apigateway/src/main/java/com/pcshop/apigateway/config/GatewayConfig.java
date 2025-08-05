package com.pcshop.apigateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter customGlobalFilter() {
        return (exchange, chain) -> {
//            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
//            System.out.println("Forwarding JWT to downstream service: " + authHeader);
//
//            if (authHeader != null && !authHeader.isBlank()) {
//                ServerWebExchange mutatedExchange = exchange.mutate()
//                        .request(builder -> builder.header("Authorization", authHeader))
//                        .build();
//
//                return chain.filter(mutatedExchange); // correct Mono<Void> return
//            }

            return chain.filter(exchange); // required Mono<Void> return
        };
    }
}
