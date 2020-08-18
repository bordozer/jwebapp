package com.bordozer.webapp;

import com.bordozer.webapp.exception.LambdaInvokeException;
import com.bordozer.webapp.model.AbstractLambdaResponse;
import com.bordozer.webapp.model.LambdaErrorResponse;
import com.bordozer.webapp.model.LambdaSuccessResponse;
import com.bordozer.webapp.utils.JsonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

import static com.google.common.collect.Lists.newArrayList;

@Slf4j
@Component
public class LambdaWrapper {

    public static final String LAMBDA_PATH = "/api";
    private static final int CONNECTION_TIMEOUT_MS = 20000;

    @Value("${aws.lambda.schema}")
    private String lambdaSchema;
    @Value("${aws.lambda.host}")
    private String lambdaHost;
    @Value("${aws.lambda.port}")
    private Integer lambdaPort;

    @SneakyThrows
    public AbstractLambdaResponse invoke() {
        final URIBuilder builder = new URIBuilder();
        builder.setScheme(lambdaSchema)
                .setHost(lambdaHost)
                .setPort(lambdaPort)
                .setPath(LAMBDA_PATH)
                .setParameters(newArrayList());
        final URI uri = builder.build();
        log.info(String.format("Lambda request string: \"%s\"", uri.toString()));

        final RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECTION_TIMEOUT_MS)
                .setConnectTimeout(CONNECTION_TIMEOUT_MS)
                .setSocketTimeout(CONNECTION_TIMEOUT_MS)
                .build();

        final HttpGet httpGet = new HttpGet(uri);
        httpGet.setConfig(requestConfig);
        httpGet.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        httpGet.addHeader(HttpHeaders.TIMEOUT, "20");

        try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
            try (final CloseableHttpResponse response = httpClient.execute(httpGet)) {
                final var responseCode = response.getStatusLine().getStatusCode();
                final var responseBody = EntityUtils.toString(response.getEntity());
                return getLambdaResponse(responseCode, responseBody);
            }
        } catch (final IOException ex) {
            log.error("Error calling lambda", ex);
            throw new LambdaInvokeException(ex.getMessage());
        }
    }

    private AbstractLambdaResponse getLambdaResponse(final int responseCode, final String responseBody) {
        final var httpStatus = HttpStatus.valueOf(responseCode);
        switch (httpStatus) {
            case OK:
                return createLambdaResponse(httpStatus, responseBody, LambdaSuccessResponse.class);
            case UNAUTHORIZED:
                return createLambdaResponse(httpStatus, responseBody, LambdaErrorResponse.class);
            default:
                throw new IllegalArgumentException(String.format("Unsupported lambda's response status: \"%s\"", httpStatus));
        }
    }

    private AbstractLambdaResponse createLambdaResponse(final HttpStatus httpStatus, final String responseBody, final Class<? extends AbstractLambdaResponse> clazz) {
        final var response = JsonUtils.read(responseBody, clazz);
        response.setStatus(httpStatus);
        return response;
    }
}
