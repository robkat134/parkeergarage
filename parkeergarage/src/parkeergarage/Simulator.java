package parkeergarage;

import java.security.acl.Owner;
import java.util.Random;

import javax.swing.JLabel;

public class Simulator {

	private static final String AD_HOC = "1";
	private static final String PASS = "2";
	

	private boolean isRunning = false;

    private int abonnementhouders = 80;//aantal abonnementhouders
	private int[][] abonnementhoudersPlekken;
	private int[][][] toegestaanVoorAbonnementhouders;
    private int geparkeerdeAbonnementhouders = 0;
    private int geparkeerdeZonderAbonnement = 0;
    public int totaalGeparkeerd = 0;
    private int AantalVrijePlekken = 0;
    private int TotaalAantalPlekken = 540;
	
	private CarQueue entranceCarQueue;
    private CarQueue entrancePassQueue;
    private CarQueue paymentCarQueue;
    private CarQueue exitCarQueue;
    private SimulatorView simulatorView;
    
    private int passCarsNow = 0; // auto's met abonnement die er geweest zijn vanaf starten programma.
    private int passCarsToday = 0; // auto's met abonnement die geteld worden tot het einde van de dag (1440 minuten).
    private int nonPassCarsNow = 0; // auto's zonder abonnement die er geweest zijn vanaf starten programma.
    private int nonPassCarsToday = 0; // auto's zonder abonnement die geteld worden tot het einde van de dag (1440 minuten).
    private int passCarsNowInReservedSpace = 0;

    private int day = 0;
    private int hour = 0;
    private int minute = 0;
    
    private int tickCount = 0;

    private int tickPause = 10;

    private int passHoldersCostPerDay = 500;
    private int nonPassHoldersCostOneMinute = 2; 
    private int incomePassHolderPerDay = 0;
    private int incomeNonPassHolderPerDay = 0;
    
    int weekDayArrivals= 100; // average number of arriving cars per hour
    int weekendArrivals = 200; // average number of arriving cars per hour
    int weekDayPassArrivals= 50; // average number of arriving cars per hour
    int weekendPassArrivals = 5; // average number of arriving cars per hour

    int enterSpeed = 3; // number of cars that can enter per minute
    int paymentSpeed = 7; // number of cars that can pay per minute
    int exitSpeed = 5; // number of cars that can leave per minute

    public Simulator() { 

    	abonnementhoudersPlekken = new int[][]{
    		{0, 0, 0, 1, 1, 0},
    		{0, 0, 0, 0, 0, 0},
    		{0, 0, 0, 0, 0, 0}
    	};
    	
        entranceCarQueue = new CarQueue();
        entrancePassQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();
        simulatorView = new SimulatorView(3, 6, 30, this);
        
    }
    public void init(){
    	updateViews();
    	isRunning = true;
    }

    public void run() 
    {
    	//System.out.println(isRunning);
        while (isRunning && tickCount < 10000)
        {
        	//System.out.println(tickCount);
        	tick();
        	tickCount++;
        	if (tickCount >= 10000)
        	{
        		
        	}
        }
    }
    
    public void tickFor(int amount)
    {
    	for (int i = 0; i < amount; i++) 
    	{
    		manualStep();
    	}
    }
    void manualStep() 
    {
    	advanceTime();
    	handleExit();
    	updateViews();
    	simulatorView.setCarsParked();
    	totaalGeparkeerd = geparkeerdeZonderAbonnement + geparkeerdeAbonnementhouders;
    	AantalVrijePlekken = TotaalAantalPlekken - totaalGeparkeerd;
    	handleEntrance();
    }

    public void makeParkingMap() {
    	int i = 0, j = 0, k = 0;
    	int abo = abonnementhouders;
    	for(i = 0; i < simulatorView.getNumberOfFloors(); i++) {
    		for(j = 0; j < simulatorView.getNumberOfRows(); j++) {
    			for(k = 0; k < simulatorView.getNumberOfPlaces(); k++) {
    				
    				if(abo != 0 && abonnementhoudersPlekken[i][j] == 1){
    					if(abo / simulatorView.getNumberOfPlaces() != 0) {
    						toegestaanVoorAbonnementhouders[i][j][k] = 1;
    					} else if(k < abo % simulatorView.getNumberOfPlaces()) {
    						toegestaanVoorAbonnementhouders[i][j][k] = 1;
    					}
    				} else {
    					toegestaanVoorAbonnementhouders[i][j][k] = 0;
    				}
    			}
    		}
    	}
    }
    
