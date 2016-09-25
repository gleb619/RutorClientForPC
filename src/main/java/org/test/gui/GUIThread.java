package org.test.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;
import org.test.Main;
import org.test.model.Settings;
import org.test.service.config.UIConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BORIS on 18.09.2016.
 */
public class GUIThread extends Thread {

    private final List<UIConfig> windows = new ArrayList<>();
    private final Settings settings;
    private Display display;
    private Shell shell;

    public GUIThread(Settings settings) {
        this.settings = settings;
        setDaemon(true);
        setName(getName() + "::GuiThread");
    }

    @Override
    public void run() {
        display = new Display();
        shell = new Shell(getDisplay());
        shell.setText("Rutor client");

        ClassLoader classLoader = getClass().getClassLoader();
        Image image = new Image(display, classLoader.getResourceAsStream(settings.value(Settings.Codes.PROJECT_LAUNCHER_ICON_FILE)));
        Image image2 = new Image(display, classLoader.getResourceAsStream(settings.value(Settings.Codes.PROJECT_LAUNCHER_ICON_FILE_MD)));
        shell.setImage(image2);
        createTrayMenu(image);

        windows.forEach(uiConfig -> uiConfig.configure(this));

        shell.pack();
        shell.setSize(400, 600);
        shell.open();
//            shell.setBackground(new Color(display, new RGB(230, 0, 0)));

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
        Main.instance.setWorking(false);
    }

    public void createTrayMenu(Image image) {
        final Tray tray = display.getSystemTray();
        if (tray == null) {
            System.out.println("The system tray is not available");
        } else {
            final TrayItem item = new TrayItem(tray, SWT.NONE);
            item.setToolTipText("SWT TrayItem");
            item.addListener(SWT.Show, event -> System.out.println("show"));
            item.addListener(SWT.Hide, event -> System.out.println("hide"));
            item.addListener(SWT.Selection, event -> System.out.println("selection"));
            item.addListener(SWT.DefaultSelection, event -> System.out.println("default selection"));
            final Menu menu = new Menu(shell, SWT.POP_UP);
            for (int i = 0; i < 8; i++) {
                MenuItem mi = new MenuItem(menu, SWT.PUSH);
                mi.setText("Item" + i);
                mi.addListener(SWT.Selection, event -> System.out.println("selection " + event.widget));
                if (i == 0) menu.setDefaultItem(mi);
            }
            item.addListener(SWT.MenuDetect, event -> menu.setVisible(true));
            item.setImage(image);
//            item.setHighlightImage (image);
        }
    }

    public GUIThread addWindow(UIConfig widget) {
        windows.add(widget);
        return this;
    }

    public Display getDisplay() {
        return display;
    }

    public Shell getShell() {
        return shell;
    }
}
