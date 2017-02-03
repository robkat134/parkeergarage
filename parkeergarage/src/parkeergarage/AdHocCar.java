package parkeergarage; 
import java.util.Random;
import java.awt.*;

public class AdHocCar extends Car {

	private static final Color COLOR=Color.red;
	private static int stayMinutes;

	
    public AdHocCar() {
    	Random random = new Random();
    	stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setMinutesParked(stayMinutes);
        this.setHasToPay(true);
        this.setReservationCar(false);
    }
    
    public AdHocCar(int time) {
    	int stayMinutes = (int) (time);
        this.setMinutesLeft(stayMinutes);
        this.setMinutesParked(stayMinutes);
        this.setHasToPay(true);
    }
    
    public Color getColor(){
    	return COLOR;
    }
    
    public static int getStayMinutes(){
    	return stayMinutes;
    }
}
