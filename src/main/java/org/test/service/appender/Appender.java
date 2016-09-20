package org.test.service.appender;

/**
 * Created by BORIS on 17.09.2016.
 */
public interface Appender<T> {
    void onCommandReceived(T data);
}
