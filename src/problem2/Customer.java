package src.problem2;

public class Customer implements Runnable {
    private String id = "";
    private int arrivalTime = 0, eatingTime = 0, leavingTime = 0, seatedTime = 0;
    private int time = 0;
    private boolean seated = false, started = false;
    private Restaurant restaurant;

    // constructor giving the thread the gate, the semaphor and the id of the
    // thread.
    public Customer(String id, int arrivalTime, int eatingTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.eatingTime = eatingTime;
    }

    public void arriveAtRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        started = true;

        try {
            System.out.println("Permits: " + restaurant.getAccess().availablePermits());
            restaurant.getAccess().acquire();
            // takes a seat.
            seatedTime = restaurant.getTime();
            System.out.println("\t" + id + " got a seat at: " + seatedTime);
            seated = true;
            leavingTime = seatedTime + eatingTime;
            // take seat.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (seated) {
            time = restaurant.getTime();
            if (leavingTime <= time) {
                // makes the total finished increment by 1.
                restaurant.incrementTotalFinished();
                // decrements the waitinguntil variable
                restaurant.decrementWaitingUntil();
                seated = false;
                break;
            }
        }
        // only releases if there wasn't 5 people sitting at once.
        if (restaurant.getWaitingUntil() < 0) {
            System.out.println("All customers have left");
            restaurant.prepareRestaurantToClean();
            restaurant.getAccess().release();
        }
    }

    public void getLog() {
        System.out.printf("%-14s %-10d %-10d %-8d \n", id, arrivalTime, seatedTime, leavingTime);
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public boolean getStarted() {
        return started;
    }
}