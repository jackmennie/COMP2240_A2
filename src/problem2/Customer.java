package src.problem2;

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
            System.out.println("\t\tPermits: " + restaurant.getAccess().availablePermits());

            if (this.restaurant.getAccess().availablePermits() > 0 && !started) {
                started = true;

                try {
                    System.out.println("\t\tPermits: " + restaurant.getAccess().availablePermits());
                    restaurant.getAccess().acquire();

                    if (this.restaurant.getAccess().availablePermits() == 0) {
                        this.restaurant.closeRestaurant();
                    }

                    // takes a seat.
                    seatedTime = restaurant.getTime();
                    System.out.println("\t\t" + id + " got a seat at: " + seatedTime);
                    seated = true;
                    leavingTime = seatedTime + eatingTime;
                    // take seat.

                    while (seated) {
                        time = restaurant.getTime();
                        if (leavingTime <= time) {
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // // only releases if there wasn't 5 people sitting at once.
                // if (restaurant.getWaitingUntil() < 0) {
                // System.out.println("All customers have left");
                // restaurant.prepareRestaurantToClean();
                // restaurant.getAccess().release();
                // }
            } else {
                System.out.println("SHOULD NEVER GET HIT");
                // leavingFunction();
            }

            System.out.println("I am getting here for infinite time");
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