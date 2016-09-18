package org.test.service;

import org.apache.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
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

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by BORIS on 12.09.2016.
 */
@Deprecated
public class WebClientImpl {

    private static final Logger logger = Logger.getLogger(WebClientImpl.class);

    //    private static final String wsUrl = "localhost";
//    private static final int port = 8080;
    private static final String url = "ws://{host}:{port}/{endpoint}";
    private final Settings settings;
    private StompSession stompSession;

    public WebClientImpl(Settings settings) {
        this.settings = settings;
    }

    public void subscribeGreetings(StompSession stompSession) throws ExecutionException, InterruptedException {
        stompSession.subscribe("/topic/greetings", new StompFrameHandler() {

            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }

            public void handleFrame(StompHeaders stompHeaders, Object body) {
                logger.info("Received greeting " + new String((byte[]) body));
//                if(Objects.nonNull(lisntener)) lisntener.onCall(new String((byte[]) body));
            }
        });
    }

    public void sendHello(StompSession stompSession) {
        String jsonHello = "{ \"name\" : \"Nick\" }";
        stompSession.send("/app/hello", jsonHello.getBytes());
    }

    public void configure() throws Exception {
        Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);
        SockJsClient sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

        stompSession = stompClient.connect(url, headers, new MyHandler(),
                settings.value(Settings.Codes.SERVER_URL),
                settings.value(Settings.Codes.SERVER_PORT),
                settings.value(Settings.Codes.SERVER_ENDPOINT)).get();
    }

    public StompSession getStompSession() {
        return stompSession;
    }

    private class MyHandler extends StompSessionHandlerAdapter {
        public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
            logger.info("Now connected");
        }
    }

    /*
    public static void main(String[] args) throws Exception {
        HelloClient helloClient = new HelloClient();

        ListenableFuture<StompSession> f = helloClient.connect();
        StompSession stompSession = f.get();

        logger.info("Subscribing to greeting topic using session " + stompSession);
        helloClient.subscribeGreetings(stompSession);

        logger.info("Sending hello message" + stompSession);
        helloClient.sendHello(stompSession);
        Thread.sleep(60000);
        stompSession.disconnect();
    }
    */
}
