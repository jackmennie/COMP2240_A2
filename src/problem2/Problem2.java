package src.problem2;

import java.util.ArrayList;

public class Problem2 {
    private ArrayList<Customer> customers;

    public void init(ArrayList<Customer> customers) {
         this.customers = customers;
         System.out.println("Problem initialised with: " + customers.size() + " customers");
    }

    public void run() {
        System.out.println("Running problem 2");

        Restaurant restaurant = new Restaurant();

        int time = 0;

        for(Customer customer : customers) {
            customer.enterRestaurant(restaurant);
            customer.clock(time);
            Thread thread = new Thread(customer);
            thread.start();
        }
    }
}
