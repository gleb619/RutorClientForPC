package org.test.service.appender.utorrent;

import com.utorrent.webapiwrapper.core.entities.RequestResult;
import lombok.extern.slf4j.Slf4j;
import org.test.model.DownloadTopicCommand;
import org.test.service.appender.Appender;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by BORIS on 16.09.2016.
 */
@Slf4j
public class UTorrentAppender implements Appender<DownloadTopicCommand> {

    private final UTorrentConfigurer uTorrentConfigurer;

    public UTorrentAppender(UTorrentConfigurer uTorrentConfigurer) {
        this.uTorrentConfigurer = uTorrentConfigurer;
    }

    @Override
    public void onCommandReceived(DownloadTopicCommand data) {
        try {
//            File torrent = downloadFile(data.getPayload());
//            RequestResult requestResult = uTorrentConfigurer.getData().addTorrent(torrent);

            RequestResult requestResult = uTorrentConfigurer.getData().addTorrent(data.getPayload());
//            System.out.println("UTorrentAppender.onCommandReceived#requestResult: " + requestResult);

            log.info(String.format("Tried to add topic, result is %s", requestResult));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private File downloadFile(String url) {
        try {
            URL website = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            String name = MD5(url) + ".torrent";
            FileOutputStream fos = new FileOutputStream(name);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            return new File(name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String MD5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(text.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {

        }

        return null;
    }

}
