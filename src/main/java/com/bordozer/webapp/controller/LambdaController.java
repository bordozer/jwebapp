package com.bordozer.webapp.controller;

import com.bordozer.webapp.LambdaWrapper;
import com.bordozer.webapp.model.LambdaResponse;
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

    private final LambdaWrapper lambdaWrapper;

    @GetMapping("/lambda")
    public ResponseEntity<LambdaResponse> invokeLambda() {
        log.info("Invoke lambda endpoint");
        final var lambdaResponse = lambdaWrapper.invoke();
        return new ResponseEntity<>(lambdaResponse, lambdaResponse.getStatus());
    }
}
