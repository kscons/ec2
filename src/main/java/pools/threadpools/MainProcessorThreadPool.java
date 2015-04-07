package pools.threadpools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainProcessorThreadPool {

    static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void runProcess(Runnable runnable) {
        executorService.execute(runnable);
    }


}





