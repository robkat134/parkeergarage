package parkeergarage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class SimulatorView extends View{
	private Simulator owner;
    private CarParkView carParkView;
    private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;
    private int numberOfOpenSpots;
    private Car[][][] cars;
    
    public JLabel parkedCars = new JLabel("Parked Cars: ");
    public JLabel time = new JLabel("Time: ");
    
    public JLabel inkomsten = new JLabel("Huidige inkomsten: �");

    public SimulatorView(int numberOfFloors, int numberOfRows, int numberOfPlaces, Simulator owner, Model model) 
    {
    	super(model);
    	this.owner = owner;
        this.numberOfFloors = numberOfFloors;
        this.numberOfRows = numberOfRows;
        this.numberOfPlaces = numberOfPlaces;
        this.numberOfOpenSpots =numberOfFloors*numberOfRows*numberOfPlaces;
        cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];
        
        carParkView = new CarParkView();

        setLayout(new BorderLayout());
        
        JPanel textPanel = new JPanel();
        textPanel.add(parkedCars);
        textPanel.add(time);
		
		add(textPanel,BorderLayout.NORTH);
        add(carParkView,BorderLayout.CENTER);
		//add(buttonPanel,BorderLayout.SOUTH);
        setVisible(true);

        updateView();
    }
    public void setCarsParked()
    {
    	parkedCars.setText("parked cars: "+owner.totaalGeparkeerd);
    }
    
    public void setInkomsten()
    {
    	if(owner.totaalOntvangen%100 < 10 && owner.totaalOntvangen%100 != 0) {
    		inkomsten.setText("huidige inkomsten: �"+ owner.totaalOntvangen/100 + ",0" + owner.totaalOntvangen%100);
    	} else if (owner.totaalOntvangen%100 == 0) {
    		inkomsten.setText("huidige inkomsten: �"+ owner.totaalOntvangen/100 + ",00");
    	} else {
    		inkomsten.setText("huidige inkomsten: �"+ owner.totaalOntvangen/100 + "," + owner.totaalOntvangen%100);
    	}
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

    public Car removeCarAt(Location location) {
        if (!locationIsValid(location)) {
            return null;
        }
        Car car = getCarAt(location);
        if (car == null) {
            return null;
        }
        cars[location.getFloor()][location.getRow()][location.getPlace()] = null;
        car.setLocation(null);
        numberOfOpenSpots++;
        return car;
    }

    
/**
 * Eerste locatie waar auto's zonder abonnement parkeren.
 */
    public Location getFirstFreeLocation(int abonnementhouders) {
    	int startAt = 0;
      //}
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
            	if (floor == 0 && row == 0){
            		startAt = abonnementhouders;
            	} else if(floor == 1 && row == 0 && abonnementhouders > 30){
            		startAt = abonnementhouders -30;
	            } else if(floor == 2 && row == 0 && abonnementhouders > 60){
	        		startAt = abonnementhouders -60;
	        	}
                else {
            		startAt = 0;
            	}
                for (int place = startAt; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    if (getCarAt(location) == null) {
                        return location;
                    }
                }
            }
        }
        return null;
    }
    /**
     * Locatie waar auto's met abonnement kunnen staan.
     */
    public Location getFirstFreeLocationPass() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows() -5; row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    if (getCarAt(location) == null) {
                        return location;
                    }
                }
            }
        }
        return null;
    }

    public Car getFirstLeavingCar() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    Car car = getCarAt(location);
                    if (car != null && car.getMinutesLeft() <= 0 && !car.getIsPaying()) {
                        return car;
                    }
                }
            }
        }
        return null;
    }

    public void tick() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
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
        private BufferedImage carParkBuffer = new BufferedImage(800, 500, BufferedImage.TYPE_INT_RGB);;
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
                carParkBuffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
                carParkImage = createImage(size.width, size.height);
                System.out.println("redrawing image");
            }
            //System.out.println(carParkBuffer);
            Graphics graphics = carParkBuffer.getGraphics();
            for(int floor = 0; floor < getNumberOfFloors(); floor++) {
                for(int row = 0; row < getNumberOfRows(); row++) {
                    for(int place = 0; place < getNumberOfPlaces(); place++) {
                        Location location = new Location(floor, row, place);
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
