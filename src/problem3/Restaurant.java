package src.problem3;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Restaurant {

    private int time, waitUntil;
    private int totalFinished;
    private boolean isOpen;

    private int maxCleaningTime;
    private boolean isCleaning = false;
    private int startedCleaning;

    // private Semaphore controller = new Semaphore(5, true);
    private ArrayList<Seat> seats;
    private final int maxSeats = 5;

    private Semaphore waitingController = new Semaphore(1, true);
    private Semaphore timerController = new Semaphore(1, true);
    private Semaphore customerCountController = new Semaphore(1, true);

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
                    // e.printStackTrace();
                    // throw e;
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

        // try {
        // this.cleaningLock.acquire();

        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
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
        try {
            customerCountController.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        customerCountController.release();

        return totalFinished;
    }

    synchronized public boolean getCompleted(int bookedSeats) {
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
            waitingController.release();
            customerCountController.release();
            timerController.release();
        }

        return temp;
    }

    public void leaveRestaurant(String id) {
        System.out.println("\t\t\tWaiting for customers to leave: " + getWaitingUntil());
        if (getWaitingUntil() == 0) {
            System.out.println("\t\tAll customers have left: ");
            cleanRestaurant();
        }
    }
}