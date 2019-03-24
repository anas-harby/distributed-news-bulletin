package shared;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dispatcher {
    private ExecutorService executorService = null;

    public Dispatcher(int numThreads) {
        this.executorService = Executors.newFixedThreadPool(numThreads);
    }

    public void dispatch(Runnable runnable) {
        this.executorService.submit(runnable);
    }

    public void shutdown() {
        this.executorService.shutdown();
    }
}
