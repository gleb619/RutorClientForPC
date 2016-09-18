package org.test.util;

import java.util.Objects;

/**
 * Created by BORIS on 17.09.2016.
 */
public class ParserUtil {

    public static Object parse(Object value, Class clazz) {
        if (Objects.isNull(value) || Objects.isNull(clazz)) {
            return value;
        }

        if (value.getClass().equals(clazz)) {
            return value;
        }

        if (value.getClass().isInstance(String.class) && clazz.equals(Integer.class)) {
            return parseIntFromString((String) value);
        } else if (value.getClass().isInstance(Integer.class) && clazz.equals(String.class)) {
            return parseStringFromInt((Integer) value);
        }

        return null;
    }

    private static Integer parseIntFromString(String message) {
        return Integer.parseInt(message);
    }

    private static String parseStringFromInt(Integer number) {
        return number.toString();
    }

}
