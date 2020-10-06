/**
 * Problem 1: The New Zealand Bridge Dilemma 
 * Jack Mennie
 * C3238004
 */
package src.problem1;

import java.util.ArrayList;

public class Problem1 {
    private int northFarmersSize;
    private int southFarmersSize;

    /**
     * Initialises the problem by passing in the params and sets it
     * 
     * @param numberOfNorthFarmers
     * @param numberOfSouthFarmers
     */
    public void init(int numberOfNorthFarmers, int numberOfSouthFarmers) {
        northFarmersSize = numberOfNorthFarmers;
        southFarmersSize = numberOfSouthFarmers;

    }

    /**
     * Sets up the farmers, threads, and runs it
     */
    public void run() {
        Bridge bridge = new Bridge();

        // Generate our farmer threads
        ArrayList<Thread> farmers = new ArrayList<>();

        for (int i = 0; i < northFarmersSize; i++) {
            String id = "N_Farmer" + (i + 1);
            Farmer farmer = new Farmer(id, "South", bridge);
            farmers.add(new Thread(farmer));
        }

        for (int i = 0; i < southFarmersSize; i++) {
            String id = "S_Farmer" + (i + 1);
            Farmer farmer = new Farmer(id, "North", bridge);
            farmers.add(new Thread(farmer));
        }

        // Start them threads
        for (Thread farmer : farmers) {
            farmer.start();
        }
    }
}
