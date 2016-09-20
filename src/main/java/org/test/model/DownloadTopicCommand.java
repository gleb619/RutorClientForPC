package org.test.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by BORIS on 19.09.2016.
 */
@Data
public class DownloadTopicCommand implements Serializable {

    private String payload;

}
