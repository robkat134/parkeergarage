package parkeergarage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class SimulatorView extends JFrame implements ActionListener{
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
    private JButton plus1 =new JButton("+1");
    private JButton plus100 =new JButton("+100");
    private JButton run =new JButton("run");
    
    public JLabel parkedCars = new JLabel("Parked Cars: ");
    public JLabel time = new JLabel("Time: ");

    public SimulatorView(int numberOfFloors, int numberOfRows, int numberOfPlaces, Simulator owner) 
    {
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
        
        plus1.addActionListener(this);
		plus100.addActionListener(this);
		run.addActionListener(this);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        JPanel textPanel = new JPanel();
        textPanel.add(parkedCars);
        textPanel.add(time);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(plus1);
		buttonPanel.add(plus100);
		buttonPanel.add(run);
		
		
        //		plus1.setBounds(0, 0, 100, 30);
        //		plus100.setBounds(0,0,100,30);
        //		run.setBounds(0,0,100,30);	
		
		contentPane.add(textPanel,BorderLayout.NORTH);
        contentPane.add(carParkView,BorderLayout.CENTER);
		contentPane.add(buttonPanel,BorderLayout.SOUTH);
        pack();
        setVisible(true);

        updateView();
    }
    public void setCarsParked()
    {
    	parkedCars.setText("parked cars: " + owner.totalCarsToday);
    }

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
            if (carParkImage == null) {
                return;
            }
    
            Dimension currentSize = getSize();
            if (size.equals(currentSize)) {
                g.drawImage(carParkImage, 0, 0, null);
            }
            else {
                // Rescale the previous image.
                g.drawImage(carParkImage, 0, 0, currentSize.width, currentSize.height, null);
            }
        }
    

        
        public void updateView() {
            // Create a new car park image if the size has changed.
            if (!size.equals(getSize())) {
                size = getSize();
                carParkImage = createImage(size.width, size.height);
            }
            Graphics graphics = carParkImage.getGraphics();
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e.getSource());
		System.out.println(owner);
		if(e.getSource() == plus100)
		{
			owner.tickFor(100);
		}
		if(e.getSource() == plus1)
		{
			owner.tickFor(1);
		}
		if(e.getSource() == run)
		{
			owner.toggleRunning();
		}



	}

}
