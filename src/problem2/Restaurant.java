package src.problem2;

import java.util.concurrent.Semaphore;

public class Restaurant {

    private int time, waitUntil;
    private int totalFinished;
    private boolean isCleaning;
    private boolean isOpen;

    private int cleaningTime;
    private int maxCleaningTime;

    private Semaphore controller = new Semaphore(5, true);
    private Semaphore waitingController = new Semaphore(1, true);
    private Semaphore timerController = new Semaphore(1, true);
    private Semaphore customerCountController = new Semaphore(1, true);

    private int availableSeats;

    private Semaphore cleaningLock = new Semaphore(1, true);

    public Restaurant() {
        time = 0;
        totalFinished = 0;
        waitUntil = 0;
        isCleaning = false;
        cleaningTime = 0;
        maxCleaningTime = 5;
        isOpen = true;
        availableSeats = 5;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void closeRestaurant() {
        isOpen = false;
    }

    public void openRestaurant() {
        isOpen = true;
    }

    public Semaphore getAccess() {
        return controller;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void decrementAvailableSeats() {
        availableSeats--;
    }

    public void resetAvailableSeats() {
        availableSeats = 5;
    }

    boolean requiresClean = false;

    public void prepareRestaurantToClean() {
        requiresClean = true;
    }

    boolean cleaning = false;

    public void prepareForCleaning() {
        cleaning = true;
        requiresClean = true;
    }

    public boolean requiresCleaning() {
        return requiresClean;
    }

    public boolean cleanRestaurant() {
        // try {
        // Thread.sleep(10);
        // cleaningTime++;
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }

        if (cleaning) {
            System.out.println("Cleaning restaurant");
            try {
                this.cleaningLock.acquire();
                while (cleaningTime < maxCleaningTime) {
                    try {
                        Thread.sleep(70);
                        System.out.print(".");
                        cleaningTime++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Cleaning is done");
                this.cleaningLock.release();
                cleaningTime = 0;
                cleaning = false;
                requiresClean = false;
                isOpen = true;

                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        return true;
    }

    public boolean isCleaning() {
        return cleaning;
    }

    // sets the waiting until all 5 customers leave their seats.
    public void setWaitingUntil() {
        try {
            waitingController.acquire();
            waitUntil = 5;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        waitingController.release();
    }

    // one customer has left his seat.
    public void decrementWaitingUntil() {
        try {
            waitingController.acquire();
            waitUntil--;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        waitingController.release();
    }

    // returns the remaining amount of customers.
    synchronized public int getWaitingUntil() {
        int temp = 0;
        try {
            waitingController.acquire();
            temp = waitUntil;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        waitingController.release();
        return temp;
    }

    // increments time by 1.
    public void incrementTime() {
        try {
            timerController.acquire();
            time++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timerController.release();
    }

    // returns the "time".
    synchronized public int getTime() {
        int temp = 0;
        try {
            timerController.acquire();

            temp = time;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timerController.release();
        return temp;
    }

    // increments the total finished by 1.
    public void incrementTotalFinished() {
        try {
            customerCountController.acquire();
            totalFinished++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        customerCountController.release();
    }

    // returns the total finished.
    synchronized public int getTotalFinished() {
        int temp = 0;
        try {
            customerCountController.acquire();
            temp = totalFinished;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        customerCountController.release();

        return temp;
    }

}