package com.bordozer.webapp.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class LambdaResponse {
    private HttpStatus status;
    private String value;
}
