package org.test.service.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by BORIS on 17.09.2016.
 */
public class DefaultConfigurator implements Configurator {

    private final List<Config> configurers;

    public DefaultConfigurator(Config... configs) {
        this.configurers = new ArrayList<>(Arrays.asList(configs));
    }

    @Override
    public void run() {
        configurers.forEach(onConfig());
    }

    private Consumer<Config> onConfig() {
        return configuration -> {
            try {
                configuration.configure();
            } catch (Exception e) {
                //TODO: implement silentMode
                e.printStackTrace();
            }
        };
    }

    @Override
    public void registerConfig(Config listener) {
        configurers.add(listener);
    }

    @Override
    public void unRegisterConfig(Config listener) {
        configurers.remove(listener);
    }
}
