package org.test.service.db;

import com.dieselpoint.norm.CustomDatabase;
import com.dieselpoint.norm.Database;
import com.dieselpoint.norm.DbException;
import lombok.extern.slf4j.Slf4j;
import org.test.model.Settings;
import org.test.service.config.ConfigWithHandler;
import org.test.util.DBUtil;

import java.sql.SQLException;

/**
 * Created by BORIS on 17.09.2016.
 */
@Slf4j
public class DatabaseConfigurer implements ConfigWithHandler<Database> {

    private final Settings settings;
    private final EntityConfigurer entityConfigurer;
    private final Database db;

    public DatabaseConfigurer(Settings settings, EntityConfigurer entityConfigurer) {
        this.settings = settings;
        this.entityConfigurer = entityConfigurer;
        this.db = new CustomDatabase();
    }

    @Override
    //TODO: ovverride security policy, create pair which based on system sn. Refer to this:
    // http://stackoverflow.com/questions/1986732/how-to-get-a-unique-computer-identifier-in-java-like-disk-id-or-motherboard-id
    // https://github.com/sarxos/secure-tokens/blob/master/src/main/java/com/github/sarxos/securetoken/impl/Hardware4Nix.java
    public void configure() throws Exception {
        System.setProperty("norm.serverName", "localhost");
        System.setProperty("norm.databaseName", String.format("%s\\%s",
                settings.value(Settings.Codes.PROJECT_WORK_DIR),
                settings.value(Settings.Codes.DB_NAME)
        ));
        System.setProperty("norm.dataSourceClassName", "org.apache.derby.jdbc.EmbeddedDataSource");
        System.setProperty("norm.user", settings.value(Settings.Codes.DB_USERNAME));
        System.setProperty("norm.password", settings.value(Settings.Codes.DB_PASSWORD));
        System.setProperty("norm.createDatabase", "create");

        entityConfigurer.getData()
                .forEach(clazz -> {
                    try {
                        db.createTable(clazz);
                    } catch (DbException e) {
                        if (e.getCause() instanceof SQLException) {
                            if (DBUtil.tableNotExists((SQLException) e.getCause())) {
                                throw e;
                            }
                        } else {
                            throw e;
                        }
                    }
                });
    }

    @Override
    public Database getData() {
        return db;
    }

}
