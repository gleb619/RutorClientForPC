package org.test.service.db.resource;

import lombok.extern.slf4j.Slf4j;
import org.test.model.Settings;
import org.test.service.config.Config;
import org.test.service.db.DatabaseConfigurer;
import org.test.service.db.entity.Setting;
import org.test.util.DBUtil;

import java.util.List;

/**
 * Created by BORIS on 17.09.2016.
 */
@Slf4j
public class SettingResource implements Config {

    //    https://github.com/dieselpoint/norm#sample-code
    private final Settings settings;
    private final DatabaseConfigurer databaseHolder;

    public SettingResource(Settings settings, DatabaseConfigurer databaseConfigurer) {
        this.settings = settings;
        this.databaseHolder = databaseConfigurer;
    }

    public List<Setting> loadAll() {
        return databaseHolder.getData().results(Setting.class);
    }

    public void update(String code, String text) {
        DBUtil.executeInTransaction(databaseHolder.getData(), t -> {
            Setting setting = new Setting(code, text);
            databaseHolder.getData().transaction(t)
                    .table(Setting.TABLE_NAME)
                    .where(Setting.PRIMARY_KEY + "=?", code)
                    .delete();
            databaseHolder.getData().transaction(t)
                    .insert(setting);

            return setting;
        });

        settings.populate(code, text);
    }

    @Override
    public void configure() throws Exception {
        loadAll().forEach(setting -> settings.populate(setting.getCode(), setting.getValue()));
    }
}
