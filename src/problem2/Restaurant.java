package src.problem2;

import java.util.concurrent.Semaphore;

public class Restaurant {
    private final int MAX_CAPACITY = 5;
    private int currentCapacity;

    private Semaphore restaurantAccess = new Semaphore(MAX_CAPACITY, true);
    private Semaphore closedForCleaning = new Semaphore(1, true);

    public Restaurant() {
        setCurrentCapacity(0);
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public Semaphore getAccess() {
        return restaurantAccess;
    }

    public Semaphore getCleaningLock() {
        return closedForCleaning;
    }
}
