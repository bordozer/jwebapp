package com.bordozer.jwebapp;

import com.bordozer.jwebapp.converter.LambdaResponseConverter;
import com.bordozer.jwebapp.exception.LambdaInvokeException;
import com.bordozer.jwebapp.model.LambdaResponse;
import lombok.RequiredArgsConstructor;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

import static com.google.common.collect.Lists.newArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class LambdaWrapper {

    private static final int CONNECTION_TIMEOUT_MS = 20000;

    @Value("${aws.lambda.schema}")
    private String lambdaSchema;
    @Value("${aws.lambda.host}")
    private String lambdaHost;
    @Value("${aws.lambda.port}")
    private Integer lambdaPort;
    @Value("${aws.lambda.path}")
    private String lambdaPath;

    @SneakyThrows
    public LambdaResponse get() {
        final URIBuilder builder = new URIBuilder();
        builder.setScheme(lambdaSchema)
                .setHost(lambdaHost)
                .setPort(lambdaPort)
                .setPath(lambdaPath)
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
                log.info("Lambda response: code=\"{}\", body=\"{}\"", responseCode, responseBody);
                return LambdaResponseConverter.convert(responseCode, responseBody);
            }
        } catch (final IOException ex) {
            log.error("Error calling lambda", ex);
            throw new LambdaInvokeException(ex.getMessage());
        }
    }
}
