package learning.concurrency;

import java.util.List;
import java.util.concurrent.Callable;

public class ConcurrentTaskProcessor {

    public <T> List<T> executeWithPlatformPool(List<Callable<T>> tasks, int poolSize) throws Exception {
        // TODO: Implement using Executors.newFixedThreadPool and try-with-resources.
        // Invoke all tasks and retrieve their results.
        return List.of();
    }

    public <T> List<T> executeWithVirtualThreads(List<Callable<T>> tasks) throws Exception {
        // TODO: Implement using Executors.newVirtualThreadPerTaskExecutor and try-with-resources.
        // Invoke all tasks and retrieve their results.
        return List.of();
    }
}
