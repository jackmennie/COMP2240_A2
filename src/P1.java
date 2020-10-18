package src;

/**
 * Jack Mennie
 * C3238004
 * 
 * Input class for problem 1
 * Entry point, read file, init problem
 * Run 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import src.problem1.Problem1;

public class P1 {
    public static void main(String args[]) {
        if (args.length != 1) {
            System.out.println("You have not entered a file!");
            System.exit(0);
        }

        File file = new File(args[0]);

        int northFarmers = 0;
        int southFarmers = 0;
        boolean northRequired = true;

        try {
            Scanner scanner = new Scanner(file).useDelimiter("[=\\,\\s+]");// Delimit on =, space and comma

            // Loop through that input and grab that information to run the program
            while (scanner.hasNext()) {
                if (scanner.hasNextInt()) {
                    // This input has two ints, so simple boolean toggle
                    // Grab north first, then south next
                    if (northRequired) {
                        northFarmers = scanner.nextInt();
                        northRequired = false;
                        continue;
                    }

                    southFarmers = scanner.nextInt();
                } else {
                    // If its not an int, then continue on doing what it needs to do
                    scanner.next();
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }

        Problem1 problem = new Problem1();

        problem.init(northFarmers, southFarmers);
        problem.run();
    }
}