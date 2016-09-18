package org.test.config;

import org.test.gui.GUIThread;
import org.test.listener.ClientAppenderListener;
import org.test.listener.CloseListener;
import org.test.listener.LogsListener;
import org.test.model.Settings;
import org.test.service.appender.utorrent.uTorrentAppender;
import org.test.service.appender.utorrent.uTorrentConfigurer;
import org.test.service.config.Configurator;
import org.test.service.config.DefaultConfigurator;
import org.test.service.db.DatabaseConfigurer;
import org.test.service.db.EntityConfigurer;
import org.test.service.db.resource.SettingResource;
import org.test.service.socket.WebSocketConfigurer;
import org.test.service.subscription.DefaultSubscriber;

/**
 * Created by BORIS on 16.09.2016.
 */
public class AppModule extends AbstractModule {

    /* ======= PROJECT ======= */
    @Override
    public Settings provideProjectSettings() {
        return singleton(() -> new Settings());
    }

    @Override
    public Configurator provideConfigurator() {
        return singleton(() -> new DefaultConfigurator(
                provideEntityConfigurer(),
                provideDatabaseConfigurer(),
                provideWebSocketConfigurer(),
                provideUTorrentConfigurer(),
                provideDefaultSubscriber(),
                provideSettingResource()
        ));
    }

    public LogsListener provideLogsListner() {
        return new LogsListener();
    }

    @Override
    public CloseListener provideCloseListener() {
        return singleton(() -> new CloseListener(provideWebSocketConfigurer()));
    }

    /* ======= SOCKET ======= */
    public WebSocketConfigurer provideWebSocketConfigurer() {
        return singleton(() -> new WebSocketConfigurer(provideProjectSettings()));
    }

    @Override
    public DefaultSubscriber provideDefaultSubscriber() {
        return singleton(() -> new DefaultSubscriber(provideWebSocketConfigurer(), provideProjectSettings(),
                provideLogsListner(),
                provideClientAppenderListner()
        ));
    }

    /* ======= APPENDERS ======= */
    public ClientAppenderListener provideClientAppenderListner() {
        return singleton(() -> new ClientAppenderListener(
                provideProjectSettings(), provideUTorrentAppender()
        ));
    }

    public uTorrentConfigurer provideUTorrentConfigurer() {
        return singleton(() -> new uTorrentConfigurer(provideProjectSettings()));
    }

    public uTorrentAppender provideUTorrentAppender() {
        return singleton(() -> new uTorrentAppender(provideUTorrentConfigurer()));
    }

    /* ======= DB ======= */
    public EntityConfigurer provideEntityConfigurer() {
        return singleton(() -> new EntityConfigurer());
    }

    public DatabaseConfigurer provideDatabaseConfigurer() {
        return singleton(() -> new DatabaseConfigurer(provideProjectSettings(), provideEntityConfigurer()));
    }

    @Override
    public SettingResource provideSettingResource() {
        return singleton(() -> new SettingResource(provideProjectSettings(), provideDatabaseConfigurer()));
    }

    /* ======= UI ======= */
    @Override
    public GUIThread provideUITread() {
        return singleton(() -> new GUIThread(provideProjectSettings()));
    }

}
