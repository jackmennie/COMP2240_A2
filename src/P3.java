package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import src.problem3.Customer;
import src.problem3.Problem3;

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

            while (scanner.hasNext()) {
                if (scanner.hasNextInt() && isArrival) {
                    arrivalTime = scanner.nextInt();
                    isArrival = false;
                } else if (scanner.hasNextInt() && !isArrival) {
                    eatingTime = scanner.nextInt();
                    isArrival = true;

                    Customer customer = new Customer(id, arrivalTime, eatingTime);
                    customers.add(customer);
                } else {
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