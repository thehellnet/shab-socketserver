package org.thehellnet.shab.socketserver.thread;

/**
 * Created by sardylan on 14/07/16.
 */
public class StoppableThread {

    private Thread thread;
    private boolean keepRunning;

    public StoppableThread(Runnable runnable) {
        thread = new Thread(() -> {
            while (keepRunning) {
                runnable.run();
            }
        });
    }

    public void start() {
        keepRunning = true;
        thread.start();
    }

    public void stop() {
        keepRunning = false;
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
