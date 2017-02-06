package parkeergarage; 
import java.util.Random;
import java.awt.*;

public class AdHocCar extends Car {

	private static final Color COLOR=Color.red;
	private static int stayMinutes;

	
    /**
     * de constructor functie van deze class.
     * Bepaaly hoelang de auto blijft staan en of die moet betalen
     */
    public AdHocCar() {
    	Random random = new Random();
    	stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setMinutesParked(stayMinutes);
        this.setHasToPay(true);
        this.setReservationCar(false);
    }
    
    /**
     * de overloaded constructor van deze class
     * @param time bepaalt hoelang de auto blijft staan. 
     */
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
