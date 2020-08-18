package com.bordozer.webapp.controller;

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
@RequestMapping("/api/health")
public class HealthCheckController {

    @GetMapping("")
    public ResponseEntity<String> localize() {
        log.info("Health check");
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
