
/**
 * Jack Mennie
 * C3238004
 * 
 * Input class for problem 3
 * Entry point, read file, init problem
 * Run 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class P3 {
    public static void main(String args[]) {
        if (args.length != 1) {
            System.out.println("You have not entered a file path!");
            System.exit(0);
        }

        File file = new File(args[0]);

        try {
            Scanner scanner = new Scanner(file).useDelimiter("[=\\,\\s+]");// Delimit on =, space and comma

            boolean isArrival = true;
            int arrivalTime = 0;
            int eatingTime = 0;
            String id = "";

            ArrayList<Customer> customers = new ArrayList<>();

            // Loop through that input and grab that information to run the program
            while (scanner.hasNext()) {
                // Input has two ints, arrival and eating time, enter arrival first as
                // it appears first, then toggle off to get eating time
                if (scanner.hasNextInt() && isArrival) {
                    arrivalTime = scanner.nextInt();
                    isArrival = false;
                } else if (scanner.hasNextInt() && !isArrival) {
                    eatingTime = scanner.nextInt();
                    isArrival = true;

                    Customer customer = new Customer(id, arrivalTime, eatingTime);
                    customers.add(customer);
                } else {
                    // Middle input is the id, so check if the next value is anything other
                    // then the empty string
                    String value = scanner.next();
                    if (value != " ") {
                        id = value;
                    }
                }
            }

            scanner.close();

            Problem3 problem = new Problem3();
            problem.init(customers);
            problem.run();
        } catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
    }
}