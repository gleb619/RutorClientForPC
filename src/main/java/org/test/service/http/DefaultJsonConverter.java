package org.test.service.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;

/**
 * Created by BORIS on 19.09.2016.
 */
public class DefaultJsonConverter implements JsonConverter {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
    private final ObjectMapper objectMapper;

    public DefaultJsonConverter() {
        this.objectMapper = new ObjectMapper();

        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        this.objectMapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.objectMapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT));
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
    }

    @Override
    public <T> T read(String text, Class<T> clazz) throws Exception {
        return objectMapper.readValue(text, clazz);
    }

    @Override
    public String write(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

}
