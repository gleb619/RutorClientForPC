package org.test.model;

import org.test.listener.Listener;
import org.test.util.ParserUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by BORIS on 16.09.2016.
 */
public class Settings {

    private final Map<String, Object> data = new HashMap<>();
    private final List<Listener<String>> oneTimeListeners = new ArrayList<>();

    public Settings() {
//        this.data.put(Settings.Codes.SERVER_URL.name(), "localhost");
//        this.data.put(Settings.Codes.SERVER_PORT.name(), "8080");
//        this.data.put(Settings.Codes.SERVER_ENDPOINT.name(), "echo");

        this.data.put(Settings.Codes.DB_USERNAME.name(), "a21232f297");
        this.data.put(Settings.Codes.DB_PASSWORD.name(), "a5f4dcc3b5");
        this.data.put(Settings.Codes.DB_NAME.name(), "rc");

        this.data.put(Settings.Codes.CLIENT_LOGIN.name(), "admin");
        this.data.put(Settings.Codes.CLIENT_PASS.name(), "admin");
        this.data.put(Settings.Codes.CLIENT_IP.name(), "localhost");
        this.data.put(Settings.Codes.CLIENT_PORT.name(), 8090);

        this.data.put(Settings.Codes.ROUTE_COMMAND.name(), "/topic/");
        this.data.put(Settings.Codes.ROUTE_NEWS.name(), "/topic/greetings");

        this.data.put(Codes.PROJECT_WORK_DIR.name(), System.getProperty("user.dir"));
        this.data.put(Codes.PROJECT_LAUNCHER_ICON_FILE.name(), "launcher-icon.ico");

        loadProperties();
    }

    public void loadProperties() {
        Properties prop = new Properties();
        try (InputStream in = Settings.class.getClassLoader().getResourceAsStream("project.properties")) {
            prop.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        prop.forEach((key, value) -> populate(parseKey(key.toString()), value.toString()));
    }

    private String parseKey(String key) {
        return Arrays.asList(key.split("\\.")).stream()
                .map(s -> s.toUpperCase())
                .collect(Collectors.joining("_"));
    }

    public void populate(String code, String value) {
        this.data.put(code, value);
        oneTimeListeners.stream()
                .filter(listener -> listener.supported().test(code))
                .map(listener -> listener.onCall(value))
                .map(listener -> oneTimeListeners.remove(listener))
                .count();
    }

    public void registerOneTimeChangesListner(Listener<String> listener) {
        oneTimeListeners.add(listener);
    }

    public String value(Codes code) {
        return data.getOrDefault(code.name(), Values.UNDEFINED.value()).toString();
    }

    public <T> T value(Codes code, Class<T> clazz) {
        return (T) ParserUtil.parse(data.getOrDefault(code.name(), ""), clazz);
    }

    public enum Codes {

        PROJECT_WORK_DIR,
        PROJECT_LAUNCHER_ICON_FILE,
        PROJECT_CLIENT_UID,
        CLIENT_LOGIN,
        CLIENT_PASS,
        CLIENT_IP,
        CLIENT_PORT,
        SERVER_URL,
        SERVER_PORT,
        SERVER_ENDPOINT,
        ROUTE_COMMAND,
        ROUTE_NEWS,
        DB_USERNAME,
        DB_PASSWORD,
        DB_NAME

    }

    public enum Values {

        UNDEFINED;

        public String value() {
            return "$" + this.name();
        }

        public boolean notSame(Object o) {
            return !same(o);
        }

        public boolean same(Object o) {
            return this.value().equals(o);
        }
    }

}
