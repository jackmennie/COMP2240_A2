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

        while (restaurant.getCompleted(bookedSeats)) {
            System.out.println("Outer Loop");
            while (!full && !empty) {
                if (restaurant.getTotalFinished() >= bookedSeats) {
                    empty = true;
                    break;
                }

                restaurant.isCleaning();

                int time = restaurant.getTime();
                System.out.println("\n!F!E\tTIME: " + time);

                for (Customer customer : customers) {
                    if (customer.getArrivalTime() <= time && !customer.getStarted()) {
                        if (restaurant.isOpen()) {
                            System.out.println("\tStarting " + customer.getId());
                            new Thread(customer).start();

                        }
                    }
                }

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
                if (restaurant.getTotalFinished() >= bookedSeats) {
                    empty = true;
                    break;
                }

                restaurant.isCleaning();

                int time = restaurant.getTime();
                System.out.println("\nF!E\tTIME: " + time);

                for (Customer customer : customers) {
                    if (customer.getArrivalTime() <= time && !customer.getStarted()) {
                        if (restaurant.isOpen()) {

                            System.out.println("\tStarting " + customer.getId());
                            new Thread(customer).start();

                        }
                    }
                }

                if (restaurant.getWaitingUntil() <= 0) {
                    full = false;
                    // resta?urant.getAccess().release(5);
                    break;

                }

                restaurant.incrementTime();

            }

        }

        // prints out the "log" of the thread.
        System.out.printf("%-10s %-12s %-8s %-10s \n", "Customer", "arrives", "Seats", "Leaves");
        for (

                int j = 0; j < customers.size(); j++) {
            customers.get(j).getLog();
        }

    }
}
