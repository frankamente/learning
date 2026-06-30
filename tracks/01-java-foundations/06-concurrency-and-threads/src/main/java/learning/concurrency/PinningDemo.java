package learning.concurrency;

import java.util.concurrent.locks.ReentrantLock;

public class PinningDemo {
    private final ReentrantLock lock = new ReentrantLock();

    public void executeSynchronizedTask() {
        // TODO: Run a synchronized block that sleeps.
        // If run within a Virtual Thread, this will cause Carrier Thread Pinning.
    }

    public void executeLockTask() {
        // TODO: Run a lock block using ReentrantLock that sleeps.
        // ReentrantLock allows Virtual Threads to unmount from the carrier thread, avoiding pinning.
    }
}
