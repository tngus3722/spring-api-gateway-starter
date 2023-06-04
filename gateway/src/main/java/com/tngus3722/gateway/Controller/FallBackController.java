package com.tngus3722.gateway.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallBackController {

    @GetMapping("/fallback")
    public Mono<String> getFallBack() {
        return Mono.just("fallback response");
    }
}
