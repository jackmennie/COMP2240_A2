package src.problem3;

public class Seat {
    private boolean isTaken;

    Seat() {
        isTaken = false;
    }

    public synchronized void getSeatForCustomer(Customer customer) throws Exception {
        if (isTaken) {
            throw new Exception();
        }

        isTaken = true;

    }

    public boolean isTaken() {
        return isTaken;
    }

    public void resetSeat() {
        this.isTaken = false;
    }
}
