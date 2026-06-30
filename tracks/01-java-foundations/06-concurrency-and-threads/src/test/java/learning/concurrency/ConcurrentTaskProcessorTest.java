package learning.concurrency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import static org.assertj.core.api.Assertions.assertThat;

class ConcurrentTaskProcessorTest {

    private ConcurrentTaskProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new ConcurrentTaskProcessor();
    }

    @Test
    void shouldExecuteTasksWithPlatformPoolTakingProportionalTime() throws Exception {
        int numberOfTasks = 10;
        int sleepMillis = 100;
        int poolSize = 2;

        List<Callable<String>> tasks = createTasks(numberOfTasks, sleepMillis);

        long start = System.currentTimeMillis();
        List<String> results = processor.executeWithPlatformPool(tasks, poolSize);
        long duration = System.currentTimeMillis() - start;

        // Since pool size is 2 and we have 10 tasks, it must run in 5 batches.
        // 5 * 100ms = 500ms minimum expected duration.
        assertThat(results).hasSize(numberOfTasks);
        assertThat(duration).isGreaterThanOrEqualTo(450); // Allowing slight system jitter
        System.out.println("Platform Pool took: " + duration + " ms");
    }

    @Test
    void shouldExecuteTasksWithVirtualThreadsTakingFlatTime() throws Exception {
        int numberOfTasks = 10;
        int sleepMillis = 100;

        List<Callable<String>> tasks = createTasks(numberOfTasks, sleepMillis);

        long start = System.currentTimeMillis();
        List<String> results = processor.executeWithVirtualThreads(tasks);
        long duration = System.currentTimeMillis() - start;

        // Virtual threads are unbound, they should all execute concurrently.
        // Expected time is close to 100ms.
        assertThat(results).hasSize(numberOfTasks);
        assertThat(duration).isLessThan(300); // Should be much faster than 500ms
        System.out.println("Virtual Threads took: " + duration + " ms");
    }

    private List<Callable<String>> createTasks(int count, int sleepMillis) {
        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final int index = i;
            tasks.add(() -> {
                Thread.sleep(sleepMillis);
                return "Task " + index;
            });
        }
        return tasks;
    }
}
