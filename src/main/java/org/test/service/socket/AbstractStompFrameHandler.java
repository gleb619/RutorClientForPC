package org.test.service.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

/**
 * Created by BORIS on 16.09.2016.
 */
@Slf4j
public abstract class AbstractStompFrameHandler implements StompFrameHandler {

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        return byte[].class;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object body) {
        try {
            handleFrame(stompHeaders, new String((byte[]) body, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public abstract void handleFrame(StompHeaders stompHeaders, String body);

}
