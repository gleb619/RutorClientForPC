package org.test.listener;

import com.utorrent.webapiwrapper.core.UTorrentAuthException;
import lombok.extern.slf4j.Slf4j;
import org.test.model.DownloadTopicCommand;
import org.test.model.Settings;
import org.test.service.appender.Appender;
import org.test.service.http.JsonConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created by BORIS on 18.09.2016.
 */
@Slf4j
public class ClientAppenderListener implements Listener<String> {

    private final List<Appender<DownloadTopicCommand>> appenders;
    private final Settings settings;
    private final JsonConverter jsonConverter;

    public ClientAppenderListener(JsonConverter jsonConverter, Settings settings, Appender<DownloadTopicCommand>... appenders) {
        this.jsonConverter = jsonConverter;
        this.settings = settings;
        this.appenders = new ArrayList<>(Arrays.asList(appenders));
    }

    @Override
    public Listener<String> onCall(String message) {
        DownloadTopicCommand command = null;
        try {
            command = jsonConverter.read(message, DownloadTopicCommand.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Objects.isNull(command)) {
            return this;
        }
        try {
            evaluateCommand(command);
        } catch (UTorrentAuthException e) {
            log.error("Can't connect to uTorrent webUi, info: " +
                    "ip: " + settings.value(Settings.Codes.CLIENT_IP) +
                    ", port: " + settings.value(Settings.Codes.CLIENT_PORT) +
                    ", login: " + settings.value(Settings.Codes.CLIENT_LOGIN) +
                    ", pass: " + (settings.value(Settings.Codes.CLIENT_PASS).hashCode() - 1) +
                    ". Trying one more time.");
            evaluateCommand(command);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    private void evaluateCommand(DownloadTopicCommand finalCommand) {
        appenders.forEach(appender -> appender.onCommandReceived(finalCommand));
    }

    @Override
    public Predicate<String> supported() {
        return s -> settings.value(Settings.Codes.ROUTE_COMMAND).equals(s);
    }
}
