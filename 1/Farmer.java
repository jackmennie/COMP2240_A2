/**
 * Well, old Macdonald had a farm, ee-I-ee-I-o And on his farm he had a cow,
 * ee-I-ee-I-o With a moo-moo here, and a moo-moo there Here a moo, there a moo,
 * everywhere a moo-moo Old Macdonald had a farm, ee-I-ee-I-o - Old MacDonald
 * 
 * Apparently new zealand farmers are stubborn But this guy does a few things
 * 
 * Implements threads runnable and uses the bridges semaphore to decide if they
 * can cross the bridge or not.
 * 
 * If the bridge says they cannot go on, then they wait If the bridge says they
 * can go on, then they start crossing the bride.
 * 
 * 
 * Jack Mennie C3238004
 */

public class Farmer implements Runnable {
    private String ID;
    private String direction;
    private Bridge bridge;

    /**
     * Sets up the farmer
     * 
     * @param ID
     * @param direction inital direction they are going
     * @param bridge    one bridge to rule them all
     */
    public Farmer(String ID, String direction, Bridge bridge) {
        this.ID = ID;
        this.direction = direction;
        this.bridge = bridge;
    }

    /**
     * A nice little util to swap the direction These farmers just like to go back
     * and forth
     * 
     * @param current
     */
    private void swapDirection(String current) {
        switch (current) {
            case "North":
                direction = "South";
                break;
            case "South":
                direction = "North";
                break;
        }
    }

    /**
     * The never ending never ending never ending never ending never ending never
     * ending never ending never ending never ending never ending never ending never
     * ending never ending never ending never ending never ending ...
     * 
     * loop.
     * 
     * 
     * Pretty simple logic
     * 
     * Get a hold of that bridge access, while still on that bridge, then output
     * your on that bridge have a quick nap Finally we reached the end swap that
     * direction because we actually wanted to go the other direction tell that
     * bridge to output NEON Give old mate Kev a go at the bridge
     */
    @Override
    public void run() {
        System.out.println(ID + ": Waiting for bridge. Going towards " + direction);

        int steps = 0;

        while (true) {
            try {
                bridge.lock().acquire();
                do {
                    steps = steps + 5;

                    // Output 5, 10, 15 but not 20 because sample output does not output 20
                    if (steps != bridge.getBridgeLength()) {
                        System.out.println(ID + ": Crossing bridge Step " + steps);
                    }

                    Thread.sleep(200);

                } while (steps < bridge.getBridgeLength());

                System.out.println(ID + ": Across the bridge");

                steps = 0;
                swapDirection(direction);
                bridge.incrementCrossings();

                System.out.println("NEON = " + bridge.getBridgeCrossings());

                bridge.lock().release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(ID + ": Waiting for bridge. Going towards " + direction);
        }
    }
}
