package src.problem2;

import java.util.ArrayList;

public class Problem2 {
    private ArrayList<Customer> customers = new ArrayList<>();
    private Restaurant restaurant;

    private boolean full = false, empty = false;

    public void init(ArrayList<Customer> customers) {
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

        while (restaurant.getTotalFinished() < bookedSeats) {
            System.out.print("Outer Loop | ");

            while (!full && !empty) {
                System.out.print("1st Loop | ");
                // if (restaurant.requiresCleaning() && !restaurant.isCleaning()) {
                // restaurant.cleanRestaurant();
                // }

                if (restaurant.getTotalFinished() >= bookedSeats) {
                    empty = true;
                    break;
                }

                int time = restaurant.getTime();

                System.out.println(time);

                for (Customer customer : customers) {
                    if (customer.getArrivalTime() <= time && !customer.getStarted()) {
                        if (restaurant.isOpen()) {
                            System.out.println("\tStarting " + customer.getId());
                            new Thread(customer).start();

                            catchUpThreads(100);
                        }
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
            System.out.print("Set waiting until | ");
            restaurant.setWaitingUntil();

            while (full && !empty) {
                System.out.print("2nd Loop | ");

                // if (restaurant.requiresCleaning() && !restaurant.isCleaning()) {
                // restaurant.cleanRestaurant();
                // }

                if (restaurant.isCleaning()) {
                    restaurant.cleanRestaurant();
                }

                if (restaurant.getTotalFinished() >= bookedSeats) {
                    empty = true;
                    break;
                }

                int time = restaurant.getTime();
                System.out.println(time);

                for (Customer customer : customers) {
                    System.out.println(
                            "Checking customer: " + customer.getId() + ", has started? " + customer.getStarted());
                    if (customer.getArrivalTime() <= time && !customer.getStarted()) {
                        if (restaurant.isOpen()) {

                            System.out.println("\tStarting " + customer.getId());
                            new Thread(customer).start();

                            catchUpThreads(100);
                        }
                    }
                }

                catchUpThreads(100);

                if (restaurant.getWaitingUntil() <= 0) {
                    full = false;
                    restaurant.getAccess().release(5);
                    break;

                } else {

                    restaurant.incrementTime();
                }

            }

        }

        System.out.print("Log | ");
        // prints out the "log" of the thread.
        System.out.printf("%-10s %-12s %-8s %-10s \n", "Customer", "arrives", "Seats", "Leaves");
        for (

                int j = 0; j < customers.size(); j++) {
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
