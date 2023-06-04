package com.tngus3722.gateway.filter;

import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


// NOTE 조잡하지만 손으로 직접 구현한 circuitBreakerFilter
@Slf4j
@Component
public class CircuitBreakerFilter extends AbstractGatewayFilterFactory<CircuitBreakerFilter.CircuitBreakerFilterConfig> {

    private final ReactiveCircuitBreakerFactory circuitBreakerFactory;

    public CircuitBreakerFilter(ReactiveCircuitBreakerFactory circuitBreakerFactory) {
        super(CircuitBreakerFilterConfig.class);
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    public GatewayFilter apply(CircuitBreakerFilterConfig config) {
        ReactiveCircuitBreaker reactiveCircuitBreaker = circuitBreakerFactory.create("defaultCircuitBreaker");
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                return reactiveCircuitBreaker.run(chain.filter(exchange)
                    .doOnSuccess(v -> {
                        log.info(exchange.getResponse().getStatusCode().toString());
                        HttpStatus httpStatus = exchange.getResponse().getStatusCode();
                        if (httpStatus.value() >= 400) {
                            throw new RestClientException("error");
                        }
                    }),
                    (t -> {
                        log.error("fallback runed");
                        ServerHttpResponse response = exchange.getResponse();
                        response.setStatusCode(HttpStatus.OK);
                        response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
                        byte[] fallbackBytes = "Fallback".getBytes(StandardCharsets.UTF_8);
                        DataBuffer buffer = response.bufferFactory().wrap(fallbackBytes);
                        return response.writeWith(Mono.just(buffer));
                }));
            }
        };
    }

    public static class CircuitBreakerFilterConfig {

    }
}
