package src.problem3;

public class Seat {
    private Restaurant restaurant;
    private boolean isTaken;

    Seat(Restaurant restaurant) {
        this.restaurant = restaurant;
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
}
