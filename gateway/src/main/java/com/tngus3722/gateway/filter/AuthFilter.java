package com.tngus3722.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import vo.AuthInfoVO;

@Slf4j
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.AuthFilterConfig> {

    private final ObjectMapper objectMapper;

    public AuthFilter(ObjectMapper objectMapper) {
        super(AuthFilterConfig.class);
        this.objectMapper = objectMapper;
    }

    @Override
    public GatewayFilter apply(AuthFilterConfig config) {
        return (exchange, chain) -> {
            log.info("filter mutate requestHeader");
            AuthInfoVO authInfoVO = getAuthInfo();
            exchange.mutate().request(request ->
                request.headers(headers -> {
                    try {
                        headers.add(AuthInfoVO.AUTH_HEADER_KEY, objectMapper.writeValueAsString(authInfoVO));
                    } catch (JsonProcessingException e) {
                        log.error("json parse exception");
                    }
                }
            ));

            return chain.filter(exchange);
        };
    }

    private AuthInfoVO getAuthInfo() {
        // to be auth Server response
        return AuthInfoVO.builder()
            .isAuth(true)
            .userId(1L)
            .build();
    }

    public static class AuthFilterConfig {
        // some config to be
    }
}
