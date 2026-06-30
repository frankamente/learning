package learning.concurrency;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class ConcurrentTaskProcessor {

    public <T> List<T> executeWithPlatformPool(List<Callable<T>> tasks, int poolSize) throws Exception {
        try (var executor = Executors.newFixedThreadPool(poolSize)) {
            return executor.invokeAll(tasks).stream().map(future -> {
                try {
                    return future.get();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).toList();
        }
    }

    public <T> List<T> executeWithVirtualThreads(List<Callable<T>> tasks) throws Exception {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            return executor.invokeAll(tasks).stream().map(future -> {
                try {
                    return future.get();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).toList();
        }
    }
}
