package com.dieselpoint.norm;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.test.model.Settings;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by BORIS on 17.09.2016.
 */
public class CustomDatabase extends Database {

    private final Settings settings;

    public CustomDatabase(Settings settings) {
        this.settings = settings;
    }

    @Override
    protected DataSource getDataSource() throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(100);
        config.setDataSourceClassName(System.getProperty("norm.dataSourceClassName"));
//        config.addDataSourceProperty("serverName", System.getProperty("norm.serverName"));
        config.addDataSourceProperty("databaseName", System.getProperty("norm.databaseName"));
        config.addDataSourceProperty("user", System.getProperty("norm.user"));
        config.addDataSourceProperty("password", System.getProperty("norm.password"));
        config.addDataSourceProperty("createDatabase", System.getProperty("norm.createDatabase"));
//        config.setInitializationFailFast(true);


//        awDataSource.setDatabaseName("target/testDB");
//        rawDataSource.setUser("test");
//        rawDataSource.setCreateDatabase("create");

//        config.addDataSourceProperty("connectionTimeout", 5000);
//        config.setConnectionTimeout(5000);
//        config.setJdbcUrl(String.format("jdbc:derby:%s\\%s;create=true",
//                settings.value(Settings.Codes.PROJECT_WORK_DIR),
//                settings.value(Settings.Codes.DB_NAME)
//        ));
//        config.addDataSourceProperty("jdbcUrl", String.format("jdbc:derby:%s;%s",
//                settings.value(Settings.Codes.PROJECT_WORK_DIR),
//                settings.value(Settings.Codes.DB_NAME)
//        ));

        return new HikariDataSource(config);
    }
}
