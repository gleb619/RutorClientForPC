package org.test.service.config;

import org.test.service.other.Handler;

/**
 * Created by BORIS on 18.09.2016.
 */
public interface ConfigWithHandler<T> extends Handler<T>, Config {
}
