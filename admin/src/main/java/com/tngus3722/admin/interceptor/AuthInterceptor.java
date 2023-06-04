package com.tngus3722.admin.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import vo.AuthInfoVO;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        AuthInfoVO authInfoVO;
        try {
            String authHeaderInfo = request.getHeader(AuthInfoVO.AUTH_HEADER_KEY);
            authInfoVO = objectMapper.readValue(authHeaderInfo, AuthInfoVO.class);
            log.info(authInfoVO.toString());
        } catch (Exception e) {
            authInfoVO = AuthInfoVO.builder()
                .isAuth(false)
                .userId(1L)
                .build();
        }
        request.setAttribute("AuthInfo", authInfoVO);
        return true;
    }
}
