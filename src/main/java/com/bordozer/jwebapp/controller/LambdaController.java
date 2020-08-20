package com.bordozer.jwebapp.controller;

import com.bordozer.jwebapp.LambdaWrapper;
import com.bordozer.jwebapp.model.Lambda;
import com.bordozer.jwebapp.model.LambdaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LambdaController {

    private final LambdaWrapper lambdaWrapper;

    @GetMapping("/lambda")
    public ResponseEntity<LambdaResponse> invokeLambda(@RequestParam final Lambda function) {
        log.info("JWebapp lambda endpoint");
        final var lambdaResponse = lambdaWrapper.get(function);
        return new ResponseEntity<>(lambdaResponse, getHttpStatus(lambdaResponse));
    }

    private HttpStatus getHttpStatus(final LambdaResponse lambdaResponse) {
        return lambdaResponse.getStatus() == HttpStatus.OK ? HttpStatus.OK : HttpStatus.EXPECTATION_FAILED;
    }
}
