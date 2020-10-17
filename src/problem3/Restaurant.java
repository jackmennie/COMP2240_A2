package src.problem3;

import java.util.ArrayList;

public class Restaurant {

    private int time, waitUntil;
    private int totalFinished;
    private boolean isOpen;

    private int maxCleaningTime;
    private boolean isCleaning = false;
    private int startedCleaning;

    private ArrayList<Seat> seats;
    private final int maxSeats = 5;

    public Restaurant() {
        time = 0;
        totalFinished = 0;
        waitUntil = 0;
        maxCleaningTime = 5;
        isOpen = true;

        seats = new ArrayList<>();

        for (int i = 0; i < maxSeats; i++) {
            seats.add(new Seat(this));
        }

        setWaitingUntil();
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
                    System.out.println("\t\tThere is a seat for: " + customer.getId());
                    break;
                } catch (Exception e) {
                    // Do nothing with the exception
                }
            }
        } else {
            System.out.println("there is not available seats");
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
            System.out.println("\t\tCleaning");
            if ((startedCleaning + maxCleaningTime) == time) {
                isCleaning = false;

                setWaitingUntil();
                // this.cleaningLock.release();
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
            waitUntil = 5;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // one customer has left his seat.
    public synchronized void decrementWaitingUntil() {
        try {
            waitUntil--;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // returns the remaining amount of customers.
    public synchronized int getWaitingUntil() {
        int temp = 0;
        try {
            temp = waitUntil;
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
        System.out.println("\t\t\tWaiting for customers to leave: " + getWaitingUntil());
        if (getWaitingUntil() == 0) {
            System.out.println("\t\tAll customers have left: ");
            cleanRestaurant();
        }
    }
}