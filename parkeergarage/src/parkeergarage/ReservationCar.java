package parkeergarage;

import java.awt.Color;
import java.util.Random;

public class ReservationCar extends Car {
	private static Color COLOR = Color.cyan;
	
    public ReservationCar() {
    	Random random = new Random();
    	int stayMinutes =  (int)(15 + random.nextFloat() * 3 * 60); //staytime 
        this.setMinutesLeft(stayMinutes);
        this.setMinutesParked(stayMinutes);
        this.setHasToPay(false);
       // this.setMinuteWaiter((int)(Math.random())*31);      
      //  this.setMinuteReservated(stayMinutes);
        this.setReservationCar(true);
        this.setReservatedSpot(false);
    }
    
    public ReservationCar(int time) {
    	Random random = new Random();
    	int stayMinutes =  time; //staytime 
        this.setMinutesLeft(stayMinutes);
        this.setMinutesParked(stayMinutes);
        this.setHasToPay(false);
       // this.setMinuteWaiter((int)(Math.random())*31);      
      //  this.setMinuteReservated(stayMinutes);
        this.setReservationCar(true);
        this.setReservatedSpot(false);
    }
    
    public Color getColor(){
    	return COLOR;
    }
}