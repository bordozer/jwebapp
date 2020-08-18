package com.bordozer.webapp.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class LambdaErrorResponse implements LambdaResponse {
    private String message;
}
