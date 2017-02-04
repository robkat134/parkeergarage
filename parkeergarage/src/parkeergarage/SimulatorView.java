package parkeergarage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.image.*;

public class SimulatorView extends View{
	private Simulator owner;
    private CarParkView carParkView;
    private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;
    private int numberOfOpenSpots;
    private int timeForReservation;
    private boolean busyhour;
    private int timeForBusyHourStaying;

    
    int nfloor;
    int nrow;
    int nplace;
    private CarQueue entranceReservedQueue = new CarQueue();
    
    /*private int numberOfPassCarsOnReservedSpotNow;
    private int numberOfPassCarsNotReservedSpotNow;
    private int numberOfPassCarsOnReservedSpotToday;
    private int numberOfPassCarsNotReservedSpotToday;
    private int numberOfNonPassCarsNow;
    private int numberOfNonPassCarsToday;*/
    
    private Car[][][] cars;
    
    public JLabel parkedCars = new JLabel("Parked Cars: ");
    public JLabel time = new JLabel("Time: ");
    public JLabel day = new JLabel ("Day: ");

    public SimulatorView(int numberOfFloors, int numberOfRows, int numberOfPlaces, Simulator owner, Model model) 
    {
    	super(model);
    	this.owner = owner;
        this.numberOfFloors = numberOfFloors;
        this.numberOfRows = numberOfRows;
        this.numberOfPlaces = numberOfPlaces;
        this.numberOfOpenSpots =numberOfFloors*numberOfRows*numberOfPlaces;
        cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];
        
        /* Aantal geparkeerde auto's op nul zetten
        numberOfPassCarsOnReservedSpotNow = 0;
        numberOfPassCarsNotReservedSpotNow = 0;
        numberOfPassCarsOnReservedSpotToday = 0;
        numberOfPassCarsNotReservedSpotToday = 0;
        numberOfNonPassCarsNow = 0;
        numberOfNonPassCarsToday = 0;*/
        
        carParkView = new CarParkView();

        setLayout(new BorderLayout());
        
        JPanel textPanel = new JPanel();
        textPanel.add(parkedCars,BorderLayout.CENTER);
        textPanel.add(time,BorderLayout.CENTER);
        textPanel.add(day,BorderLayout.CENTER);
		

		add(textPanel,BorderLayout.NORTH);
        add(carParkView,BorderLayout.CENTER);
        setVisible(true);

