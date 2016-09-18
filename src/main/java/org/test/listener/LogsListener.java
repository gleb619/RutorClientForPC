package org.test.listener;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by BORIS on 18.09.2016.
 */
@Slf4j
public class LogsListener implements Listener<String> {

    @Override
    public Listener<String> onCall(String message) {
        log.info(String.format("Receive message from server, message is %s", message));
        return this;
    }

}
