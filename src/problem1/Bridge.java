package src.problem1;

import java.util.concurrent.Semaphore;

public class Bridge {
    private final int MAX_LOAD = 20;
    private int currentLoad;

    private Semaphore isDeadlocked = new Semaphore(1, true);

    public Bridge() {
        currentLoad = 0;
    }

    public void incrementLoad() {
        currentLoad++;
    }

    public int getMaxBridgeLoad() {
        return MAX_LOAD;
    }

    public Semaphore isDeadlocked() {
        return isDeadlocked;
    }

}
