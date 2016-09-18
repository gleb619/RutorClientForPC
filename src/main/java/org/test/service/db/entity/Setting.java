package org.test.service.db.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by BORIS on 17.09.2016.
 */
@Data
//TODO: Create pull request, tables doesn't created without name
@Table(name = "setting")
public class Setting implements Entity {

    @Id
    //TODO: Create pull request, primary key must have an alternative name
    private String argument;
    private String value;


}
