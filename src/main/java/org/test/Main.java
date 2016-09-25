package org.test;

import org.test.config.AbstractModule;
import org.test.config.AppModule;
import org.test.gui.SimpleForm;

import java.util.concurrent.TimeUnit;

/**
 * Created by BORIS on 12.09.2016.
 */
public class Main implements Runnable {

    public static final Main instance = new Main();
    private final AbstractModule objectGraph = new AppModule();
    private Boolean working = Boolean.TRUE;

    public static void main(String[] args) {
        try {
            instance.run();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /* ============== */

    @Override
    public void run() {
        try {
            Thread thread = new Thread(objectGraph.provideConfigurator());
            thread.setDaemon(true);
            thread.setName(thread.getName() + "::Configuration");
            thread.start();
            thread.join();

            objectGraph.provideUITread()
                    .addWindow(new SimpleForm(objectGraph.provideProjectSettings(), objectGraph.provideSettingResource(), objectGraph.provideDefaultSubscriber()))
                    .start();
            while (working) {
                Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //TODO: implement closable services, close db connection
            objectGraph.provideCloseListener().onCall(null);
        }
    }

    public Boolean getWorking() {
        return working;
    }

    public void setWorking(Boolean working) {
        this.working = working;
    }
}
