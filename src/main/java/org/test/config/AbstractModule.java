package org.test.config;

import com.vanillasource.jaywire.standalone.StandaloneModule;
import org.test.gui.GUIThread;
import org.test.listener.Listener;
import org.test.model.Settings;
import org.test.service.config.Configurator;
import org.test.service.db.resource.SettingResource;
import org.test.service.subscription.Subscriber;

/**
 * Created by BORIS on 17.09.2016.
 */
public abstract class AbstractModule extends StandaloneModule {

    /* ======= PROJECT ======= */
    public abstract Settings provideProjectSettings();

    public abstract Configurator provideConfigurator();

    public abstract Subscriber provideDefaultSubscriber();

    public abstract Listener<Void> provideCloseListener();

    public abstract SettingResource provideSettingResource();

    public abstract GUIThread provideUITread();
}
