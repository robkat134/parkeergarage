package parkeergarage;

import java.awt.Color;
import java.util.Random;

public class ReservationPlace extends Car {
	private static final Color COLOR=Color.gray;
	
    public ReservationPlace() {
    	Random random = new Random();
    	int stayMinutes = (int) (15 + (Math.random()*15)); //De tijd voordat de reserveerde auto komt. De plek wordt standaard 15minuten van te voren gereserveerd
        this.setMinutesLeft(stayMinutes);
        this.setMinutesParked(stayMinutes);
        this.setHasToPay(false);
        this.setReservatedSpot(true);
        this.setMinutesLeavingEarly(15);
        this.setTypeOfCar("reservation");
    }
  
    public ReservationPlace(boolean after) {
    	int stayMinutes = (int) (15);
        this.setMinutesLeft(stayMinutes);
        this.setMinutesParked(stayMinutes);
        this.setHasToPay(false);
        this.setReservatedSpot(true);
        this.setMinutesLeavingEarly((int)(Math.random()*30));  // De tijd dat de reserveerde auto eerder weggaat 
        this.setEarlyLeaving(true);
        this.setTypeOfCar("reservation");
    }
    
    public Color getColor(){
    	return COLOR;
    }
}

