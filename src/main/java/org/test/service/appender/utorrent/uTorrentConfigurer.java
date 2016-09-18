package org.test.service.appender.utorrent;

import com.utorrent.webapiwrapper.core.UTorrentWebAPIClient;
import com.utorrent.webapiwrapper.restclient.ConnectionParams;
import lombok.extern.slf4j.Slf4j;
import org.test.model.Settings;
import org.test.service.config.ConfigWithHandler;

/**
 * Created by BORIS on 16.09.2016.
 */
@Slf4j
public class uTorrentConfigurer implements ConfigWithHandler<UTorrentWebAPIClient> {

    private final Settings setting;
    private UTorrentWebAPIClient client;

    public uTorrentConfigurer(Settings setting) {
        this.setting = setting;
    }

    @Override
    public void configure() {
        ConnectionParams connectionParams = ConnectionParams.builder()
                .enableAuthentication(true)
                .withCredentials(
                        setting.value(Settings.Codes.CLIENT_LOGIN),
                        setting.value(Settings.Codes.CLIENT_PASS))
                .withAddress(
                        setting.value(Settings.Codes.CLIENT_IP),
                        setting.value(Settings.Codes.CLIENT_PORT, Integer.class))
                .withTimeout(1500)
                .create();
        client = UTorrentWebAPIClient.getClient(connectionParams);
    }

    @Override
    public UTorrentWebAPIClient getData() {
        return client;
    }
}
