package src.problem2;

public class Customer {
    private String ID;
    private int arrivalTime;
    private int eatingTime;

    public Customer(String ID, int arrivalTime, int eatingTime) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.eatingTime = eatingTime;
    }

    public int getEatingTime() {
        return eatingTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public String getID() {
        return ID;
    }
}
