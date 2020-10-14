package src.problem2;

import java.util.ArrayList;
import java.util.Queue;

public class Problem2 {
    private Queue<Customer> customers;

    public void init(Queue<Customer> customers) {
        this.customers = customers;
        System.out.println("Problem initialised with: " + customers.size() + " customers");
    }

    public void run() {
        System.out.println("Running problem 2");

        Restaurant restaurant = new Restaurant();

        int time = 0;

        int bookedCustomers = customers.size();

        while (restaurant.getTotalFinished() < bookedCustomers) {
            // Start welcoming customers in
            while (!restaurant.isFull() && !restaurant.isEmpty()) {
                if (restaurant.getTotalFinished() >= bookedCustomers) {
                    System.out.println("Restaurant is now empty");
                    // empty = true
                    break;
                }

                time = restaurant.getTime();

                // Time to start accepting customers
                if (customers.peek().getArrivalTime() <= time) {
                    Customer customer = customers.remove();
                    restaurant.incrementCapacity();
                    new Thread(customer).start();

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // Do nothing with the exception
                    }
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException ie) {
                    // Handle the exception
                }

                restaurant.incrementTime();
            }

            // Since the restaurant is full, or empty (customer has not arrived yet).
            // Then we must set the waiting time for the 5 customers
            restaurant.setWaitingUntil();

            while (restaurant.isFull() && !restaurant.isEmpty()) {
                if (restaurant.getTotalFinished() >= bookedCustomers) {
                    // empty is true
                    break;
                }

                if (customers.peek().getArrivalTime() <= time) {
                    Customer customer = customers.remove();
                    restaurant.incrementCapacity();
                    new Thread(customer).start();

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // Do nothing with the exception
                    }
                }

                // Let the customer thread catch up
                try {
                    Thread.sleep(30);
                } catch (InterruptedException ie) {
                    // Do nothing the exception
                }

                if (restaurant.getWaitingUntil() <= 0) {
                    restaurant.getAccess().release();
                    break;

                } else {

                    restaurant.incrementTime();
                }
            }
        }

        // prints out the "log" of the thread.
        System.out.printf("%-10s %-12s %-8s %-10s \n", "Customer", "arrives", "Seats", "Leaves");
        for (Customer customer : restaurant.getFinishedCustomers()) {
            customer.getLog();
        }
    }
}