        updateView();
    }
    public void setCarsParked()
    {

    	parkedCars.setText("parked cars: "+owner.totalCarsToday);
    }
    
   /* public void setInkomsten()
    {
    	if(owner.totaalOntvangen%100 < 10 && owner.totaalOntvangen%100 != 0) {
    		inkomsten.setText("huidige inkomsten: �"+ owner.totaalOntvangen/100 + ",0" + owner.totaalOntvangen%100);
    	} else if (owner.totaalOntvangen%100 == 0) {
    		inkomsten.setText("huidige inkomsten: �"+ owner.totaalOntvangen/100 + ",00");
    	} else {
    		inkomsten.setText("huidige inkomsten: �"+ owner.totaalOntvangen/100 + "," + owner.totaalOntvangen%100);
    	}
    }*/

    public void updateView() {
        carParkView.updateView();
    }
    
	public int getNumberOfFloors() {
        return numberOfFloors;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfPlaces() {
        return numberOfPlaces;
    }

    public int getNumberOfOpenSpots(){
    	return numberOfOpenSpots;
    }
    
    public Car getCarAt(Location location) {
        if (!locationIsValid(location)) {
            return null;
        }
        return cars[location.getFloor()][location.getRow()][location.getPlace()];
    }


    public boolean setCarAt(Location location, Car car) {
        if (!locationIsValid(location)) {
            return false;
        }
        Car oldCar = getCarAt(location);
        if (oldCar == null) {
            cars[location.getFloor()][location.getRow()][location.getPlace()] = car;
            car.setLocation(location);
            numberOfOpenSpots--;
            return true;
        }
        return false;
    }
    
    
    public void changeColor(Car car) {
    	
    }
    
    public Car removeCarAt(Location location) {
        if (!locationIsValid(location)) {
            return null;
        }
        Car car = getCarAt(location);
        if (car == null) {
            return null;
        }
        // Plaats de auto op de gevonde vrije locatie
        if(car.getReservationCar()){
        	 System.out.println("they");
        entranceReservedQueue.addCar(new ReservationPlace());
        setCarAt(location, car);
        }
        cars[location.getFloor()][location.getRow()][location.getPlace()] = null;
        car.setLocation(null);
        numberOfOpenSpots++;
        return car;
    }

    
    /**
     * Eerste locatie waar auto's zonder abonnement parkeren.
     */
    public Location getFirstFreeLocation(int[][][] aboPlekken) {

    	// Loop door alle verdiepingen
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
        	// Loop door alle rijen
            for (int row = 0; row < getNumberOfRows(); row++) {
            	// Loop door alle plaatsen
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                	// Check of de geselecteerde plaats beschikbaar is voor niet pashouders
                	if(aboPlekken[floor][row][place] == 0) {
                		// Maak een nieuwe locatie aan met de gevonde vrije plek
                		Location location = new Location(floor, row, place, false);
                		if( getCarAt(location) == null ) {
                			return location;
                		}
                	}
                }
            }
        }
        return null;
    }

    /**
     * Locatie waar auto's met abonnement kunnen staan.
     */
    public Location getFirstFreeLocationPass(int[][][] aboPlekken) {
    	
    	// Loop door alle verdiepingen
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
        	// Loop door alle rijen
            for (int row = 0; row < getNumberOfRows(); row++) {
            	// Loop door alle plaatsen 
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                	
                	// Als alle abonnementplaatsen bezet zijn(abo > 0) moet hij gewoon NULL returnen. Ook als de rij niet gereserveerd is voor abohouders moet hij NULL returnen. Anders gewoon een locatie
                	if(aboPlekken[floor][row][place] == 1){
                        Location location = new Location(floor, row, place, false);
                        if (getCarAt(location) == null) {
                            return location;
                        }
                	}

                }
            }
        }
        return null;
    }
    
    public Location getFirstFreeLocationRes(int[][][] aboPlekken) {
    	// Loop door alle verdiepingen
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
        	// Loop door alle rijen
            for (int row = 0; row < getNumberOfRows(); row++) {
            	// Loop door alle plaatsen 
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                	
                	// Als alle abonnementplaatsen bezet zijn(abo > 0) moet hij gewoon NULL returnen. Ook als de rij niet gereserveerd is voor abohouders moet hij NULL returnen. Anders gewoon een locatie
                	if(aboPlekken[floor][row][place] == 1){
                        Location location = new Location(floor, row, place, true);
                        if (getCarAt(location) == null) {
                            return location;
                        }
                	}

                }
            }
        }
        return null;
    }

    public Car getFirstLeavingCar() {//3
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place, false);
                    Car car = getCarAt(location);
                    if (car !=null && car.getReservatedSpot() == true && car.getMinutesLeft() == 0 && car.getEarlyLeaving() == false){
                    	makeNewCar(location);
                    } else if (car !=null && car.getReservationCar() == true && car.getMinutesLeft() == 0){
                    	makeNewCar1(location);
                    } else if (car !=null && car.getReservatedSpot() == true && car.getEarlyLeaving() == true && car.getMinutesLeavingEarly() == 0){
                    	return car;}
                    if (car != null && car.getMinutesLeft() <= 0 && !car.getIsPaying() && car.getReservationCar() !=true && car.getReservatedSpot() !=true) {
                        return car;
                    }
               }
            }
        }
        return null;
    }
    
    public void setTimeForReservation(int time){
    	timeForBusyHourStaying = time;
    }
    
    public int getTimeForReservation(){
    	return timeForBusyHourStaying;
    }
    
    private void makeNewCar(Location location){
    		if(getTimeForReservation() > 0){
    		Car car = new ReservationCar(getTimeForReservation());
    		setCarAt1(location, car);}
    		else{
    		Car car = new ReservationCar();
    		setCarAt1(location, car);}
	}
    
    private void makeNewCar1(Location location){
    	Car car = new ReservationPlace(true);
    	setCarAt1(location, car);
	}
    
    
    public boolean setCarAt1(Location location, Car car) {
            cars[location.getFloor()][location.getRow()][location.getPlace()] = car;
            car.setLocation(location);
            return true;
    }

    public void tick() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place, false);
                    Car car = getCarAt(location);
                    if (car != null) {
                        car.tick();
                    }
                }
            }
        }
    }

    private boolean locationIsValid(Location location) {
        int floor = location.getFloor();
        int row = location.getRow();
        int place = location.getPlace();
        if (floor < 0 || floor >= numberOfFloors || row < 0 || row > numberOfRows || place < 0 || place > numberOfPlaces) {
            return false;
        }
        return true;
    }
    
    private class CarParkView extends JPanel {
        
        private Dimension size;
        private Image carParkImage;   
        private BufferedImage carParkBuffer = new BufferedImage(800, 500, BufferedImage.TYPE_INT_ARGB);
        /**
         * Constructor for objects of class CarPark
         */
        public CarParkView() {
            size = new Dimension(0, 0);
        }
    
        /**
         * Overridden. Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize() {
            return new Dimension(800, 500);
        }
    
        /**
         * Overriden. The car park view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        public void paintComponent(Graphics g) {
        	
        	g.setColor(Color.decode("#EEEEEE"));
        	g.fillRect(0, 0, 838, 500);
            if (carParkBuffer == null) {
                return;
            }
    
            Dimension currentSize = getSize();
            if (size.equals(currentSize)) {
                g.drawImage(carParkBuffer, 0, 0, null);
            }
            else {
                // Rescale the previous image.
            	System.out.println(currentSize.getHeight());
            	if (currentSize.height > 500)
            	{
            		g.drawImage(carParkBuffer, 0, 0, currentSize.width, 500, null);
            		
            	}
            	else
            	{
            		g.drawImage(carParkBuffer, 0, 0, currentSize.width, currentSize.height, null);
            	}
            }
        }
    

        
        public void updateView() {
            // Create a new car park image if the size has changed.
            if (!size.equals(getSize())) 
            {
                size = getSize();
                carParkBuffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
                carParkImage = createImage(size.width, size.height);
                System.out.println("redrawing image");
            }
            //System.out.println(carParkBuffer);
            Graphics graphics = carParkBuffer.getGraphics();
            for(int floor = 0; floor < getNumberOfFloors(); floor++) {
                for(int row = 0; row < getNumberOfRows(); row++) {
                    for(int place = 0; place < getNumberOfPlaces(); place++) {
                        Location location = new Location(floor, row, place, false);
                        Car car = getCarAt(location);
                        Color color = car == null ? Color.white : car.getColor();
                        drawPlace(graphics, location, color);
                    }
                }
            }
            repaint();
        }
    
        /**
         * Paint a place on this car park view in a given color.
         */
        private void drawPlace(Graphics graphics, Location location, Color color) {
            graphics.setColor(color);
            graphics.fillRect(
                    location.getFloor() * 260 + (1 + (int)Math.floor(location.getRow() * 0.5)) * 75 + (location.getRow() % 2) * 20,
                    60 + location.getPlace() * 10,
                    20 - 1,
                    10 - 1); // TODO use dynamic size or constants
        }
    }

}
