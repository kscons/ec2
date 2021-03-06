package pools.threadpools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by serhii on 07.04.15.
 */
public class LogSavingPool {
    static ExecutorService executorService = Executors.newFixedThreadPool(100);

    public static void runProcess(Runnable runnable) {
        executorService.execute(runnable);
    }

}
