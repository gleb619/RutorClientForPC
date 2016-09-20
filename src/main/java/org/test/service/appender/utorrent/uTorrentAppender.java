package org.test.service.appender.utorrent;

import com.utorrent.webapiwrapper.core.entities.RequestResult;
import lombok.extern.slf4j.Slf4j;
import org.test.model.DownloadTopicCommand;
import org.test.service.appender.Appender;

/**
 * Created by BORIS on 16.09.2016.
 */
@Slf4j
public class uTorrentAppender implements Appender<DownloadTopicCommand> {

    private final uTorrentConfigurer uTorrentConfigurer;

    public uTorrentAppender(uTorrentConfigurer uTorrentConfigurer) {
        this.uTorrentConfigurer = uTorrentConfigurer;
    }

    @Override
    public void onCommandReceived(DownloadTopicCommand data) {
        try {
            RequestResult requestResult = uTorrentConfigurer.getData().addTorrent(data.getPayload());
            log.info(String.format("Tried to add topic, result is %s", requestResult));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
