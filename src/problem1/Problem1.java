package src.problem1;

public class Problem1 implements Runnable {
    private int northFarmersSize;
    private int southFarmersSize;

    public void init(int numberOfNorthFarmers, int numberOfSouthFarmers) {
        northFarmersSize = numberOfNorthFarmers;
        southFarmersSize = numberOfSouthFarmers;

        System.out.println(String.format("%-9s %-9s", northFarmersSize, southFarmersSize));
    }

    @Override
    public void run() {
        System.out.println("Running P1");
    }
    
}
