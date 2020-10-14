package src.problem2;

public class Customer implements Runnable {
    private String ID;
    private int arrivalTime;
    private int eatingTime;
    private int seatedTime;
    private int leavingTime;
    private Restaurant restaurant;
    private int currentTime;
    private boolean hasNotSeated;

    public Customer(String ID, int arrivalTime, int eatingTime) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.eatingTime = eatingTime;
        hasNotSeated = true;
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

    public void enterRestaurant(Restaurant restaurant, int time) {
        this.restaurant = restaurant;
        this.currentTime = time;
    }

    public void clock(int time) {
        this.currentTime = time;
    }

    @Override
    public void run() {
        boolean isSeated = true;

        while (isSeated) {
            System.out.println("\t" + currentTime);
            if (arrivalTime <= currentTime) {
                
                if(hasNotSeated) {
                    System.out.println("Can seat");
                    try {
                        restaurant.getAccess().acquire();
                        seatedTime = currentTime;
                        hasNotSeated = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                }

               if(currentTime == (eatingTime + seatedTime)) {
                   System.out.println("Can leave");
                   leavingTime = currentTime;
                   isSeated = false;
               }
            } else {
                System.out.println("\tHave not arrived yet");
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            currentTime++;
        }

        if(restaurant.getCurrentCapacity() == restaurant.getMaxCapacity()) {
            restaurant.getAccess().release();
        }
    }
}
