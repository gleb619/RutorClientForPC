package org.test.service.http;

/**
 * Created by BORIS on 19.09.2016.
 */
public interface JsonConverter {

    <T> T read(String text, Class<T> clazz) throws Exception;

    String write(Object object) throws Exception;

}
