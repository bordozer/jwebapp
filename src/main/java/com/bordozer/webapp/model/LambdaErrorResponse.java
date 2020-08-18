package com.bordozer.webapp.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class LambdaErrorResponse extends AbstractLambdaResponse {
    private String message;
}
