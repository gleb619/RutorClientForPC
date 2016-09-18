package org.test.service.db.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

import static org.test.service.db.entity.Setting.TABLE_NAME;

/**
 * Created by BORIS on 17.09.2016.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
//TODO: Create pull request, tables doesn't created without name
//TODO: Create pull request, primary key must have an alternative name
//TODO: Repair StandardSqlMaker.populateGeneratedKey
@Table(name = TABLE_NAME)
public class Setting implements Entity {

    public static final String TABLE_NAME = "setting";
    public static final String PRIMARY_KEY = "code";

    @Id
    private String code;
    private String value;


}
