package com.bordozer.jwebapp.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LambdaInvokeException extends RuntimeException {
    private final String message;
}
