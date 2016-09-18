package org.test.service.subscription;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.test.listener.Listener;
import org.test.listener.SettingsChangesListener;
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
        if (Settings.Values.UNDEFINED.same(settings.value(Settings.Codes.PROJECT_CLIENT_UID))) {
            settings.registerOneTimeChangesListner(
                    new SettingsChangesListener(Settings.Codes.PROJECT_CLIENT_UID, value -> registerCommandsSubscriber()));
            log.info("Can't find record about client uid");
        } else {
            registerCommandsSubscriber();
        }

        registerNewsSubscriber();
    }

    private Listener registerNewsSubscriber() {
        webSocketConfigurer.getData().subscribe(settings.value(Settings.Codes.ROUTE_NEWS), new AbstractStompFrameHandler() {

            @Override
            public void handleFrame(StompHeaders stompHeaders, String body) {
                listeners.stream()
                        .filter(listener -> listener.supported().test(settings.value(Settings.Codes.ROUTE_NEWS)))
                        .forEach(stringListener -> stringListener.onCall(body));
            }

        });
        log.info(String.format("Successfully subscribed to %s", settings.value(Settings.Codes.ROUTE_NEWS)));

        return null;
    }

    private Listener registerCommandsSubscriber() {
        String path = String.format("%s%s",
                settings.value(Settings.Codes.ROUTE_COMMAND),
                settings.value(Settings.Codes.PROJECT_CLIENT_UID));

        webSocketConfigurer.getData().subscribe(path, new AbstractStompFrameHandler() {

            @Override
            public void handleFrame(StompHeaders stompHeaders, String body) {
                listeners.stream()
                        .filter(listener -> listener.supported().test(settings.value(Settings.Codes.ROUTE_COMMAND)))
                        .forEach(stringListener -> stringListener.onCall(body));
            }

        });
        log.info(String.format("Successfully subscribed to %s", path));

        return null;
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
