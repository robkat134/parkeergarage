package parkeergarage;

import java.util.Random;
import java.awt.*;

public class AdHocCar extends Car {
	private static final Color COLOR=Color.red;
	
    public AdHocCar(Boolean busy, int stayTime) {
    	int stayMinutes = 0;
    	Random random = new Random();
    	if(busy){
    	   stayMinutes = stayTime;
    	   this.setMinutesLeft(stayMinutes);
           this.setHasToPay(true);
    	} else {
    	stayMinutes = (int) (stayTime + random.nextFloat() * 3 * 60); 
    	System.out.println(stayMinutes);
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(true);
     }
    }
    
    public Color getColor(){
    	return COLOR;
    }
}
