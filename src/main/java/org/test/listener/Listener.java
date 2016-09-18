package org.test.listener;

import java.util.function.Predicate;

/**
 * Created by BORIS on 17.09.2016.
 */
public interface Listener<T> {

    Listener<T> onCall(T message);

    default Predicate<T> supported() {
        return s -> Boolean.TRUE;
    }

}
