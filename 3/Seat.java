/**
 * Seat Jack Mennie C3238004
 * 
 * The Seat Monitor Class
 * 
 * Holds the logic of if a seat is taken
 */
public class Seat {
    private boolean isTaken;

    /**
     * Set that seat is taken to false
     */
    Seat() {
        isTaken = false;
    }

    /**
     * Takes in the customer thread and sets it to the seat
     * 
     * @param customer
     * @throws Exception
     */
    public synchronized void getSeatForCustomer(Customer customer) throws Exception {
        if (isTaken) {
            throw new Exception();
        }

        isTaken = true;

    }

    /**
     * Return if seat is taken
     * 
     * @return
     */
    public boolean isTaken() {
        return isTaken;
    }

    /**
     * Resets the seat to available
     */
    public void resetSeat() {
        this.isTaken = false;
    }
}
