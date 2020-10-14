package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

import src.problem2.Customer;
import src.problem2.Problem2;

public class P2 {
    public static void main(String args[]) {
        if(args.length != 1) {
            System.out.println("You have not entered a file path!");
            System.exit(0);
        }

        File file = new File(args[0]);

        try {
            Scanner scanner = new Scanner(file).useDelimiter("[=\\,\\s+]");//Delimit on =, space and comma

            boolean isArrival = true;
            int arrivalTime = 0;
            int eatingTime = 0;
            String id = "";

            Queue<Customer> customers = new ArrayDeque<>();

            while(scanner.hasNext()) {
                if(scanner.hasNextInt() && isArrival) {
                    arrivalTime = scanner.nextInt();
                    isArrival = false;
                } else if(scanner.hasNextInt() && !isArrival) {
                    eatingTime = scanner.nextInt();
                    isArrival = true;

                    Customer customer = new Customer(id, arrivalTime, eatingTime);
                    customers.add(customer);
                } else {
                    String value = scanner.next();
                    if(value != " ") {
                        id = value;
                    }
                }  
            }

            scanner.close();

            Problem2 problem = new Problem2();
            problem.init(customers);
            problem.run();
        } catch(FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
    }
}