package org.test.listener;

import java.util.function.Predicate;

/**
 * Created by BORIS on 17.09.2016.
 */
public interface Listener<T> {

    void onCall(T message);

    default Predicate<String> supported() {
        return s -> Boolean.TRUE;
    }

}