    void tick() 
    {
    	makeParkingMap();
    	advanceTime();
    	handleExit();
    	updateViews();
    	simulatorView.setCarsParked();
    	totaalGeparkeerd = geparkeerdeZonderAbonnement + geparkeerdeAbonnementhouders;
    	AantalVrijePlekken = TotaalAantalPlekken - totaalGeparkeerd;
//    	simulatorView.parkedCars = new JLabel("Totaal geparkeerd: " + totaalGeparkeerd);
//    	System.out.println("Zonder abo: " + geparkeerdeZonderAbonnement);
// 		System.out.println("Met abo: " + geparkeerdeAbonnementhouders);
// 		System.out.println("Totaal geparkeerd: " + totaalGeparkeerd);
// 		System.out.println("Totaal aantal vrije plekken:  " + AantalVrijePlekken);
    	// Pause.
        try {
        	
            Thread.sleep(tickPause);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    	handleEntrance();
    }
    
    public void toggleRunning()
    {
    	isRunning = !isRunning;
    	System.out.println(isRunning);
    	run();
    }

    private void advanceTime(){
        // Advance the time by one minute.
        minute++;
        while (minute > 59) {
            minute -= 60;
            hour++;
        }
        while (hour > 23) {
            hour -= 24;
            day++;
        }
        while (day > 6) {
            day -= 7;
        }
        displayTime();
        System.out.println("Dag: "+ day);
        System.out.println("tijd " + hour + " : "+ minute );

    }
	private void displayTime() {
		if (hour < 10)
        {
        	if (minute < 10)
        		simulatorView.time.setText("Time: 0"+ hour + ":0" + minute);
        	else
        		simulatorView.time.setText("Time: 0"+ hour + ":" + minute);
        }
        else
        {
        	if (minute < 10)
        		simulatorView.time.setText("Time: "+ hour + ":0" + minute);
        	else
        		simulatorView.time.setText("Time: "+ hour + ":" + minute);
        }
	}

    private void handleEntrance(){
    	// Onderstaande aanroep zorgt voor een random toestroom van auto's met en zonder abonnement. Er wordt gekeken naar of het weekend is of niet. De nieuw toegestroomde auto's komen in queue. Autos met abonnement in de entrancePassQueue en zonder in de entranceCarQueue
    	carsArriving();
    	
    	// De verschillende autos worden op de juiste locatie geplaatst
    	carsEntering(entrancePassQueue);
    	carsEntering(entranceCarQueue);  	
        
    	// Onderstaande 4 regels zijn voor de stats
    	int allCarsToday = nonPassCarsToday + passCarsToday;
        int allCarsNow = nonPassCarsNow + passCarsNow;
        System.out.println("Alle gepasseerde auto's vandaag: "+allCarsToday);
        System.out.println("Alle gepasseerde auto's van start: "+allCarsNow);
    }
    
    private void handleExit(){
        carsReadyToLeave();
        carsPaying();
        carsLeaving();
    }
    
    private void updateViews(){
    	simulatorView.tick();
        // Update the car park view.
        simulatorView.updateView();	
    }
    
    private void carsArriving(){
    	// In onderstaande variablele wordt de hoeveelheid auto's random berekend voor mensen zonder een abonnement. Programma kijkt zelf al naar of het weekend is of niet
    	int numberOfCars=getNumberOfCars(weekDayArrivals, weekendArrivals);
    	
    	// De nieuwe auto's die bij de ingang staan worden aan een queue toegevoegd met wachtende auto's om te parkeren.
        addArrivingCars(numberOfCars, AD_HOC);    	
        
        // Hieronder wordt opnieuw een random getal berekend voor binnenkomende auto's, maar dan voor mensen met en abonnement
    	numberOfCars=getNumberOfCars(weekDayPassArrivals, weekendPassArrivals);
    	
    	// De nieuwaangeschoven auto's met abonnement komen in een queue te staan om de parkeergarage binnen te treden.
        addArrivingCars(numberOfCars, PASS);    	
    }
    
    private int getNumberOfNeededParkingSpots(int[][] abo, SimulatorView simulatorview){
    	int i, j, counter = 0;
    	for(i = 0; i < simulatorview.getNumberOfFloors(); i++){
    		for(j = 0; j < simulatorView.getNumberOfRows(); j++) {
    			if (abo[i][j] == 1) {
    				counter += simulatorview.getNumberOfPlaces();	
    			}
    		}
    		
    	}
    	return counter;
    }
    
/**
 * Onderstaande functie zet auto's weg in de parkeergarege. Er wordt gekeken naar welke rijen gereserveerd zijn voor de 
 * abonnementhouders. De gereserveerde plekken mogen alleen de abonnementhouders gebruiken maar mocht het zo zijn dat de
 * gereserveerde plekken op zijn dan worden de abonnementhouders op een niet gereserveerde plaats geplaatst. 
 *  * @param queue
 */
    private void carsEntering(CarQueue queue) {
        int i = 0;
        boolean goThroughIf = true;
        
        // Check the amount of spaces that is freed with reserving parking rows and store this value in an int variable ('freed parking spots' = 'number of rows freed' * 'number of parkingspaces in one row') 
        int reservedPlacesForPassHolders = getNumberOfNeededParkingSpots(abonnementhoudersPlekken, simulatorView);
        
        // If the amount of subscribers is less then the reserved spaces it's unnecessary to keep all of the reserved. The amount of reserved spaces will be reduced to the number of subscriptions
        if( abonnementhouders < reservedPlacesForPassHolders ) {
        	reservedPlacesForPassHolders = abonnementhouders; 
        }
        // Remove car from the front of the queue and assign to a parking space.
    	while( queue.carsInQueue() > 0 && simulatorView.getNumberOfOpenSpots() > 0 && i < enterSpeed ) {
    		
    		// Haal een auto uit de queue en sla deze op in het type Car
            Car car = queue.removeCar();
            
            //Zodra de dag voorbij is worden de auto's die die dag binnengekomen zijn terug gebracht naar 0
            if( hour == 0 && minute == 0 && goThroughIf == true ) {
            	passCarsToday = 0;
            	nonPassCarsToday = 0;
            	goThroughIf = false;
            }
            
            // De rij met abonnementhouders kunnen parkeren op de gereserveerde plekken.
            if( queue == entrancePassQueue ) {
            	
            	// Onderstaande 3 variabelen zijn nodig voor de stats
            	passCarsNow++;
	            passCarsToday++;
            	geparkeerdeAbonnementhouders++;
	            
            	if( passCarsNowInReservedSpace < reservedPlacesForPassHolders ) {      
	            	// Kijk naar de eerst volgende vrije locatie voor een abonnementhouder. Als reservedPlacesForPassHolders == passCarsNow, dan is er geen plek meer en zal de auto dus niet geplaatst kunnen worden op een gereserveerde locatie
	            	Location freeLocation = simulatorView.getFirstFreeLocationPass(abonnementhoudersPlekken, reservedPlacesForPassHolders-passCarsNow);
	            	// Plaats de auto op de gevonden vrije locatie
	            	simulatorView.setCarAt(freeLocation, car);
	            	passCarsNowInReservedSpace++;
            	}
            }
            
            //De andere auto's en auto's met abonnement maar zonder plek kunnen op de eerst volgende plekken parkeren.
            else {
            	// Kijk naar de eerst volgende vrije locatie voor een niet abonnementhouder
                Location freeLocation = simulatorView.getFirstFreeLocation(abonnementhoudersPlekken ,abonnementhouders);
                // Plaats de auto op de gevonde vrije locatie
                simulatorView.setCarAt(freeLocation, car);
                
                // Omdat de overgebleven abonnementhouders ook hier kunnen komen moet er even gecheckt worden of het wel om een niet abonnementhouder gaat voor de stats
                if( queue == entranceCarQueue ) {
                    // Onderstaande is nodig voor de stats
                    geparkeerdeZonderAbonnement++;
                    nonPassCarsNow++;
    	            nonPassCarsToday++;
                }

            }
            i++;           
        }
    }
    
    private void carsReadyToLeave(){
    	int i = 0, j = 0;
    	
        // Add leaving cars to the payment queue.
        Car car = simulatorView.getFirstLeavingCar();
        while (car != null) {
        	
        	// Er wordt gekeken of auto moet betalen(geen abonnement) of wel
        	if (car.getHasToPay()){
        		
        		// Auto komt hierna in betalingsqueue dus wordt er alvast gezegd dat hij betaald heeft
	            car.setIsPaying(true);
	            
	            // Auto wordt aan de queue toegevoegd voor het betalen. De auto heeft dus nog niet betaald en is dus nog niet vertrokken???(even chekcen)
	            paymentCarQueue.addCar(car);
	            
	            // Onderstaande is voor de stats
	            --geparkeerdeZonderAbonnement;
	            nonPassCarsNow--;
        	}
        	else {
        		// Als het een abonnementhouder is dan mag hij direct weg zonder in de rij te staan voor het betalen of voor het verlaten. 
        		carLeavesSpot(car);
        		for(i = 0; i < simulatorView.getNumberOfFloors(); i++) {
        			for(j = 0; j < simulatorView.getNumberOfRows(); j++) {
        				if(abonnementhoudersPlekken[i][j] == 1) {
        					car.getLocation().getFloor();
        				}
        			}
        		}
        		if(toegestaanVoorAbonnementhouders[car.getLocation().getFloor()][car.getLocation().getRow()][car.getLocation().getPlace()] == 1) {
        			passCarsNowInReservedSpace--;
        		}
        		geparkeerdeAbonnementhouders--;
        		passCarsNow--;
        	}
            car = simulatorView.getFirstLeavingCar();
        }
    }

    private void carsPaying(){
        // Let cars pay.
    	int i=0;
    	while (paymentCarQueue.carsInQueue()>0 && i < paymentSpeed){

            Car car = paymentCarQueue.removeCar();
            //------------- TODO Handle payment.
            carLeavesSpot(car);
            i++;
    	}
    }
    // Hieronder gaan de autos pas uit het gebouw
    private void carsLeaving(){
        // Let cars leave.
    	int i=0;
    	while (exitCarQueue.carsInQueue()>0 && i < exitSpeed){
            exitCarQueue.removeCar();
            i++;
    	}	
    }
    
    private int getNumberOfCars(int weekDay, int weekend){
        Random random = new Random();

        // Get the average number of cars that arrive per hour.
        int averageNumberOfCarsPerHour = day < 5
                ? weekDay
                : weekend;

        // Calculate the number of cars that arrive this minute.
        double standardDeviation = averageNumberOfCarsPerHour * 0.3;
        double numberOfCarsPerHour = averageNumberOfCarsPerHour + random.nextGaussian() * standardDeviation;
        return (int)Math.round(numberOfCarsPerHour / 60);	
    }
    
    private void addArrivingCars(int numberOfCars, String type){
        // Add the cars to the back of the queue.
    	switch(type) {
    	case AD_HOC: 
    		
    		// Auto's zonder abonnement wordt bijgehouden hoeveel tijd ze geparkeerd staan om vervolgens te berekenen hoeveel ze moeten betalen. hasToPay wordt naar true gezet
            for (int i = 0; i < numberOfCars; i++) {
            	entranceCarQueue.addCar(new AdHocCar());
            }
            break;
    	case PASS:
    		
    		// Auto's zonder abonnement hoeven niet bij het uitrijden direct te betalen maar betalen een vast bedrag per x aantal dagen. hasToPau wordt bij hun op false gezet
            for (int i = 0; i < numberOfCars; i++) {
            	entrancePassQueue.addCar(new ParkingPassCar());
            }
            break;	            
    	}
    }
    
    private void carLeavesSpot(Car car){
    	// Auto locatie komt helemaal op NULL te staan en wordt toegevoegd aan het exitCarQueue. De auto staat dus hierna in de rij om het gebouw te verlaten
    	simulatorView.removeCarAt(car.getLocation());
        exitCarQueue.addCar(car);
    }

}


