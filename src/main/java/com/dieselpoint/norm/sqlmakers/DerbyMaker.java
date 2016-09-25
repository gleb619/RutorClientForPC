package com.dieselpoint.norm.sqlmakers;

import com.dieselpoint.norm.DbException;
import com.dieselpoint.norm.Util;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by BORIS on 18.09.2016.
 */
public class DerbyMaker extends StandardSqlMaker {

    protected static final ConcurrentHashMap<Class, StandardPojoInfo> map = new ConcurrentHashMap<Class, StandardPojoInfo>();

    @Override
    public StandardPojoInfo getPojoInfo(Class rowClass) {
        StandardPojoInfo pi = map.get(rowClass);
        if (pi == null) {
            pi = new DerbyStandardPojoInfo(rowClass);
            map.putIfAbsent(rowClass, pi);

            makeInsertSql(pi);
            makeUpsertSql(pi);
            makeUpdateSql(pi);
            makeSelectColumns(pi);
        }
        return pi;
    }

    protected void makeSelectColumns(StandardPojoInfo pojoInfo) {
        if (pojoInfo.propertyMap.isEmpty()) {
            // this applies if the rowClass is a Map
            pojoInfo.selectColumns = "*";
        } else {
            ArrayList<String> cols = new ArrayList<String>();
            for (Property prop : pojoInfo.propertyMap.values()) {
                cols.add(prop.name);
            }
            pojoInfo.selectColumns = Util.join(cols);
        }
    }

    @Override
    public void populateGeneratedKey(ResultSet generatedKeys, Object insertRow) {
        PojoInfo pojoInfo = getPojoInfo(insertRow.getClass());

        try {
            Property prop = pojoInfo.getGeneratedColumnProperty();
            if (Objects.isNull(prop)) {
                return;
            }

            boolean isInt = prop.dataType.isAssignableFrom(int.class); // int or long

            Object newKey;

            // if there is just one column, it's the generated key
            // postgres returns multiple columns, though, so we have the fetch the value by name
            int colCount = generatedKeys.getMetaData().getColumnCount();
            if (colCount == 1) {
                if (isInt) {
                    newKey = generatedKeys.getInt(1);
                } else {
                    newKey = generatedKeys.getLong(1);
                }
            } else {
                // colcount > 1, must do by name
                if (isInt) {
                    newKey = generatedKeys.getInt(prop.name);
                } else {
                    newKey = generatedKeys.getLong(prop.name);
                }
            }

            pojoInfo.putValue(insertRow, prop.name, newKey);

        } catch (Throwable t) {
            throw new DbException(t);
        }
    }

}
