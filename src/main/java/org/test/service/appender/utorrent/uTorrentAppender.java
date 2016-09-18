package org.test.service.appender.utorrent;

import com.utorrent.webapiwrapper.core.entities.RequestResult;
import lombok.extern.slf4j.Slf4j;
import org.test.model.Settings;
import org.test.service.appender.Appender;

import java.io.IOException;

/**
 * Created by BORIS on 16.09.2016.
 */
@Slf4j
public class uTorrentAppender implements Appender {

    private final Settings setting;
    private final uTorrentConfigurer uTorrentConfigurer;

    public uTorrentAppender(Settings setting, uTorrentConfigurer uTorrentConfigurer) {
        this.setting = setting;
        this.uTorrentConfigurer = uTorrentConfigurer;
    }

    @Override
    public void onCommandReceived(String url) {
        if (1 == 1) {
            return;
        }
        try {
            RequestResult requestResult = uTorrentConfigurer.getData().addTorrent(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
