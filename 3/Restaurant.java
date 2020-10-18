
/**
 * Jack Mennie
 * C3238004
 * 
 * The COVID-Safe Restaurant
 * Using Monitor 
 */

import java.util.ArrayList;

public class Restaurant {
    private final int MAX_CLEANING_TIME = 5;
    private final int MAX_SEATS = 5;

    private int time;
    private int waitingCount;
    private int totalFinished;
    private boolean isOpen;
    private boolean isCleaning = false;
    private int startedCleaningAtTime;

    // Wow no more semaphores
    private ArrayList<Seat> seats;

    /**
     * Construct that restaurant We are open for Business^
     * 
     * ^only to 5 customers at a time
     */
    public Restaurant() {
        waitingCount = MAX_SEATS;
        isOpen = true;

        seats = new ArrayList<>();

        for (int i = 0; i < MAX_SEATS; i++) {
            seats.add(new Seat());
        }
    }

    /**
     * Once the customers have all left their seats and cleaning is done, this
     * function will be called which sets each seat to available
     */
    public void resetSeats() {
        for (Seat seat : seats) {
            seat.resetSeat();
        }
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
     * The main monitor. Instead of using a Semaphore to acquire
     * 
     * This uses synchronisation to get a seat from the seat class
     * 
     * Loops for each seat to find if one is available
     * 
     * @param customer
     * @throws Exception
     */
    public synchronized void getASeat(Customer customer) throws Exception {
        // check if full
        if (restaurantHasAvailabeSeats()) {
            // Try and get that seat
            for (Seat seat : seats) {
                try {
                    seat.getSeatForCustomer(customer);
                    break;
                } catch (Exception e) {
                    // Do nothing with the exception
                }
            }
        }
    }

    /**
     * Goes through each seat to see if there is atleast one available
     * 
     * @return
     */
    public boolean restaurantHasAvailabeSeats() {
        for (Seat seat : seats) {
            if (!seat.isTaken()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Goes through each seat to see if there is a seat available and adds to the
     * count to be returned
     * 
     * @return
     */
    public int getAvailableSeats() {
        int count = 0;

        for (Seat seat : seats) {
            if (!seat.isTaken()) {
                count++;
            }
        }

        return count;
    }

    /**
     * Ongoing function that cleans the restaurant if cleaning is required.
     */
    public synchronized void cleanRestaurantIfRequired() {
        if (isCleaning) {
            if ((startedCleaningAtTime + MAX_CLEANING_TIME) == time) {
                isCleaning = false;

                setWaitingUntil();
                resetSeats();

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
    }

    /**
     * Once the max amount of customers have seated This sets that number, and
     * tracks when they have finished eating.
     * 
     * This is used for the cleaning function once the waiting count is 0.
     */
    public synchronized void setWaitingUntil() {
        try {
            waitingCount = MAX_SEATS;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Once the customer have left their seat, then decrease the waiting count
     */
    public synchronized void decrementWaitingUntil() {
        try {
            waitingCount--;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @return the amount of customers still eating
     */
    public synchronized int getWaitingUntil() {
        int temp = 0;
        try {
            temp = waitingCount;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * Increments the time by 1
     */
    public synchronized void incrementTime() {
        try {

            Thread.sleep(10);
        } catch (InterruptedException ie) {
            // Do nothing with the exception
        }

        try {
            time++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the current time
     */
    public synchronized int getTime() {
        int temp = 0;
        try {
            temp = time;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return temp;
    }

    /**
     * Add a customer to the total finished count
     */
    public synchronized void incrementTotalFinished() {
        totalFinished++;
    }

    /**
     * @return the amount of finished customers
     */
    public synchronized int getTotalFinished() {
        return totalFinished;
    }

    /**
     * Similar to the above function, but returns true if the count is < the booked
     * seat number
     * 
     * @param bookedSeats
     * @return true if more customers are required to be processed
     */
    public synchronized boolean getCompleted(int bookedSeats) {
        return totalFinished < bookedSeats;
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