package src.problem3;

public class Customer implements Runnable {
    private String id = "";
    private int arrivalTime = 0, eatingTime = 0, leavingTime = 0, seatedTime = 0;
    private int time = 0;
    private boolean seated = false, started = false;
    private Restaurant restaurant;
    private boolean finished;

    public String getId() {
        return id;
    }

    // constructor giving the thread the gate, the semaphor and the id of the
    // thread.
    public Customer(String id, int arrivalTime, int eatingTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.eatingTime = eatingTime;
        finished = false;
    }

    public void arriveAtRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        while (!finished) {
            if (restaurant.getAvailableSeats() > 0 && !started) {
                started = true;

                try {
                    restaurant.getASeat(this);
                    System.out.println("\t\t" + id + " got a seat");

                    if (restaurant.getAvailableSeats() == 0) {
                        this.restaurant.closeRestaurant();
                    }

                    // takes a seat.
                    seatedTime = restaurant.getTime();
                    seated = true;
                    leavingTime = seatedTime + eatingTime;
                    // take seat.

                    while (seated) {
                        time = restaurant.getTime();
                        if (leavingTime <= time) {
                            System.out.println("\t\t" + id + " has finished");
                            // makes the total finished increment by 1.
                            restaurant.incrementTotalFinished();
                            // decrements the waitinguntil variable
                            restaurant.decrementWaitingUntil();

                            seated = false;

                            restaurant.leaveRestaurant(id);
                            finished = true;
                            // leavingFunction();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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