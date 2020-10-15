// import java.util.Scanner;
// import java.util.concurrent.Semaphore;
// import java.util.LinkedList;

// public class C3182761A2P2 {
// // making semaphor only have 1 resource.
// private Semaphore shopController = new Semaphore(0);

// // list of customer for inputdata.
// LinkedList<customer> customers = new LinkedList<customer>();

// // int variables
// private int totalCustomersComing = 0, nextToArrive = 0, nextToSit = 0,
// nextToFinish = 0, time;

// // boolean to do the rule if the 5 seats are taken. must wait till all have
// // left.
// private boolean full = false, empty = false;

// // the clock and controler.
// private shop iceCreamShop = new shop();

// private boolean test = true;

// public static void main(String[] args) {
// C3182761A2P2 Assignment = new C3182761A2P2();
// Assignment.run();
// }

// private void run() {

// // reads in the file given.
// readFile();
// shopController.release(5);
// // tests if all that will come are finished.
// while (iceCreamShop.getTotalFinished() < totalCustomersComing) {
// while (!full && !empty) {

// if (iceCreamShop.getTotalFinished() >= totalCustomersComing) {
// empty = true;
// break;
// }

// time = iceCreamShop.getTime();

// // it starts the customers thread when they arrive.
// for (int i = nextToArrive; i < customers.size(); i++) {
// if (customers.get(i).getArrivalTime() > time) // the for loop is broken since
// // the list is in order
// // so none of the following will be able to enter.
// {
// nextToArrive = i;
// break;
// }
// if (time == customers.get(i).getArrivalTime() &&
// !customers.get(i).getStarted()) {
// customer a = customers.get(i);
// new Thread(a).start();

// // this helps the first thread to start before the next thread. and the
// threads
// // continue before the main thread goes to fast.
// try {
// Thread.sleep(10);
// } catch (InterruptedException ie) {
// // Handle the exception
// }
// }
// }
// // sleeps for a small time to let the threads catch up.
// try {
// Thread.sleep(50);
// } catch (InterruptedException ie) {
// // Handle the exception
// }

// if (shopController.availablePermits() == 0) {
// full = true;
// break;
// }

// iceCreamShop.incrementTime();

// }
// // sets the waiting until 5 process's are done.
// iceCreamShop.setWaitingUntil();
// while (full && !empty) {

// if (iceCreamShop.getTotalFinished() >= totalCustomersComing) {
// empty = true;
// break;
// }

// // since i use time more then once in the next few steps i decided to make a
// // variable for it. to make program go faster.
// time = iceCreamShop.getTime();

// // it starts the customers thread when they arrive.
// for (int i = nextToArrive; i < customers.size(); i++) {
// if (customers.get(i).getArrivalTime() > time) // the for loop is broken since
// the list is in order
// // so none following will be able to enter.
// {
// nextToArrive = i;
// break;
// }

// if (time == customers.get(i).getArrivalTime() &&
// !customers.get(i).getStarted()) {
// customer a = customers.get(i);
// new Thread(a).start();

// // this helps the first thread to start before the next thread. i was getting
// // some threads starting not in order. so i did this sleep between starting
// // threads.
// // and the threads continue before the main thread goes to fast.
// try {
// Thread.sleep(10);
// } catch (InterruptedException ie) {
// // Handle the exception
// }
// }
// }

// // lets the 'customer' threads catch up if they are behind i found sometimes
// // they are slow and dont get the correct time.
// try {
// Thread.sleep(30);
// } catch (InterruptedException ie) {
// // Handle the exception
// }

// if (iceCreamShop.getWaitingUntil() <= 0) {
// full = false;
// shopController.release(5);
// break;

// } else {

// iceCreamShop.incrementTime();
// }

// }

// }

// // prints out the "log" of the thread.
// System.out.printf("%-10s %-12s %-8s %-10s \n", "Customer", "arrives",
// "Seats", "Leaves");
// for (int j = 0; j < customers.size(); j++) {
// customers.get(j).getLog();
// }

// }

// private void readFile() {
// String temp = "";
// String name = "";
// int arrivalTime = 0, eatingTime = 0, n = 0;
// boolean cond = true;
// // input of the amount of customer.
// Scanner input = new Scanner(System.in);
// while (cond) {
// temp = input.next();
// if (temp.equals("END")) {
// cond = false;
// break;
// }
// arrivalTime = Integer.parseInt(temp);
// name = input.next();
// temp = input.next();
// eatingTime = Integer.parseInt(temp);
// customer a = new customer(shopController, name, arrivalTime, eatingTime,
// iceCreamShop);
// customers.add(a);
// totalCustomersComing++;
// }
// }

// }