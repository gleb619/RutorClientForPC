package org.test.service.subscription;

import org.test.listener.Listener;
import org.test.service.config.Config;

/**
 * Created by BORIS on 18.09.2016.
 */
public interface Subscriber extends Config {
    void registerListener(Listener<String> listener);

    void unRegisterListener(Listener<String> listener);
}
