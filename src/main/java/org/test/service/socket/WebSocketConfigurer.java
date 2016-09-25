package org.test.service.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;
import org.test.model.Settings;
import org.test.service.config.Config;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Created by BORIS on 17.09.2016.
 */
@Slf4j
public class WebSocketConfigurer implements Config {

    private static final String url = "ws://{host}:{port}/{endpoint}";

    private final Settings settings;
    private final StompSessionHandlerAdapter handlerAdapter;

    private StompSession stompSession;

    public WebSocketConfigurer(Settings settings, StompSessionHandlerAdapter handlerAdapter) {
        this.settings = settings;
        this.handlerAdapter = Objects.nonNull(handlerAdapter) ? handlerAdapter : new AbstractHandler();
    }

    public WebSocketConfigurer(Settings settings) {
        this(settings, null);
    }

    @Override
    public void configure() throws ExecutionException, InterruptedException {
        log.debug("Web socket config start");
        Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);
        SockJsClient sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

        log.info("Prepare to create stomp session with provided settings: " +
                String.format("url: %s, port: %s, endpoint: %s;",
                        settings.value(Settings.Codes.SERVER_URL),
                        settings.value(Settings.Codes.SERVER_PORT),
                        settings.value(Settings.Codes.SERVER_ENDPOINT))
        );

        stompSession = stompClient.connect(url, headers, handlerAdapter,
                settings.value(Settings.Codes.SERVER_URL),
                settings.value(Settings.Codes.SERVER_PORT),
                settings.value(Settings.Codes.SERVER_ENDPOINT)).get();
        log.debug("Web socket config succeeded, session: " + stompSession);
    }

    public StompSession getData() {
        return stompSession;
    }

    public static class AbstractHandler extends StompSessionHandlerAdapter {
        public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
            log.info(String.format("Successfully connected to remote server, sessionId: %s, stompHeaders: %s;", stompSession.getSessionId(), stompHeaders));
        }
    }

}
