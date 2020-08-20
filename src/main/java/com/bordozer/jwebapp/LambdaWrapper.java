package com.bordozer.jwebapp;

import com.bordozer.jwebapp.converter.LambdaResponseConverter;
import com.bordozer.jwebapp.model.Lambda;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.google.common.collect.Lists.newArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class LambdaWrapper {

    private static final int CONNECTION_TIMEOUT_MS = 20000;

    @Value("${aws.jlambda.schema}")
    private String jlambdaSchema;
    @Value("${aws.jlambda.host}")
    private String jlambdaHost;
    @Value("${aws.jlambda.port}")
    private Integer jlambdaPort;
    @Value("${aws.jlambda.path}")
    private String jlambdaPath;

    @Value("${aws.olalambda.schema}")
    private String olalambdaSchema;
    @Value("${aws.olalambda.host}")
    private String olalambdaHost;
    @Value("${aws.olalambda.port}")
    private Integer olalambdaPort;
    @Value("${aws.olalambda.path}")
    private String olalambdaPath;

    @SneakyThrows
    public LambdaResponse get(final Lambda function) {
        final URI uri = getUri(function);
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
            return LambdaResponse.builder()
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .value(ex.getMessage())
                    .build();
        }
    }

    private URI getUri(final Lambda function) throws URISyntaxException {
        final URIBuilder builder = new URIBuilder();
        switch (function) {
            case JLAMBDA:
                builder.setScheme(jlambdaSchema)
                        .setHost(jlambdaHost)
                        .setPort(jlambdaPort)
                        .setPath(jlambdaPath)
                        .setParameters(newArrayList());
                break;
            case OLALAMBDA:
                builder.setScheme(olalambdaSchema)
                        .setHost(olalambdaHost)
                        .setPort(olalambdaPort)
                        .setPath(olalambdaPath)
                        .setParameters(newArrayList());
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown function parameter: \"%s\"", function));
        }
        return builder.build();
    }
}
