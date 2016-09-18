package org.test.service.db.resource;

import com.dieselpoint.norm.Database;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by BORIS on 17.09.2016.
 */
@Slf4j
public class SettingsResource {

    //    https://github.com/dieselpoint/norm#sample-code
    private final Database database;

    public SettingsResource(Database database) {
        this.database = database;
    }
}
