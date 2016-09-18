package org.test.listener;

import lombok.extern.slf4j.Slf4j;
import org.test.service.socket.WebSocketConfigurer;

import java.util.Objects;

/**
 * Created by BORIS on 15.09.2016.
 */
@Slf4j
public class CloseListener implements Listener<Void> {

    private final WebSocketConfigurer webSocketConfigurer;

    public CloseListener(WebSocketConfigurer webSocketConfigurer) {
        this.webSocketConfigurer = webSocketConfigurer;
    }

    @Override
    public void onCall(Void message) {
        if (Objects.nonNull(webSocketConfigurer) && Objects.nonNull(webSocketConfigurer.getData())) {
            webSocketConfigurer.getData().disconnect();
        }
    }
}
