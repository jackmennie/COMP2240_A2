package src.problem3;

import java.util.ArrayList;

public class Restaurant {
    private final int MAX_CLEANING_TIME = 5;
    private final int MAX_SEATS = 5;

    private int time;
    private int waitingCount;
    private int totalFinished;
    private boolean isOpen;
    private boolean isCleaning = false;
    private int startedCleaning;

    private ArrayList<Seat> seats;

    public Restaurant() {
        time = 0;
        totalFinished = 0;
        waitingCount = MAX_SEATS;
        isOpen = true;

        seats = new ArrayList<>();

        for (int i = 0; i < MAX_SEATS; i++) {
            seats.add(new Seat());
        }
    }

    public void resetSeats() {
        for (Seat seat : seats) {
            seat.resetSeat();
        }
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

    public boolean restaurantHasAvailabeSeats() {
        for (Seat seat : seats) {
            if (!seat.isTaken()) {
                return true;
            }
        }

        return false;
    }

    public int getAvailableSeats() {
        int count = 0;

        for (Seat seat : seats) {
            if (!seat.isTaken()) {
                count++;
            }
        }

        return count;
    }

    public synchronized void isCleaning() {
        if (isCleaning) {
            if ((startedCleaning + MAX_CLEANING_TIME) == time) {
                isCleaning = false;

                setWaitingUntil();
                resetSeats();

                isOpen = true;
            }
        }
    }

    public void cleanRestaurant() {
        isCleaning = true;
        startedCleaning = time;
    }

    // sets the waiting until all 5 customers leave their seats.
    public synchronized void setWaitingUntil() {
        try {
            waitingCount = MAX_SEATS;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // one customer has left his seat.
    public synchronized void decrementWaitingUntil() {
        try {
            waitingCount--;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // returns the remaining amount of customers.
    public synchronized int getWaitingUntil() {
        int temp = 0;
        try {
            temp = waitingCount;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    // increments time by 1.
    public void incrementTime() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ie) {
            // Do nothing with the exception
        }

        try {
            time++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // returns the "time".
    synchronized public int getTime() {
        int temp = 0;
        try {
            temp = time;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return temp;
    }

    // increments the total finished by 1.
    public void incrementTotalFinished() {
        totalFinished++;
    }

    // returns the total finished.
    public int getTotalFinished() {
        return totalFinished;
    }

    public boolean getCompleted(int bookedSeats) {
        return totalFinished < bookedSeats;
    }

    public void leaveRestaurant(String id) {
        if (getWaitingUntil() == 0) {
            cleanRestaurant();
        }
    }
}