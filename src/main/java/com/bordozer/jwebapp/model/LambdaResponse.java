package com.bordozer.jwebapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class LambdaResponse {
    @JsonIgnore
    @NonNull
    private final HttpStatus status;
    @NonNull
    private final String value;

    public int getStatusCode() {
        return status.value();
    }
}
