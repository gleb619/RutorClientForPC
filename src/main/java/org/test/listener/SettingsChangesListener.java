package org.test.listener;

import org.test.model.Settings;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created by BORIS on 18.09.2016.
 */
public class SettingsChangesListener implements Listener<String> {

    private final String code;
    private final Listener<String> listener;

    public SettingsChangesListener(Settings.Codes code, Listener<String> listener) {
        this.code = code.name();
        this.listener = Objects.requireNonNull(listener);
    }

    @Override
    public Listener<String> onCall(String message) {
        return listener.onCall(message);
    }

    @Override
    public Predicate<String> supported() {
        return s -> code.equals(s);
    }

}
