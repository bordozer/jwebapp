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

        final LambdaResponse response = new LambdaResponse();
        response.setStatus(httpStatus);

        switch (httpStatus) {
            case OK:
                response.setValue(JsonUtils.read(responseBody, LambdaSuccessResponse.class).getPayload());
                break;
            case UNAUTHORIZED:
            case FORBIDDEN:
                response.setValue(JsonUtils.read(responseBody, LambdaUnauthorizedResponse.class).getMessage());
                break;
            default:
                throw new IllegalArgumentException(String.format("Unsupported lambda's response status: \"%s\"", httpStatus));
        }
        return response;
    }
}
