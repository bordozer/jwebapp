package com.bordozer.webapp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

public final class JsonUtils {

    private static final ObjectWriter WRITER;
    private static final ObjectReader READER;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.findAndRegisterModules();
        WRITER = MAPPER.writer();
        READER = MAPPER.reader();
    }

    private JsonUtils() {
    }

    public static String write(final Object value) {
        try {
            return WRITER.writeValueAsString(value);
        } catch (final JsonProcessingException ex) {
            throw new IllegalArgumentException(String.format("Object '%s' cannot be serialize", value));
        }
    }

    public static String pretty(final Object value) {
        try {
            return WRITER.withDefaultPrettyPrinter().writeValueAsString(value);
        } catch (final JsonProcessingException ex) {
            throw new IllegalArgumentException(String.format("Object '%s' cannot be serialize", value));
        }
    }

    public static <T> T read(final String value, final Class<T> clazz) {
        try {
            return READER.forType(clazz).readValue(value);
        } catch (final IOException ex) {
            throw new IllegalArgumentException(String.format("String '%s' cannot be deserialize to class %s", value, clazz));
        }
    }
}
