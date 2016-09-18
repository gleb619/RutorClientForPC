package org.test.service.db;

import lombok.extern.slf4j.Slf4j;
import org.test.service.config.ConfigWithHandler;
import org.test.service.db.entity.Entity;
import org.test.service.db.entity.Setting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BORIS on 17.09.2016.
 */
@Slf4j
public class EntityConfigurer implements ConfigWithHandler<List<Class<? extends Entity>>> {

    private final List<Class<? extends Entity>> entities;

    public EntityConfigurer() {
        this.entities = new ArrayList<>();
    }

    @Override
    public void configure() throws Exception {
        this.entities.add(Setting.class);
    }

    @Override
    public List<Class<? extends Entity>> getData() {
        return entities;
    }

}
