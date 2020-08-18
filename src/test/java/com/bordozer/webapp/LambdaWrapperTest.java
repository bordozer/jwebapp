package com.bordozer.webapp;

import com.bordozer.webapp.utils.CommonUtils;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.bordozer.webapp.LambdaWrapper.LAMBDA_PATH;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class LambdaWrapperTest {

    private static final String LAMBDA_SUCCESS_RESPONSE = CommonUtils.readResource("lambda-success-response.json");
    private static final String LAMBDA_ERROR_RESPONSE = CommonUtils.readResource("lambda-error-response.json");

    @Autowired
    private LambdaWrapper lambdaWrapper;

    private WireMockServer wm;

    @Value("${aws.lambda.port}")
    private Integer port;

    @BeforeEach
    void testUp() {
        wm = new WireMockServer(WireMockConfiguration.options().port(port));
        wm.start();
    }

    @AfterEach
    void testTearDown() {
        wm.stop();
    }

    @Test
    @SneakyThrows
    void shouldReturnSuccessResponse() {
        // given
        wm.stubFor(WireMock.get(WireMock.urlPathEqualTo(LAMBDA_PATH))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(LAMBDA_SUCCESS_RESPONSE)
                ));
        // when
        final var response = lambdaWrapper.invoke();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);
        assertThat(response.getValue()).isEqualTo("lambda value");
    }

    @Test
    @SneakyThrows
    void shouldReturnErrorResponse() {
        // given
        wm.stubFor(WireMock.get(WireMock.urlPathEqualTo(LAMBDA_PATH))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.UNAUTHORIZED.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(LAMBDA_ERROR_RESPONSE)
                ));
        // when
        final var response = lambdaWrapper.invoke();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getValue()).isEqualTo("Unauthorized");
    }
}
