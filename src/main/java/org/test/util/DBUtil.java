package org.test.util;

import com.dieselpoint.norm.Database;
import com.dieselpoint.norm.Transaction;

import java.sql.SQLException;

/**
 * Created by BORIS on 17.09.2016.
 */
public class DBUtil {

    public static boolean tableNotExists(SQLException e) {
        return !tableAlreadyExists(e);
    }

    public static boolean tableAlreadyExists(SQLException e) {
        boolean exists;
        exists = e.getSQLState().equals("X0Y32");
        return exists;
    }

    public static <T> T executeInTransaction(Database database, Callback<T> callback) {
        Transaction trans = database.startTransaction();
        T output = null;

        try {
            output = callback.onCall(trans);
            trans.commit();
            return output;
        } catch (Throwable e) {
            e.printStackTrace();
            trans.rollback();
        }

        return output;
    }

    public interface Callback<T> {
        T onCall(Transaction transaction);
    }

}
