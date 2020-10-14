package src.problem2;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Restaurant {
    private final int MAX_CAPACITY = 5;
    private int currentCapacity;
    private int time;

    private Semaphore restaurantAccess = new Semaphore(MAX_CAPACITY, true);
    private Semaphore customerCount = new Semaphore(1, true);
    private Semaphore waitForCustomersToLeave = new Semaphore(1, true);
    private Semaphore clock = new Semaphore(1, true);

    private ArrayList<Customer> finishedCustomers;

    public Restaurant() {
        currentCapacity = 0;
        time = 0;
        finishedCustomers = new ArrayList<>();
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public boolean isFull() {
        return currentCapacity == MAX_CAPACITY;
    }

    public boolean isEmpty() {
        return currentCapacity == 0;
    }

    public ArrayList<Customer> getFinishedCustomers() {
        return finishedCustomers;
    }

    public void addFinishedCustomer(Customer customer) {
        finishedCustomers.add(customer);
    }

    public int getMaxCapacity() {
        return MAX_CAPACITY;
    }

    public void incrementCapacity() {
        this.currentCapacity++;
    }

    public void decrementCapacity() {
        try {
            waitForCustomersToLeave.acquire();
            this.currentCapacity--;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        waitForCustomersToLeave.release();
    }

    public Semaphore getAccess() {
        return restaurantAccess;
    }

    // increments the total finished by 1.
    public void incrementTotalFinished(Customer customer) {
        try {
            customerCount.acquire();
            finishedCustomers.add(customer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        customerCount.release();
    }

    // returns the total finished.
    synchronized public int getTotalFinished() {
        int temp = 0;
        try {
            customerCount.acquire();
            temp = finishedCustomers.size();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        customerCount.release();

        return temp;
    }

    // increments time by 1.
    public void incrementTime() {
        try {
            clock.acquire();
            time++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clock.release();
    }

    // returns the "time".
    synchronized public int getTime() {
        int temp = 0;
        try {
            clock.acquire();

            temp = time;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clock.release();
        return temp;
    }

    // sets the waiting until all 5 customers leave their seats.
    public void setWaitingUntil() {
        try {
            waitForCustomersToLeave.acquire();
            // waitUntil = 5;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        waitForCustomersToLeave.release();
    }

    // returns the remaining amount of customers.
    synchronized public int getWaitingUntil() {
        int temp = 0;
        try {
            waitForCustomersToLeave.acquire();
            temp = MAX_CAPACITY - currentCapacity;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        waitForCustomersToLeave.release();
        return temp;
    }
}
