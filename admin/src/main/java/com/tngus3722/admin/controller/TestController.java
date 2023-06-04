package com.tngus3722.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;
import vo.AuthInfoVO;

@RequiredArgsConstructor
@RestController
public class TestController {

    @GetMapping("test")
    public ResponseEntity<AuthInfoVO> getTest(@RequestAttribute("AuthInfo") AuthInfoVO authInfoVO) {
        double rand = Math.random();
        System.out.println(rand);
        if (Math.random() < 0.2) {
            throw new RuntimeException("");
        }
        return ResponseEntity.ok(authInfoVO);
    }
}
