package org.test;

import org.test.config.AbstractModule;
import org.test.config.AppModule;
import org.test.gui.GUIThread;
import org.test.gui.SimpleForm;

/**
 * Created by BORIS on 12.09.2016.
 */
public class Main implements Runnable {

    private static final Main instance = new Main();
    private final AbstractModule objectGraph = new AppModule(new GUIThread());

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
            thread.setName(thread.getName() + "::configuration");
            thread.start();
            thread.join();

            objectGraph.provideUITread()
                    .addWindow(new SimpleForm(objectGraph.provideDefaultSubscriber()))
                    .start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            objectGraph.provideCloseListener().onCall(null);
        }
    }

}
