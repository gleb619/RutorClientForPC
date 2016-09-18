package org.test.service.subscription;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.test.listener.Listener;
import org.test.model.Settings;
import org.test.service.socket.AbstractStompFrameHandler;
import org.test.service.socket.WebSocketConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by BORIS on 18.09.2016.
 */
@Slf4j
public class DefaultSubscriber implements Subscriber {

    private final List<Listener<String>> listeners;
    private final WebSocketConfigurer webSocketConfigurer;
    private final Settings settings;

    public DefaultSubscriber(WebSocketConfigurer webSocketConfigurer, Settings settings, Listener<String>... listeners) {
        this.webSocketConfigurer = webSocketConfigurer;
        this.settings = settings;
        this.listeners = new ArrayList<>(Arrays.asList(listeners));
    }

    @Override
    public void configure() throws ExecutionException, InterruptedException {
        webSocketConfigurer.getData().subscribe(settings.value(Settings.Codes.ROUTE_COMMAND), new AbstractStompFrameHandler() {

            @Override
            public void handleFrame(StompHeaders stompHeaders, String body) {
                listeners.parallelStream()
                        .filter(listener -> listener.supported().test(settings.value(Settings.Codes.ROUTE_COMMAND)))
                        .forEach(stringListener -> stringListener.onCall(body));
            }

        });
        log.info(String.format("Successfully subscribed to %s", settings.value(Settings.Codes.ROUTE_COMMAND)));
    }

    @Override
    public void registerListener(Listener<String> listener) {
        listeners.add(listener);
    }

    @Override
    public void unRegisterListener(Listener<String> listener) {
        listeners.remove(listener);
    }

}
