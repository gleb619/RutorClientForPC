package org.test.gui;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.test.listener.Listener;
import org.test.service.config.UIConfig;
import org.test.service.subscription.Subscriber;
import org.test.util.GUIUtil;


/**
 * Created by BORIS on 15.09.2016.
 */
@Slf4j
public class SimpleForm implements UIConfig, Listener<String> {

    private GUIThread guiThread;

    private Text address;
    private Text logs;

    public SimpleForm(Subscriber subscriber) {
        subscriber.registerListener(this);
    }

    @Override
    public void configure(GUIThread guiThread) {
        this.guiThread = guiThread;
        this.address = new Text(guiThread.getShell(), SWT.BORDER | SWT.WRAP);
        this.logs = new Text(guiThread.getShell(), SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        this.logs.setEditable(false);

        guiThread.getShell().setLayout(new GridLayout(1, true));
        guiThread.getShell().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        GridData gridData = new GridData(GridData.FILL_BOTH);
        this.logs.setLayoutData(gridData);
    }

    @Override
    public void onCall(String message) {
        System.out.println("SimpleForm.onCall");
        GUIUtil.doUpdate(guiThread.getDisplay(), logs, message);
    }

}
