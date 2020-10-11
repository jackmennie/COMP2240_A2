package src.problem2;

public class Customer implements Runnable {
    private String ID;
    private int arrivalTime;
    private int eatingTime;
    private Restaurant restaurant;

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

	public void enterRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
	}
}
