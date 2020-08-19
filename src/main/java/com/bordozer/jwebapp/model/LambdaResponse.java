package com.bordozer.jwebapp.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class LambdaResponse {
    private HttpStatus status;
    private String value;

    public int getStatusCode() {
        return status.value();
    }
}
