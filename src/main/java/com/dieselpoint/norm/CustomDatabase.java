package com.dieselpoint.norm;

import com.dieselpoint.norm.sqlmakers.DerbyMaker;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by BORIS on 17.09.2016.
 */
public class CustomDatabase extends Database {

    public CustomDatabase() {
        setSqlMaker(new DerbyMaker());
    }

    @Override
    protected DataSource getDataSource() throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(100);

        config.setDataSourceClassName(System.getProperty("norm.dataSourceClassName"));
        config.addDataSourceProperty("databaseName", System.getProperty("norm.databaseName"));
        config.addDataSourceProperty("user", System.getProperty("norm.user"));
        config.addDataSourceProperty("password", System.getProperty("norm.password"));
        config.addDataSourceProperty("createDatabase", System.getProperty("norm.createDatabase"));

        System.clearProperty("norm.databaseName");
        System.clearProperty("norm.user");
        System.clearProperty("norm.password");

        return new HikariDataSource(config);
    }

}
