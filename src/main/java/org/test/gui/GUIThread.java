package org.test.gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.test.service.config.UIConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BORIS on 18.09.2016.
 */
public class GUIThread extends Thread {

    private final List<UIConfig> windows = new ArrayList<>();
    private Display display;
    private Shell shell;

    @Override
    public void run() {
        display = new Display();
        shell = new Shell(getDisplay());
        shell.setText("Rutor client");

        windows.forEach(uiConfig -> uiConfig.configure(this));

        shell.pack();
        shell.setSize(400, 600);
        shell.open();
//            shell.setBackground(new Color(display, new RGB(230, 0, 0)));

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();

//        Shell shell = new Shell(display);
//        shell.setLayout(new GridLayout());
//        shell.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//        label = new Label(shell, SWT.NONE);
//        label.setText(" -- ");
//        shell.open();
//        shell.pack();
//
//        while (!shell.isDisposed()) {
//            if (!display.readAndDispatch()) display.sleep();
//        }
//        display.dispose();
    }

    public GUIThread addWindow(UIConfig widget) {
        windows.add(widget);
        return this;
    }

//    public synchronized void update(final int value) {
//        if (display == null || display.isDisposed())
//            return;
//        display.asyncExec(new Runnable() {
//
//            public void run() {
//                label.setText("" + value);
//            }
//        });
//
//    }

    public Display getDisplay() {
        return display;
    }

    public Shell getShell() {
        return shell;
    }
}
