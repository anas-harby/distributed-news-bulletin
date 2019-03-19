import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private Map<String, Thread> workingThreads;

    public Dispatcher() {
        this.workingThreads = new HashMap<>();
    }

    public void dispatch(String id, Runnable runnable) {
        this.workingThreads.putIfAbsent(id, new Thread(runnable));
        if (this.workingThreads.get(id).isAlive())
            throw new RuntimeException("Thread is still alive. Unable to dispatch");
        this.workingThreads.get(id).start();
    }
}
