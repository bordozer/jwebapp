package com.bordozer.webapp.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class LambdaSuccessResponse implements LambdaResponse {
    private String payload;
}
