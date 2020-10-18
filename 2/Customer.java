
/**
 * Customer Jack Mennie C3238004
 * 
 * The customer holds all the information of when they have arrived, how long
 * they eat, when they have left, when they were allowed to seat
 * 
 * They have reference of the restaurant they sit at, and abides by their covid
 * safe plan
 * 
 */

public class Customer implements Runnable {
    // Customer attributes
    private String id;
    private int arrivalTime;
    private int eatingTime;
    private int leavingTime;
    private int seatedTime;

    // Thread attributes
    private boolean seated;
    private boolean started;
    private Restaurant restaurant;
    private boolean finished;

    /**
     * Construct that initial customer
     * 
     * @param id
     * @param arrivalTime
     * @param eatingTime
     */
    public Customer(String id, int arrivalTime, int eatingTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.eatingTime = eatingTime;
    }

    /**
     * Customer has called up and they want to dine in at this restaurant, so assign
     * them that they will be attending
     * 
     * @param restaurant
     */
    public void bookedAtRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    /**
     * The inner workings of the customers mind. Must have consideration of the
     * restaurant they have booked since they have a covid plan
     */
    @Override
    public void run() {
        // Keep the thread going until there is nothing left to do
        while (!finished) {
            // If no permits are available, then keep looping. Restaurant only allows 5
            // people at once!!!
            // Also if they have already started, then don't start again
            if (this.restaurant.getAccess().availablePermits() > 0 && !started) {
                started = true;

                try {
                    // Get that access
                    restaurant.getAccess().acquire();

                    // Restaurant may be full now, if it is, then close access to all
                    if (this.restaurant.getAccess().availablePermits() == 0) {
                        this.restaurant.closeRestaurant();
                    }

                    // Takes a seat at the current time
                    seatedTime = restaurant.getTime();
                    seated = true;
                    leavingTime = seatedTime + eatingTime;

                    // While seated, then eat ya food
                    while (seated) {
                        // Since time is a finicking thing with threads, we compare if its less than or
                        // equal to
                        // Once condition is met, then the customer can leave it's seat, pay their bill
                        // and go on their merry way
                        if (leavingTime <= restaurant.getTime()) {
                            restaurant.incrementTotalFinished();
                            restaurant.decrementWaitingUntil();

                            seated = false;

                            restaurant.leaveRestaurant(id);
                            finished = true; // No more work needed
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Prints those stats I was talking about at the start
     */
    public void getLog() {
        System.out.printf("%-14s %-10d %-10d %-8d \n", id, arrivalTime, seatedTime, leavingTime);
    }

    /**
     * Returns the arrival time
     * 
     * @return
     */
    public int getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Returns if they have started or not
     */
    public boolean getStarted() {
        return started;
    }
}