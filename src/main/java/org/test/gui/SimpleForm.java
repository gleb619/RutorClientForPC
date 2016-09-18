package org.test.gui;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.test.listener.Listener;
import org.test.model.Settings;
import org.test.service.config.UIConfig;
import org.test.service.db.resource.SettingResource;
import org.test.service.subscription.Subscriber;
import org.test.util.GUIUtil;

import java.util.function.Predicate;


/**
 * Created by BORIS on 15.09.2016.
 */
@Slf4j
public class SimpleForm implements UIConfig, Listener<String> {

    private final Settings settings;
    private final SettingResource settingResource;
    private GUIThread guiThread;

    private Text address;
    private Text logs;
    private Button registerClientUID;

    public SimpleForm(Settings settings, SettingResource settingResource, Subscriber subscriber) {
        this.settings = settings;
        this.settingResource = settingResource;
        subscriber.registerListener(this);
    }

    @Override
    public void configure(GUIThread guiThread) {
        configureUI(guiThread);
        configureData();
    }

    private void configureUI(GUIThread guiThread) {
        this.guiThread = guiThread;
        this.address = new Text(guiThread.getShell(), SWT.BORDER | SWT.WRAP);
        this.registerClientUID = new Button(guiThread.getShell(), SWT.PUSH);
        this.logs = new Text(guiThread.getShell(), SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        this.logs.setEditable(false);

        this.registerClientUID.setText("Save");
        this.registerClientUID.addListener(SWT.Selection, e -> {
            switch (e.type) {
                case SWT.Selection:
                    settingResource.update(Settings.Codes.PROJECT_CLIENT_UID.name(), this.address.getText());
                    break;
            }
        });

        guiThread.getShell().setLayout(new GridLayout(3, false));
        guiThread.getShell().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.horizontalSpan = 3;

        GridData gridData2 = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        gridData.horizontalSpan = 2;

        this.address.setLayoutData(gridData2);
        this.logs.setLayoutData(gridData);
    }

    private void configureData() {
        if (Settings.Values.UNDEFINED.notSame(settings.value(Settings.Codes.PROJECT_CLIENT_UID))) {
            this.address.setText(settings.value(Settings.Codes.PROJECT_CLIENT_UID));
        }
    }

    @Override
    public Listener<String> onCall(String message) {
        GUIUtil.doUpdate(guiThread.getDisplay(), logs, message);
        return this;
    }

    @Override
    public Predicate<String> supported() {
        return s -> settings.value(Settings.Codes.ROUTE_NEWS).equals(s) || settings.value(Settings.Codes.ROUTE_COMMAND).equals(s);
    }
}
