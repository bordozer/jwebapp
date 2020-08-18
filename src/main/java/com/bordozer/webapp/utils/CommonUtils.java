package com.bordozer.webapp.utils;

import com.google.common.io.Resources;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonUtils {

    @SneakyThrows
    public static String readResource(final String name) {
        final URL url = Resources.getResource(name);
        return Resources.toString(url, StandardCharsets.UTF_8);
    }

    public static long getCurrentEpochTime() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
