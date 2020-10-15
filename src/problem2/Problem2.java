package src.problem2;

import java.util.ArrayList;

public class Problem2 {
    private ArrayList<Customer> customers = new ArrayList<>();
    private Restaurant restaurant;

    private int time;

    // boolean to do the rule if the 5 seats are taken. must wait till all have
    // left.
    private boolean full = false, empty = false;

    public void init(ArrayList<Customer> customers) {
        time = 0;

        restaurant = new Restaurant();

        // Customers need reference of the restaurant they are at
        for (Customer customer : customers) {
            customer.arriveAtRestaurant(restaurant);
        }

        this.customers = customers;
        System.out.println("Problem initialised with: " + customers.size() + " customers");
    }

    public void run() {
        int bookedSeats = customers.size();

        // restaurant.getAccess().release(5); // Release 5 seats

        while (restaurant.getTotalFinished() < bookedSeats) {
            while (!full && !empty) {
                if (restaurant.isCleaning()) {
                    restaurant.cleanRestaurant();
                }

                if (restaurant.getTotalFinished() >= bookedSeats) {
                    empty = true;
                    break;
                }

                time = restaurant.getTime();

                for (Customer customer : customers) {
                    if (time == customer.getArrivalTime() && !customer.getStarted()) {
                        new Thread(customer).start();

                        catchUpThreads(10);
                    }
                }

                catchUpThreads(50);

                if (restaurant.getAccess().availablePermits() == 0) {
                    full = true;
                    break;
                }

                restaurant.incrementTime();

            }

            // sets the waiting until 5 process's are done.
            restaurant.setWaitingUntil();

            while (full && !empty) {
                if (restaurant.isCleaning()) {
                    restaurant.cleanRestaurant();
                }

                if (restaurant.getTotalFinished() >= bookedSeats) {
                    empty = true;
                    break;
                }

                time = restaurant.getTime();

                for (Customer customer : customers) {
                    if (time == customer.getArrivalTime() && !customer.getStarted()) {
                        new Thread(customer).start();

                        catchUpThreads(10);
                    }
                }

                catchUpThreads(30);

                if (restaurant.getWaitingUntil() <= 0) {
                    full = false;
                    restaurant.getAccess().release(5);
                    break;

                } else {

                    restaurant.incrementTime();
                }

            }

        }
        // prints out the "log" of the thread.
        System.out.printf("%-10s %-12s %-8s %-10s \n", "Customer", "arrives", "Seats", "Leaves");
        for (int j = 0; j < customers.size(); j++) {
            customers.get(j).getLog();
        }

    }

    /**
     * Catch up threads prevents java from running threads out of order Since this
     * program requires customers coming in at a certain time we do not want to
     * allow a later customer to go before an earlier one
     * 
     * @param amount
     */
    private void catchUpThreads(int amount) {
        try {
            Thread.sleep(amount);
        } catch (InterruptedException ie) {
            // Handle the exception
        }
    }
}
