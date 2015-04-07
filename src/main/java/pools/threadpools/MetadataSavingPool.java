package pools.threadpools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by serhii on 07.04.15.
 */
public class MetadataSavingPool {
    static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void runProcess(Runnable runnable) {
        executorService.execute(runnable);
    }

}
