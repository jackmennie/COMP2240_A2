package src.problem2;

public class Customer implements Runnable {
    private String ID;
    private int arrivalTime;
    private int eatingTime;
    private int seatedTime;
    private int leavingTime;
    private Restaurant restaurant;
    private int currentTime;
    private boolean isSeated;

    public Customer(String ID, int arrivalTime, int eatingTime) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.eatingTime = eatingTime;
        isSeated = true;
    }

    public void getLog() {
        System.out.printf("%-14s %-10d %-10d %-8d \n", ID, arrivalTime, seatedTime, leavingTime);
    }

    public int getEatingTime() {
        return eatingTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public String getID() {
        return ID;
    }

    public void enterRestaurant(Restaurant restaurant, int time) {
        this.restaurant = restaurant;
        this.currentTime = time;
    }

    public void clock(int time) {
        this.currentTime = time;
    }

    @Override
    public void run() {
        try {
            restaurant.getAccess().acquire();
            // takes a seat.
            seatedTime = restaurant.getTime();
            isSeated = true;
            leavingTime = seatedTime + eatingTime;
            // take seat.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (isSeated) {
            currentTime = restaurant.getTime();
            if (leavingTime <= currentTime) {
                // makes the total finished increment by 1.
                restaurant.incrementTotalFinished(this);
                // decrements the waitinguntil variable
                restaurant.decrementCapacity();
                isSeated = false;
                break;
            }
        }

        // only releases if there wasn't 5 people sitting at once.
        if (restaurant.getWaitingUntil() < 0) {
            restaurant.getAccess().release();
        }
    }
}
