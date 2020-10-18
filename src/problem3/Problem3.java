package src.problem3;

/**
 * Problem 2: The Covid-Safe Restaurant (but with a Monitor)
 * Jack Mennie
 * C3238004
 */

import java.util.ArrayList;

public class Problem3 {
    private ArrayList<Customer> customers = new ArrayList<>();
    private Restaurant restaurant;

    private boolean fullRestaurant = false;
    private boolean hasProcessedAllBooked = false;
    private int bookedSeats;

    /**
     * Initialises the problem by passing in an array of customers Assumes the order
     * is correct
     * 
     * @param customers
     */
    public void init(ArrayList<Customer> customers) {
        restaurant = new Restaurant();

        // Customers need reference of the restaurant they are at
        for (Customer customer : customers) {
            customer.bookedAtRestaurant(restaurant);
        }

        this.customers = customers;
        bookedSeats = customers.size();
    }

    /**
     * The outer logic of problem 3
     * 
     * Runs customer threads when they should be ran
     */
    public void run() {
        // While there is customers to be completed as per the booked night
        while (restaurant.getCompleted(bookedSeats)) {
            // While there is atleast one customer, then perform restaurant logic
            while (!fullRestaurant && !hasProcessedAllBooked) {
                if (checkCustomerAndPermitToEnter()) {
                    break;
                }

                if (restaurant.getAvailableSeats() == 0) {
                    fullRestaurant = true;
                    break;
                }

                restaurant.incrementTime();

            }

            // Restaurant is full, so we can set the waiting count
            restaurant.setWaitingUntil();

            // While full, the threads are still processing and can become complete,
            // and we still have customers to process, so lets keep checking them
            while (fullRestaurant && !hasProcessedAllBooked) {
                if (checkCustomerAndPermitToEnter()) {
                    break;
                }

                if (restaurant.getWaitingUntil() <= 0) {
                    fullRestaurant = false;
                    break;
                }

                restaurant.incrementTime();

            }

        }

        // prints out the "log" of the thread.
        System.out.printf("%-10s %-12s %-8s %-10s \n", "Customer", "arrives", "Seats", "Leaves");
        for (int j = 0; j < customers.size(); j++) {
            customers.get(j).getLog();
        }

    }

    /**
     * We need this function to break the loop Hence why it returns a boolean
     * 
     * If we have finished processing all customers that have booked, then we set
     * hasProcessedAllBooked to true which then will break out from the inner loop.
     * 
     * @return if we can break
     */
    private boolean checkCustomerAndPermitToEnter() {
        if (restaurant.getTotalFinished() >= bookedSeats) {
            hasProcessedAllBooked = true;
            System.out.println("Should be complete");
            return true;
        }

        restaurant.cleanRestaurantIfRequired();

        int time = restaurant.getTime();

        // Go through each customer that is booked, check if they have arrived, and
        // start that thread
        // only if the restaurant can allow them to come in.
        for (Customer customer : customers) {
            if (customer.getArrivalTime() <= time && !customer.getStarted()) {
                if (restaurant.isOpen()) {
                    new Thread(customer).start();

                }
            }
        }

        return false;
    }
}
