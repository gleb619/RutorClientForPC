package org.test.model;

import org.test.util.ParserUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by BORIS on 16.09.2016.
 */
public class Settings {

    private Map<String, Object> data = new HashMap<>();

    public Settings() {
        this.data.put(Settings.Codes.SERVER_URL.name(), "localhost");
        this.data.put(Settings.Codes.SERVER_PORT.name(), "8080");
        this.data.put(Settings.Codes.SERVER_ENDPOINT.name(), "echo");
        this.data.put(Settings.Codes.DB_USERNAME.name(), "a21232f297");
        this.data.put(Settings.Codes.DB_PASSWORD.name(), "a5f4dcc3b5");
        this.data.put(Settings.Codes.DB_NAME.name(), "rc");

        this.data.put(Settings.Codes.CLIENT_LOGIN.name(), "admin");
        this.data.put(Settings.Codes.CLIENT_PASS.name(), "admin");
        this.data.put(Settings.Codes.CLIENT_IP.name(), "localhost");
        this.data.put(Settings.Codes.CLIENT_PORT.name(), 8090);

        this.data.put(Settings.Codes.ROUTE_COMMAND.name(), "/topic/greetings");

        this.data.put(Codes.PROJECT_WORK_DIR.name(), System.getProperty("user.dir"));

    }

    public String value(Codes code) {
        return data.getOrDefault(code.name(), "").toString();
    }

    public <T> T value(Codes code, Class<T> clazz) {
        return (T) ParserUtil.parse(data.getOrDefault(code.name(), ""), clazz);
    }

    public enum Codes {

        PROJECT_WORK_DIR,
        CLIENT_LOGIN,
        CLIENT_PASS,
        CLIENT_IP,
        CLIENT_PORT,
        SERVER_URL,
        SERVER_PORT,
        SERVER_ENDPOINT,
        ROUTE_COMMAND,
        DB_USERNAME,
        DB_PASSWORD,
        DB_NAME,

    }

    public enum Values {

        UNDEFINED;

        public String value() {
            return "$" + this.name();
        }
    }

}
