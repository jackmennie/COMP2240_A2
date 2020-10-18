package src.problem2;

/**
 * Jack Mennie
 * C3238004
 * 
 * The COVID-Safe Restaurant
 * Using Semaphores 
 */

import java.util.concurrent.Semaphore;

public class Restaurant {
    private final int MAX_CLEANING_TIME = 5;
    private final int MAX_SEATS = 5;

    private int time;
    private int waitingCount;
    private int totalFinished;
    private boolean isOpen;
    private boolean isCleaning;
    private int startedCleaningAtTime;

    // The never ending list of Semaphores
    private Semaphore controller = new Semaphore(MAX_SEATS, true);
    private Semaphore waitingController = new Semaphore(1, true);
    private Semaphore timerController = new Semaphore(1, true);
    private Semaphore customerCountController = new Semaphore(1, true);
    private Semaphore cleaningController = new Semaphore(1, true);

    /**
     * Construct that restaurant We are open for Business^
     * 
     * ^only to 5 customers at a time
     */
    public Restaurant() {
        isOpen = true;
    }

    /**
     * @return boolean if restaurant is open or not
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Close the restaurant
     */
    public void closeRestaurant() {
        isOpen = false;
    }

    /**
     * Main controllers access
     * 
     * @return
     */
    public Semaphore getAccess() {
        return controller;
    }

    /**
     * Ongoing function that cleans the restaurant if cleaning is required.
     */
    public void cleanRestaurantIfRequired() {
        if (isCleaning) {
            if ((startedCleaningAtTime + MAX_CLEANING_TIME) == time) {
                isCleaning = false;

                setWaitingUntil();
                this.cleaningController.release();
                this.controller.release(MAX_SEATS);

                isOpen = true;
            }
        }
    }

    /**
     * All customers that have been eating have finally left We can now deep clean
     * the restaurant
     * 
     * Toggle that to true, set the startCleaningAtTime to the current time Acquire
     * that lock, so the other threads does not modify that time value
     */
    public void prepareRestaurantToClean() {
        isCleaning = true;
        startedCleaningAtTime = time;

        try {
            this.cleaningController.acquire();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Once the max amount of customers have seated This sets that number, and
     * tracks when they have finished eating.
     * 
     * This is used for the cleaning function once the waiting count is 0.
     */
    public void setWaitingUntil() {
        try {
            waitingController.acquire();
            waitingCount = MAX_SEATS;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        waitingController.release();
    }

    /**
     * Once the customer have left their seat, then decrease the waiting count
     */
    public void decrementWaitingUntil() {
        try {
            waitingController.acquire();
            waitingCount--;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        waitingController.release();
    }

    /**
     * 
     * @return the amount of customers still eating
     */
    public int getWaitingUntil() {
        int temp = 0;
        try {
            waitingController.acquire();
            temp = waitingCount;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        waitingController.release();
        return temp;
    }

    /**
     * Increments the time by 1
     */
    public void incrementTime() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ie) {
            // Handle the exception
        }

        try {
            timerController.acquire();
            time++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timerController.release();
    }

    /**
     * @return the current time
     */
    public int getTime() {
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

    /**
     * Add a customer to the total finished count
     */
    public void incrementTotalFinished() {
        try {
            customerCountController.acquire();
            totalFinished++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        customerCountController.release();
    }

    /**
     * @return the amount of finished customers
     */
    public int getTotalFinished() {
        try {
            customerCountController.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        customerCountController.release();

        return totalFinished;
    }

    /**
     * Similar to the above function, but returns true if the count is < the booked
     * seat number
     * 
     * @param bookedSeats
     * @return true if more customers are required to be processed
     */
    public boolean isPendingCustomers(int bookedSeats) {
        boolean temp = false;
        try {
            customerCountController.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        customerCountController.release();

        if (totalFinished < bookedSeats) {
            temp = true;
        } else {
            /**
             * Since cleaning is started after every 5 customers have left, then we will
             * find ourselves in an infinite loop because the cleaning thread as started.
             * 
             * These next few lines, ensure that there is no running threads upon completion
             * of the program.
             */
            cleaningController.release();
            waitingController.release();
            customerCountController.release();
            timerController.release();
        }

        return temp;
    }

    /**
     * Customer is done, they leave their seat Once there is no remaining customers,
     * then prepare the restuarant to clean
     * 
     * @param id
     */
    public void leaveRestaurant(String id) {
        if (getWaitingUntil() == 0) {
            prepareRestaurantToClean();
        }
    }
}