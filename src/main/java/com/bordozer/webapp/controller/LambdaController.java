package com.bordozer.webapp.controller;

import com.bordozer.webapp.LambdaClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LambdaController {

    private final LambdaClient lambdaClient;

    @GetMapping("")
    public ResponseEntity<String> localize() {
        log.info("Health check");
        final var value = lambdaClient.invoke();
        return new ResponseEntity<>(value, HttpStatus.OK);
    }
}
