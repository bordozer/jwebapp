package com.bordozer.webapp.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public abstract class AbstractLambdaResponse {
    private HttpStatus status;
}
