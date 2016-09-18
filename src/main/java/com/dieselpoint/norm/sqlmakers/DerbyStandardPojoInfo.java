package com.dieselpoint.norm.sqlmakers;

import com.dieselpoint.norm.DbException;

import javax.persistence.EnumType;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by BORIS on 18.09.2016.
 */
public class DerbyStandardPojoInfo extends StandardPojoInfo {

    public DerbyStandardPojoInfo(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public void putValue(Object pojo, String name, Object value) {
        Property prop = propertyMap.get(name);
        if (prop == null) {
            prop = propertyMap.get(name.toLowerCase());
        }
        if (prop == null) {
            throw new DbException("No such field: " + name);
        }

        if (value != null) {
            if (prop.serializer != null) {
                value = prop.serializer.deserialize((String) value, prop.dataType);

            } else if (prop.isEnumField) {
                value = getEnumConst(prop.enumClass, prop.enumType, value);
            }
        }

        if (prop.writeMethod != null) {
            try {
                prop.writeMethod.invoke(pojo, value);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new DbException(
                        String.format("Could not write value into pojo. Property: %s, method: %s, value: %s;",
                                prop.name,
                                prop.writeMethod.toString(),
                                value), e);
            }
            return;
        }

        if (prop.field != null) {
            try {
                prop.field.set(pojo, value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new DbException("Could not set value into pojo. Field: " + prop.field.toString() + " value: " + value, e);
            }
            return;
        }
    }

    protected <T extends Enum<T>> Object getEnumConst(Class<T> enumType, EnumType type, Object value) {
        String str = value.toString();
        if (type == EnumType.ORDINAL) {
            Integer ordinalValue = (Integer) value;
            if (ordinalValue < 0 || ordinalValue >= enumType.getEnumConstants().length) {
                throw new DbException("Invalid ordinal number " + ordinalValue + " for enum class " + enumType.getCanonicalName());
            }
            return enumType.getEnumConstants()[ordinalValue];
        } else {
            for (T e : enumType.getEnumConstants()) {
                if (str.equals(e.toString())) {
                    return e;
                }
            }
            throw new DbException("Enum value does not exist. value:" + str);
        }
    }

}
