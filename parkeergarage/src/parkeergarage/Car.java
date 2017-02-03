package parkeergarage;

import java.awt.*;

public abstract class Car {

    private Location location;
    private int minutesLeft;
    private boolean isPaying;
    private boolean hasToPay;
    private int minutesParked;
    private int minutesLeavingEarly = 0;
    private boolean reservationCar = false;
    private boolean reservatedSpot = false;
    private boolean earlyLeaving = false;
    private String typeOfCar = "nonreservation";

    /**
     * Constructor for objects of class Car
     */
    public Car() {
    }
    public void setMinutesLeavingEarly(int minutes) {
    	minutesLeavingEarly = minutes;
    }
    public String getTypeOfCar(){
    	return typeOfCar;
    }
    
    public void setTypeOfCar(String type){
    	typeOfCar = type;
    }
    
    public int getMinutesLeavingEarly() {
    	return minutesLeavingEarly;
    }
    public void setReservatedSpot(boolean a) {
    	reservatedSpot = a;
    }
    public boolean getEarlyLeaving() {
    	return earlyLeaving;
    }
    public void setEarlyLeaving(boolean a) {
    	earlyLeaving = a;
    }
    public boolean getReservatedSpot() {
    	return reservatedSpot;
    }
    public void setReservationCar(boolean a) {
    	reservationCar = a;
    }
    
    public boolean getReservationCar() {
    	return reservationCar;
    }
    
    public void setMinutesParked(int minutes) {
    	minutesParked = minutes;
    }
    
   /* public int getMinuteReservated() {
    	return minuteReservated;
    }
    
    public void setMinuteReservated(int minuteReservated) {
    	this.minuteReservated = minuteReservated;
    }
    
    public void setMinuteWaiter(int wait) {
    	minuteWaiter = wait;
    }
    
    public int getMinuteWaiter(){
    	return minuteWaiter;
    }*/
    
    public int getMinutesParked(){
    	return minutesParked;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getMinutesLeft() {
        return minutesLeft;
    }

    public void setMinutesLeft(int minutesLeft) {
        this.minutesLeft = minutesLeft;
    }
    
   /* public void setMinuteCounter(int minute) {
    	minuteCounter = minute;
    }
    
    public int getMinuteCounter() {
    	return minuteCounter;
    }*/
    
    public boolean getIsPaying() {
        return isPaying;
    }

    public void setIsPaying(boolean isPaying) {
        this.isPaying = isPaying;
    }

    public boolean getHasToPay() {
        return hasToPay;
    }

    public void setHasToPay(boolean hasToPay) {
        this.hasToPay = hasToPay;
    }

    public void tick() {
    	if(getMinutesLeavingEarly()> 0 ){
    		minutesLeavingEarly--;}
        minutesLeft--;
    }
    
    public void ret(){
    }
    
    public void setColor(String kleur) {
    }
    
    public void setColors(Boolean type) {
    }
    
    public abstract Color getColor();
}