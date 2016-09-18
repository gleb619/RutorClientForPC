package org.test.service.config;

import org.test.gui.GUIThread;

/**
 * Created by BORIS on 18.09.2016.
 */
public interface UIConfig {

    void configure(GUIThread guiThread) throws RuntimeException;

}
