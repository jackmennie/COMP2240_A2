package src.problem2;

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

        do {
            System.out.println("Time in outer Loop: " + time);

            if(restaurant.getCurrentCapacity() <= restaurant.getMaxCapacity()) {
                Customer customer = customers.remove();
                customer.enterRestaurant(restaurant, time);
                Thread thread = new Thread(customer);
                thread.start();
            }
            // for(Customer customer : customers) {
            //     customer.enterRestaurant(restaurant);
            //     customer.clock(time);
            //     Thread thread = new Thread(customer);
            //     thread.start();
            // }

            time++;
        } while(customers.size() > 6);

        
    }
}
