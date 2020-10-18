
/**
 * I don't ever wanna feel
 * Like I did that day
 * Take me to the place I love
 * Take me all the way
 * I don't ever wanna feel
 * Like I did that day
 * Take me to the place I love
 * Take me all the way
 *      - Red Hot Chili Peppers - Under the Bridge
 * 
 * The bridge contains a semaphore which locks itself 
 * if it has detected load.
 * 
 * It takes 20 steps to cross the bridge, determined by
 * the max length.
 * 
 * Holds statistical information (bridge crossings), which
 * is the count of farmers that have crossed the bridge
 * 
 * Jack Mennie
 * C3238004
 */

import java.util.concurrent.Semaphore;

public class Bridge {
    private final int LENGTH = 20;
    private int bridgeCrossings;

    private Semaphore isDeadlocked = new Semaphore(1, true);

    public Bridge() {
        bridgeCrossings = 0;
    }

    public void incrementCrossings() {
        bridgeCrossings++;
    }

    public int getBridgeCrossings() {
        return bridgeCrossings;
    }

    public int getBridgeLength() {
        return LENGTH;
    }

    public Semaphore lock() {
        return isDeadlocked;
    }

}
