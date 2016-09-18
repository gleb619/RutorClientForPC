package org.test.listener;

import org.test.model.Settings;
import org.test.service.appender.Appender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by BORIS on 18.09.2016.
 */
public class ClientAppenderListener implements Listener<String> {

    private final List<Appender> appenders;
    private final Settings settings;

    public ClientAppenderListener(Settings settings, Appender... appenders) {
        this.settings = settings;
        this.appenders = new ArrayList<>(Arrays.asList(appenders));
    }

    @Override
    public Listener<String> onCall(String message) {
        appenders.forEach(appender -> appender.onCommandReceived(message));
        return this;
    }

    @Override
    public Predicate<String> supported() {
        return s -> settings.value(Settings.Codes.ROUTE_COMMAND).equals(s);
    }
}
