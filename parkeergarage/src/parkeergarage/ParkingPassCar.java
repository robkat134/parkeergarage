package parkeergarage;

import java.util.Random;
import java.awt.*;

public class ParkingPassCar extends Car {
	
	int startTime = 15;
	
	private static final Color COLOR=Color.blue;
    public ParkingPassCar(Boolean busy) {
    	if (busy){
    		startTime = 240;
    	}
    	Random random = new Random();
    	int stayMinutes = (int) (startTime + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(false);
    }
    
    public Color getColor(){
    	return COLOR;
    }
}
