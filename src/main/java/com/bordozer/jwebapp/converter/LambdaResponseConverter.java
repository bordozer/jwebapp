package com.bordozer.jwebapp.converter;

import com.bordozer.jwebapp.model.LambdaResponse;
import com.bordozer.jwebapp.model.LambdaSuccessResponse;
import com.bordozer.jwebapp.model.LambdaUnauthorizedResponse;
import com.bordozer.jwebapp.utils.JsonUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LambdaResponseConverter {

    public static LambdaResponse convert(final int responseCode, final String responseBody) {
        final var httpStatus = HttpStatus.valueOf(responseCode);
        return LambdaResponse.builder()
                .status(httpStatus)
                .value(getBody(responseBody, httpStatus))
                .build();
    }

    private static String getBody(final String responseBody, final HttpStatus httpStatus) {
        if (httpStatus == HttpStatus.OK) {
            return JsonUtils.read(responseBody, LambdaSuccessResponse.class).getPayload();
        }
        return JsonUtils.read(responseBody, LambdaUnauthorizedResponse.class).getMessage();
    }
}
