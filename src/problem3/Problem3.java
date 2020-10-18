package src.problem3;

import java.util.ArrayList;

public class Problem3 {
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
    }

    public void run() {
        int bookedSeats = customers.size();

        while (restaurant.getCompleted(bookedSeats)) {
            while (!full && !empty) {
                if (restaurant.getTotalFinished() >= bookedSeats) {
                    empty = true;
                    break;
                }

                restaurant.isCleaning();

                int time = restaurant.getTime();

                for (Customer customer : customers) {
                    if (customer.getArrivalTime() <= time && !customer.getStarted()) {

                        if (restaurant.isOpen()) {
                            new Thread(customer).start();

                        }
                    }
                }

                if (restaurant.getAvailableSeats() == 0) {
                    full = true;
                    break;
                }

                restaurant.incrementTime();

            }

            // sets the waiting until 5 process's are done.
            restaurant.setWaitingUntil();

            while (full && !empty) {
                if (restaurant.getTotalFinished() >= bookedSeats) {
                    empty = true;
                    break;
                }

                restaurant.isCleaning();

                int time = restaurant.getTime();

                for (Customer customer : customers) {
                    if (customer.getArrivalTime() <= time && !customer.getStarted()) {
                        if (restaurant.isOpen()) {
                            new Thread(customer).start();

                        }
                    }
                }

                if (restaurant.getWaitingUntil() <= 0) {
                    full = false;
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
