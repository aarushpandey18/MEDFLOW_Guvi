package com.medflow.service;

import com.medflow.model.Notifiable;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Background reminder thread (Multithreading).
 * Request threads put messages in a BlockingQueue (producer);
 * this thread takes them out and "sends" them (consumer).
 */
public class ReminderService implements Runnable {

    private record Job(Notifiable target, String message) {}

    private final BlockingQueue<Job> queue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;
    private Thread worker;

    public void start() {
        worker = new Thread(this, "reminder-thread");
        worker.setDaemon(true);
        worker.start();
    }

    public void stop() {
        running = false;
        if (worker != null) worker.interrupt();
    }

    public void schedule(Notifiable target, String message) {
        queue.offer(new Job(target, message));
    }

    @Override
    public void run() {
        while (running) {
            try {
                Job job = queue.take();                       // blocks until work arrives
                System.out.println("[Reminder] " + job.target().notifyUser(job.message()));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
