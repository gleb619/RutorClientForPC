package org.test.listener;

import org.test.service.appender.Appender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by BORIS on 18.09.2016.
 */
public class ClientAppenderListener implements Listener<String> {

    private final List<Appender> appenders;

    public ClientAppenderListener(Appender... appenders) {
        this.appenders = new ArrayList<>(Arrays.asList(appenders));
    }

    @Override
    public void onCall(String message) {
        appenders.forEach(appender -> appender.onCommandReceived(message));
    }

}
