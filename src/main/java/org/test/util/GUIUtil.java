package org.test.util;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

/**
 * Created by BORIS on 17.09.2016.
 */
public class GUIUtil {

    public static void doUpdate(final Display display, final Text target, final String value) {
        display.asyncExec(() -> {
            if (!target.isDisposed()) {
                target.append("\n" + value);
                target.getParent().layout();
            }
        });
    }

    public static void executeInUIThread(final Display display, final Runnable runnable) {
        display.asyncExec(runnable);
    }

}
