package org.test.service.config;

/**
 * Created by BORIS on 18.09.2016.
 */
public interface Configurator extends Runnable {

    void registerConfig(Config listener);

    void unRegisterConfig(Config listener);

}
