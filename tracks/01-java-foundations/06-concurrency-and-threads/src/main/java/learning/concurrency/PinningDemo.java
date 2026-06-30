package learning.concurrency;

import java.util.concurrent.locks.ReentrantLock;

public class PinningDemo {
    private final ReentrantLock lock = new ReentrantLock();

    public void executeSynchronizedTask() {
        synchronized (new Object()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void executeLockTask() {
        var localLock = new ReentrantLock();
        localLock.lock();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            localLock.unlock();
        }
    }
}
