package com.tngus3722.internal.controller;

import constant.ServerTypeEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("test")
    public ResponseEntity getTest() {
        return ResponseEntity.ok(ServerTypeEnum.INTERNAL.getCode());
    }
}
