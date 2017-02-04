package parkeergarage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import java.util.Random;
import javax.swing.JLabel;

//import parkeergarage.Car;

public class Simulator {

	/**
	 * In te vullen naar voorkeur
	 */
	protected boolean parkingOnNotReservedSpot = true; 
	// TODO dit hieronder ff chekcen. Wat is Abonnementhouders en wat is abonnementHoudersPlekken?
	private int abonnementHouders = 90; // Max aantal abonnenthouders
	private int abonnementHoudersPlekken = 60; // Max aantal abonnenthouders plekken.
    private int passHoldersCostPerWeek = 1100; // Kosten voor abonnementhouders
    private int nonPassHoldersCostPerMinute = 3; // Kosten per minute voor niet abonementhouders
    private int passHoldersCostPerMinute = 1; // Kosten per minute voor abonnementhouders
    private int reservationCostPerMinute = 2; // Kosten per minute voor reserveringen
    private int startTarief = 500; // Kosten om een reservering te plaatsen
    public int totaalGeparkeerd = 0;
    
    protected int incomePassHoldersPerDay = 0; // Inkomsten van abonnementhouders van die dag
    protected int incomePassHoldersTotal = 0; // Inkomsten van abonnementhouders intotaal
    protected int incomeNonPassHoldersPerDay = 0; // Inkomsten van niet abonnementhouders van die dag
    protected int incomeNonPassHoldersTotal = 0; // Inkomsten van niet abonnementhouders intotaal
    protected int incomePassHoldersPerWeek = 0; // Inkomsten van abonnementhouders per week(abo kosten zonder per minuut te hebben betaald)
    protected int incomeReservationPerDay = 0; // Inkomsten van reserveringen per dag
    protected int incomeReservationTotal = 0; // Inkomsten van reserveringen totaal
    
    private int nightHourStart = 21; //Begin van de rustige nachtelijke uren.
    private int nightHourEnd = 5; //Einde van de rustige nachtelijke uren.

	private Thread thread;
    public String event;
    
    
    private static final String AD_HOC = "1";
    private static final String PASS = "2";
    private static final String RES = "3";
    
    private boolean isRunning = false; 
    
    // Benodigde arrays om te checken of een abo auto ergens wel of niet mag parkeren
    private int[][] abonnementhoudersPlekken;
    private int[][][] toegestaanVoorAbonnementhouders;
   
    /**
     * Onderstaande is allemaal voor de stats. Gemaakt door Rob dus voor vragen bij hem zijn
     */
    protected int passCarsNowWithReservedSpot = 0; // Aantal abonnementhouders met een speciale abonnementhouders plek op dit moment
    protected int passCarsTodayWithReservedSpot = 0; // Aantal abonnementhouders met een speciale abonnementhouders plek die vandaag hun auto hebben gepakeerd
    protected int passCarsTotalWithReservedSpot = 0; // Aantal abonnementhouders met een speciale abonnementhouders plek die er vanaf het begin geparkeerd hebben
    protected int passCarsNowWithoutReservedSpot = 0; // Aantal abonnementhouders zonder een speciale abonnementhouders plek op dit moment
    protected int passCarsTodayWithoutReservedSpot = 0; // Aantal abonnementhouders zonder een speciale abonnementhouders plek die vandaag hun auto hebben geparkeerd
    protected int passCarsTotalWithoutReservedSpot = 0; // Aantal abonnementhouders zonder een speciale abonnementhouders pelk die er vanaf het begin geparkeerd hebben.
    
    protected int reservedCarsNow = 0; // Aantal reserveringen op dit moment
    protected int reservedCarsToday = 0; // Aantal reserveringen vandaag
    protected int reservedCarsTotal = 0; //Aantal reserveringen totaal gemaakt
    protected int nonPassCarsNow = 0; // Aantal niet abonnementhouders die hun auto geparkeerd hebben op dit moment
    protected int nonPassCarsToday = 0; // Aantal niet abonnementhouders die hun auto vnadaag geparkeerd hebben
    protected int nonPassCarsTotal = 0; // Aantal niet abonnementhouders die hun auto geparkeerd ehbben vanaf het begin
    protected int carsPassedToday = 0; // Aantal auto's die zijn doorgereden omdat de rij telang was 
    protected int carsPassedTotal = 0; // Aantal auto's die vanaf het starten zijn doorgereden omdat de rij telang was
    
    public int totalCarsToday = 0;
    
    // Om op te vragen hoeveel auto's er in de queue staan de wachten gebruik bijv. entranceCauQueue.size();
    private CarQueue entranceCarQueue;
    private CarQueue entrancePassQueue;
    private CarQueue entranceResQueue;
    private CarQueue paymentCarQueue;
    private CarQueue exitCarQueue;
    private SimulatorView simulatorView;
    private CarQueue paymentReservationQueue;
    
    private SoundPlayer soundPlayer;

    private View lineView;
    private View PieView;
    private View barView;
    private View statView;
    private Controller controller;
    private Model model;
    private JFrame screen;


    private int day = 0;
    private int hour = 0;
    private int minute = 0;
    private int tickCount = 0;

    private int tickPause = 100;

    private boolean playingSound; //True als er nog geluid speelt. Zorgt ervoor dat er geen twee geluiden door elkaar kunnen spelen.
    private int soundLastPlayed; //Onthoudt het uur waarop een geluid voor het laatst is afgespeeld, zodat een geluid maar 1 keer speelt per uur.

    int timeToStayBusy = 120;
    
    int timeBetweenHourAndBusyhour = 120; // Hiermee wordt de tijd bepaald tussen de huidige tijd en de eind tijd van het drukke uur
    int timeForBusyHourStaying = 60; // Deze variable wordt meegegeven waneer er een busyhour
    int timeForReservationDuringBusyHour = 120; // De tijd in minuten. Hoelang de gereserveerde auto's mogen blijven komen voor het eind.
    
    int weekDayResArrivals; // average number of arriving cars per hour
    int weekendResArrivals; // average number of arriving cars per hour
    int weekDayArrivals; // average number of arriving cars per hour
    int weekendArrivals; // average number of arriving cars per hour
    int weekDayPassArrivals = 90; // average number of arriving cars per hour
    int weekendPassArrivals = 25;// average number of arriving cars per hour

    int enterSpeed = 2; // number of cars that can enter per minute
    int paymentSpeed = 7; // number of cars that can pay per minute
    int exitSpeed = 5; // number of cars that can leave per minute    
    int maxLength = 5; // Zodra er meer dan 'x' aantal autos in de rij staan zullen ze doorrijden 
    final int speedPerExit = 5;
    final int speedPerEntrance = 2;
    
    private String[] dayArray = new String[7];

    
    /**
     * CONSTRUCTOR
     */
    public Simulator() { 

        abonnementhoudersPlekken = new int[][]{
            {1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0}
        };
        
        paymentReservationQueue = new CarQueue();
        entranceCarQueue = new CarQueue();
        entrancePassQueue = new CarQueue();
        entranceResQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();
        
        soundPlayer = new SoundPlayer();

        model = new Model();
        simulatorView = new SimulatorView(3, 6, 30, this, model);
        simulatorView.start();
        lineView = new LineView(model, this);
        lineView.start();
        PieView = new PieView(model, this);
        PieView.start();
        barView = new BarView(model, this);
        statView = new StatView(model, this);
        controller = new Controller(this);
        
        screen = new JFrame("Line View");
        screen.setSize(825, 747);
        screen.setResizable(false);
        screen.setLayout(null);

        screen.add(simulatorView);
        screen.add(controller);
        screen.add(lineView);
        screen.add(PieView);
        screen.add(barView);
        screen.add(statView);
        
        Insets insets = screen.getInsets();
        simulatorView.setBounds(insets.left, insets.top, 838, 500);
        controller.setBounds(insets.left, 500 + insets.top, 261, 1000);
        lineView.setBounds(490 + insets.left, 500 + insets.top, 230, 100);
        PieView.setBounds(720 + insets.left, 500 + insets.top, 100, 100);
        barView.setBounds(490 + insets.left, 600 + insets.top, 330, 1000);
        statView.setBounds(261 + insets.left, 500 + insets.top, 230, 1000);
        screen.setVisible(true);   
        
        toegestaanVoorAbonnementhouders = new int[simulatorView.getNumberOfFloors()][simulatorView.getNumberOfRows()][simulatorView.getNumberOfPlaces()];  
           
    }
    
    
    /**
     * TODO HIER JUISTE COMMENT NEERZETTEN
     */
    public void init(){
    	isRunning = true;
    }

    
    /**
     * 
     */
    public void run() 
    {
        while (tickCount < 100000)
        {
        	tick();
        	if (isRunning)
        	tickCount++;
        }
    }
    
    public void tickFor(int amount){
    	for( int i = 0; i < amount; i++ ) {
    		manualStep();
    	}
    }
    
    
    /**
     * 
     */
    void manualStep() 
    {

		//Als er een dag voorbij is reset alle stats die per dag bijgehouden wordt. 
		if( hour == 0 && minute == 0 ) {
			if(day % 7 == 0 && day > 0) {
				resetStats("week");
			}
			resetStats("dag");	
		}
		
		// Maak een 3D-array aan waarin alle beschikbare locaties voor abonnementhouders worden geselecteerd
		makeParkingMap();
		
		// Regel de drukte aan de hand van evenementen, nachtelijke uren of uren overdag.
		setCarArrivals();
		
		// Tel 1 minuut bij de tijd op en verdeel alles in minuten, uren en dagen
		advanceTime();
		
		// Check welke auto's weg mogen en fix deze shit
		handleExit();
		
		// Bereken aantal plekken enzo (moet hier gebeuren want na updateViews() is het telaat en loopt hij een minuut achter elke keer.
        totalCarsToday = nonPassCarsNow + passCarsNowWithoutReservedSpot + passCarsNowWithReservedSpot + reservedCarsNow;
		
		// Vernieuw 
		updateViews();
		
    	((LineView) lineView).addRectangle();
		lineView.startX++;
		model.notifyViews();
		//totaalGeparkeerd = geparkeerdeZonderAbonnement + geparkeerdeAbonnementhouders;
		
		simulatorView.setCarsParked();
		
		if(simulatorView.getNumberOfOpenSpots() > 70){
		    handleEntrance();
		}
		else{
        	//Speel een geluidsbestand wanneer de garage vol is.
        	if(playingSound == false && soundLastPlayed != hour){
        		soundPlayer.startPlaying("src/parkeergarage/sounds/alarm.mp3");
        		soundLastPlayed = hour;
        	}
			
		}
    }

    
	/**
	 * ROB
	 * Onderstaande functie maakt een 3D array(verdieping, rij, plaats) om aan te geven waar een abonnementhouder mag parkeren en waar niet
	 */ 
    public void makeParkingMap() {
    	
    	// i = de voor de verdiepingen, j is voor de rijen en k is voor de plaatsen
    	int i = 0, j = 0, k = 0;
    	
    	// Bij elke plaats die vrijgehouden wordt wordt abo-- gedaan. Om ervoor te zorgen dat de abonnementHoudersPlekken variabele niet aagepast wordt wordt er een kopie gemaakt
    	int abo = abonnementHoudersPlekken;
    	
    	// Als er minder abonnementHouders zijn dan abonnementHoudersPlekken dan hoeven niet alle abonnementHoudersPlekken bezet te worden gehouden voor de abonnementhouders. Deze plekken worden immers toch niet gebruikt
    	if( abonnementHoudersPlekken >= abonnementHouders ) {
    		abo = abonnementHouders;
    	}
    	
    	// Loop door alle verdieping
    	for(i = 0; i < simulatorView.getNumberOfFloors(); i++) {
    		// Loop door alle rijen
    		for(j = 0; j < simulatorView.getNumberOfRows(); j++) {
    			// Loop door alle plaatsen
    			for(k = 0; k < simulatorView.getNumberOfPlaces(); k++) {

    				// Zolang er nog abo plekken moet worden vrijgehouden en er nog plekken te reserveren zijn mag hij in deze 'if' statement gaan.
    				if(abo != 0 && abonnementhoudersPlekken[i][j] == 1){
    						toegestaanVoorAbonnementhouders[i][j][k] = 1;
    						abo--;
    				
    				// Als al het bovenste niet waar is dan is het sowieso geen aboplek en wordt er een '0' neergezet om aan te geven dat hier niet geparkeerd mag worden	
    				} else {
    					toegestaanVoorAbonnementhouders[i][j][k] = 0;
    				}
    			}
    		}
    	}
    }
    
    
    /**
     * ROB
     * reset elke dag de daginkomen en aantalgeparkeerde autos. Een keer per week reset hij de inkomsten van het parkeren met een abonnement 
     * @param dagOfWah, dit kan 'dag' of 'week' zijn.
     */
    void resetStats(String dayOrWeek) {
    	
    	// Reset alles voor de dag
        if( dayOrWeek == "dag" ) {
        	incomeNonPassHoldersPerDay = 0;
        	incomePassHoldersPerDay = 0;
        	passCarsTodayWithoutReservedSpot = 0;
        	passCarsTodayWithReservedSpot = 0;
        	nonPassCarsToday = 0;
        	reservedCarsToday = 0;
        	incomeReservationPerDay = 0;
        	carsPassedToday = 0;
        	
        // Reset alles van de week
    	} else if( dayOrWeek == "week" ){
    		if(abonnementHouders < abonnementHoudersPlekken) {
            	incomePassHoldersPerWeek = passHoldersCostPerWeek * abonnementHouders;
    		} else {
            	incomePassHoldersPerWeek = passHoldersCostPerWeek * abonnementHoudersPlekken;
    		}
    		incomePassHoldersPerDay += incomePassHoldersPerWeek;
    	
    		// Geef foutcode. Hij mag eigenlijk nooit in onderstaande else-statement komen.
    	} else {
    		//System.out.println("ERROR: resetStats. Verkeerde string!!");
    	}
    }
    
    
    /**
     * Onderstaande gaat per minuut steeds overal bijlangs wat nodig is.
     */
    void tick() 
    {
    	if (isRunning)
    	{
	    	// Als er een dag voorbij is reset alle stats die per dag bijgehouden wordt. 
	    	if( hour == 0 && minute == 0 ) {
	    		if(day % 7 == 0 && day > 0) {
	    			resetStats("week");
	    		}
	    		resetStats("dag");	
	    	}
	    	
	        // Maak een 3D-array aan waarin alle beschikbare locaties voor abonnementhouders worden geselecteerd
	        makeParkingMap();
	
	        // Regel de drukte aan de hand van evenementen, nachtelijke uren of uren overdag.
	        setCarArrivals();
	    
	        // Tel 1 minuut bij de tijd op en verdeel alles in minuten, uren en dagen
	        advanceTime();
	        
	        // Check welke auto's weg mogen en fix deze shit
	        handleExit();
	        
	        // Bereken aantal plekken enzo (moet hier gebeuren want na updateViews() is het telaat en loopt hij een minuut achter elke keer.
	        totalCarsToday = nonPassCarsNow + passCarsNowWithoutReservedSpot + passCarsNowWithReservedSpot + reservedCarsNow;
	        
	        // Vernieuw 
	        updateViews();
	        
	    	lineView.startX++;
	    	model.notifyViews();
	    	//totaalGeparkeerd = geparkeerdeZonderAbonnement + geparkeerdeAbonnementhouders;
	    	
	        simulatorView.setCarsParked();
	
	        // Pause.
	        try {
	            
	            Thread.sleep(tickPause);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        
	        setPlayingSound();
	        
	       if(simulatorView.getNumberOfOpenSpots() > 20){
	            handleEntrance();
	        }
	        else{
	        	//Speel een geluidsbestand wanneer de garage vol is.
	        	if(playingSound == false && soundLastPlayed != hour){
	        		soundPlayer.startPlaying("src/parkeergarage/sounds/alarm.mp3");
	        		soundLastPlayed = hour;
	        	}
	        }
    	}
    }
    
    public void toggleRunning()
    {
    	isRunning = !isRunning;
    }
    public void toggleParkOnNotReservedSpot()
    {
    	parkingOnNotReservedSpot = !parkingOnNotReservedSpot;
    }

    
    /**
     * Gegeven functie
     * Deze functie telt 1 minuut bij het geheel op en verdeelt dit dan in minuten, uren en dagen
     */
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
        displayDay();

    }
    
    
    /**
     * RUTGER
     * Deze functie zorgt ervoor dat er geen tijden zoals 12:0 wordt weergeven maar als 12:00
     */
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
    
    private void displayDay() {
    	dayArray[0]= "Monday";
    	dayArray[1]= "Tuesday";
    	dayArray[2]= "Wednesday";
    	dayArray[3]= "Thursday";
    	dayArray[4]= "Friday";
    	dayArray[5]= "Saturday";
    	dayArray[6]= "Sunday";
    	simulatorView.day.setText("Day: " + dayArray[getDay()]);
    }

    
    /**
     * 
     */
    private void handleEntrance(){
    	// Onderstaande aanroep zorgt voor een random toestroom van auto's met en zonder abonnement. Er wordt gekeken naar of het weekend is of niet. De nieuw toegestroomde auto's komen in queue. Autos met abonnement in de entrancePassQueue en zonder in de entranceCarQueue
    	carsArriving();

    	// De verschillende autos worden op de juiste locatie geplaatst
    	carsEntering(entranceResQueue);
    	carsEntering(entrancePassQueue);
    	carsEntering(entranceCarQueue);  
    }
    
    
    /**
     * 
     */
    private void handleExit(){
        carsReadyToLeave();
        carsPaying();
        carsLeaving();
    }
    
    
    /**
     * 
     */
    private void updateViews(){
        simulatorView.tick();
        // Update the car park view.
        simulatorView.updateView(); 
    }
    
    
    /**
     * 
     */
    private void carsArriving(){

    	// In onderstaande variablele wordt de hoeveelheid auto's random berekend voor mensen zonder een abonnement. Programma kijkt zelf al naar of het weekend is of niet
        int numberOfCars = getNumberOfCars(weekDayResArrivals, weekendResArrivals);
        
        // De nieuwe auto's die bij de ingang staan worden aan een queue toegevoegd met wachtende auto's om te parkeren.
        addArrivingCars(numberOfCars, RES);
        
        // In onderstaande variablele wordt de hoeveelheid auto's random berekend voor mensen zonder een abonnement. Programma kijkt zelf al naar of het weekend is of niet
        numberOfCars=getNumberOfCars(weekDayArrivals, weekendArrivals);
        
        // De nieuwe auto's die bij de ingang staan worden aan een queue toegevoegd met wachtende auto's om te parkeren.
        addArrivingCars(numberOfCars, AD_HOC);      
	        
        // Hieronder wordt opnieuw een random getal berekend voor binnenkomende auto's, maar dan voor mensen met en abonnement
        numberOfCars=getNumberOfCars(weekDayPassArrivals, weekendPassArrivals);
    	
        // De nieuwaangeschoven auto's met abonnement komen in een queue te staan om de parkeergarage binnen te treden.
        if( !(passCarsNowWithReservedSpot + passCarsNowWithoutReservedSpot + numberOfCars > abonnementHouders) ){
    		addArrivingCars(numberOfCars, PASS);    
	    }

    }
 
    
    /**
     * Onderstaande functie zet auto's weg in de parkeergarege. Er wordt gekeken naar welke rijen gereserveerd zijn voor de 
     * abonnementhouders. De gereserveerde plekken mogen alleen de abonnementhouders gebruiken maar mocht het zo zijn dat de
     * gereserveerde plekken op zijn dan worden de abonnementhouders op een niet gereserveerde plaats geplaatst. 
     *  * @param queue
     */
    private void carsEntering(CarQueue queue) {
        int i = 0;

        // Remove car from the front of the queue and assign to a parking space.
        while( queue.carsInQueue() > 0 && simulatorView.getNumberOfOpenSpots() > 0 && i < enterSpeed ) {
            
            // Haal een auto uit de queue en sla deze op in het type Car

            Car car = queue.removeCar();
            
            // De rij met abonnementhouders kunnen parkeren op de gereserveerde plekken.
            if(queue == entranceResQueue){
                Location freeLocation = simulatorView.getFirstFreeLocation(toegestaanVoorAbonnementhouders);
                
                // Plaats de auto op de gevonde vrije locatie
                simulatorView.setCarAt(freeLocation, car);
                
                // Standaard te betalen voor reserveringen
                incomeReservationPerDay += startTarief;
                incomeReservationTotal += startTarief;
                
                // Onderstaande 3 variabelen zijn nodig voor de stats
                reservedCarsNow++;
                reservedCarsToday++;
                reservedCarsTotal++;
            }
            
            // De rij met abonnementhouders kunnen parkeren op de gereserveerde plekken.
            else if( queue == entrancePassQueue && simulatorView.getFirstFreeLocationPass(toegestaanVoorAbonnementhouders) != null && toegestaanVoorAbonnementhouders[simulatorView.getFirstFreeLocationPass(toegestaanVoorAbonnementhouders).getFloor()][simulatorView.getFirstFreeLocationPass(toegestaanVoorAbonnementhouders).getRow()][simulatorView.getFirstFreeLocationPass(toegestaanVoorAbonnementhouders).getPlace()] == 1 ) {
                Location freeLocation = simulatorView.getFirstFreeLocationPass(toegestaanVoorAbonnementhouders);

                // Plaats de auto op de gevonden vrije locatie
                simulatorView.setCarAt(freeLocation, car);
                
                // Onderstaande 3 variabelen zijn nodig voor de stats
                passCarsNowWithReservedSpot++;
                passCarsTodayWithReservedSpot++;
                passCarsTotalWithReservedSpot++;
                
            }
            
            //De andere auto's en auto's met abonnement maar zonder plek kunnen op de eerst volgende plekken parkeren.
            else {
               
            	// Omdat de overgebleven abonnementhouders ook hier kunnen komen moet er even gecheckt worden of het wel om een niet abonnementhouder gaat voor de stats
                if( queue == entranceCarQueue ) {
                    
                	// Onderstaande is nodig voor de stats
                    nonPassCarsNow++;
                    nonPassCarsToday++;
                    nonPassCarsTotal++;
                   
                    // Kijk naar de eerst volgende vrije locatie voor een niet abonnementhouder
                    Location freeLocation = simulatorView.getFirstFreeLocation(toegestaanVoorAbonnementhouders);
                   
                    // Plaats de auto op de gevonde vrije locatie
                    simulatorView.setCarAt(freeLocation, car);
                }
                
                // 
                if( queue == entrancePassQueue ) {
                    if( parkingOnNotReservedSpot ) {
                        passCarsNowWithoutReservedSpot++;
                        passCarsTodayWithoutReservedSpot++;
                        passCarsTotalWithoutReservedSpot++;
                        
                        // Kijk naar de eerst volgende vrije locatie voor een niet abonnementhouder
                        Location freeLocation = simulatorView.getFirstFreeLocation(toegestaanVoorAbonnementhouders);
                        
                        // Plaats de auto op de gevonde vrije locatie
                        simulatorView.setCarAt(freeLocation, car);
                    } 
                }
            }
            i++;           
        }
    }
    
    
    /**
     * 
     */
    private void carsReadyToLeave(){
        
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
                nonPassCarsNow--;
            }
            else if(car.getTypeOfCar() == "reservation"){
                
            	paymentReservationQueue.addCar(car);
                
                reservedCarsNow--;
                carLeavesSpot(car);
                
            }
            
            // Als het een abonnementhouder is dan mag hij direct weg zonder in de rij te staan voor het betalen of voor het verlaten. 
            else {

        		// Kijkt of de abo auto op een gereserveerde plek staat of niet en dan voor de stats die teller aanpassen
                if(toegestaanVoorAbonnementhouders[car.getLocation().getFloor()][car.getLocation().getRow()][car.getLocation().getPlace()] == 1) {
                    passCarsNowWithReservedSpot--;
                } else if (toegestaanVoorAbonnementhouders[car.getLocation().getFloor()][car.getLocation().getRow()][car.getLocation().getPlace()] == 0) {
                    passCarsNowWithoutReservedSpot--;
                } 
            	incomePassHoldersPerDay += passHoldersCostPerMinute * car.getMinutesParked();
            	incomePassHoldersTotal += passHoldersCostPerMinute * car.getMinutesParked();
                
                carLeavesSpot(car);
            }
            car = simulatorView.getFirstLeavingCar();
        }
    }
        

    private void carsPaying(){
        // Let cars pay.
        int i=0;
        while ( (paymentReservationQueue.carsInQueue() > 0 || paymentCarQueue.carsInQueue() > 0) && i < paymentSpeed){
        	Car car;
        	
        	if( paymentCarQueue.carsInQueue() > 0 ){
        		car = paymentCarQueue.removeCar();
                incomeNonPassHoldersPerDay += nonPassHoldersCostPerMinute * car.getMinutesParked();
                incomeNonPassHoldersTotal += nonPassHoldersCostPerMinute * car.getMinutesParked();
                carLeavesSpot(car);
                i++;
        	} else if (paymentReservationQueue.carsInQueue() > 0 && i < paymentSpeed ){
        		car = paymentReservationQueue.removeCar();
                incomeReservationPerDay += reservationCostPerMinute * car.getMinutesParked();
                incomeReservationTotal += reservationCostPerMinute * car.getMinutesParked();
                i++;
        	}

        }
    }
    
    
    /**
     * Zolang de exitspeed niet is overschreden zullen er auto's uit de queue worden gehaald totdat er geen autos meer uit hde queue te halen zijn
     */
    private void carsLeaving(){
        
    	// Let cars leave.
        int i=0;
        while (exitCarQueue.carsInQueue()>0 && i < exitSpeed){
            exitCarQueue.removeCar();
            i++;
        }   
    }
    
    
    /**
     * 
     * @param weekDay
     * @param weekend
     * @return
     */
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
    
    
    //GAAT NOG IETS FOUT MET HET IN DE QUEUE LATEN VAN AUTOS DIE GRAAG WILLEN PARKEREN MET ABO MAAR DAT ER DAN GEEN RUIMTE IS OFZO
    private void addArrivingCars(int numberOfCars, String type){
        // Add the cars to the back of the queue.
            
            switch(type) {
                case AD_HOC: 
                    
                    // Auto's zonder abonnement wordt bijgehouden hoeveel tijd ze geparkeerd staan om vervolgens te berekenen hoeveel ze moeten betalen. hasToPay wordt naar true gezet
                    for (int i = 0; i < numberOfCars; i++) {
                        if(entranceCarQueue.carsInQueue() > maxLength){
                        	carsPassedToday++; 
                        	carsPassedTotal++;
                        }
                        else {
                        	if( busyHour() ){
                                entranceCarQueue.addCar(new AdHocCar(timeForBusyHourStaying));
                            }
                            entranceCarQueue.addCar(new AdHocCar());
                        }
                    }
                    break;
                    
                case PASS:
                    
                    // Auto's zonder abonnement hoeven niet bij het uitrijden direct te betalen maar betalen een vast bedrag per x aantal dagen. hasToPau wordt bij hun op false gezet
                    for (int i = 0; i < numberOfCars; i++) {
                        if(entrancePassQueue.carsInQueue() > maxLength){
                        	carsPassedToday++; 
                        	carsPassedTotal++;
                        }
                        else{
                            entrancePassQueue.addCar(new ParkingPassCar());
                        }
                    }
                    break;
                    
                case RES:
                    
                    // Auto's zonder abonnement hoeven niet bij het uitrijden direct te betalen maar betalen een vast bedrag per x aantal dagen. hasToPau wordt bij hun op false gezet
                    for (int i = 0; i < numberOfCars; i++) {
                        if(entranceResQueue.carsInQueue() < 6 && busyHour() != true){
                       	 	entranceResQueue.addCar(new ReservationPlace());
                        } else if(timeBetweenHourAndBusyhour > timeForReservationDuringBusyHour && entranceResQueue.carsInQueue() < 6){
                    	   entranceResQueue.addCar(new ReservationPlace()); 
                        }                     
                    }
                    break;
                    
               default:
            	   System.out.println("ERROR_35");
            } 
        }

    
    /**
     * 
     * @param car
     */
    private void carLeavesSpot(Car car){
        // Auto  locatie komt helemaal op NULL te staan en wordt toegevoegd aan het exitCarQueue. De auto staat dus hierna in de rij om het gebouw te verlaten
        simulatorView.removeCarAt(car.getLocation());
        exitCarQueue.addCar(car);
    }
    
    
    /**
     * @return busyHour (true or false)
     */
    private Boolean busyHour()
    {
        int busyday = 0;
        int busyhour = 0;
        int tillbusyday = 0;
        int tillbusyhour = 0;
        int[][] datas = new int[][]
        {
        	//De tijden wanneer het extra druk is
			{1, 7, 1, 17}, //Donderdagavond van 8 tot 17 uur: Market
			{3, 18, 3, 21}, //Donderdagavond van 18 tot 21 uur: shopping night
			{4, 18, 4, 24}, //vrijdagavond van 18 tot 24 uur
			{5, 18, 5, 24}, //zaterdagavond van 18 tot 24 uur
			{6, 7, 6, 17}, //zondagmiddag van 8 tot 17 uur  
        };
          //Het event wat bij de bovenstaande uren hoort
        String[] events = new String[]
        {"Market","shopping night", "Theatre", "Theatre", "Market"};
        for(int i = 0; i<datas.length; i++)
        {
            busyday = datas[i][0];
            busyhour = datas[i][1];
            tillbusyday = datas[i][2];
            tillbusyhour = datas[i][3];
            event = events[i];
            if(day > busyday && day < tillbusyday)
            {
                return true;
            } 
            else if (day == busyday && hour >= busyhour && day < tillbusyday)
            {
                return true;
            } 
            else if (day == busyday && hour >= busyhour && hour <= tillbusyhour)
            {
                calculateTimeStaying(tillbusyhour);
                return true;
            } 
            else 
            {
            	
            }       
        }
        calculateTimeStaying(0);
        event = "Nothing Special";
        return false;
    }
    
    
    /**
     * 
     * @param etime
     */
    private void calculateTimeStaying(int time){
    	if(time > 0){
    	timeBetweenHourAndBusyhour = ((time - hour) * 60);
    	timeForBusyHourStaying = timeForReservationDuringBusyHour - 60;
    	simulatorView.setTimeForReservation(timeForBusyHourStaying); }
    } 
    
    
     /**
     * Regelt de drukte aan de hand van evenementen, nachtelijke uren of uren overdag.
     */
    private void setCarArrivals(){
        if(busyHour()){//drukte tijdens de drukke uren.
            weekDayArrivals = 175;
            weekendArrivals = 350;
            weekDayPassArrivals = 50;
            weekendPassArrivals = 50;
            weekDayResArrivals = 25;
            weekendResArrivals = 50;
        }
        else if(hour >= nightHourStart || hour <= nightHourEnd){//drukte in de nachtelijke uren.
            weekDayArrivals = 25;
            weekendArrivals = 100;
            weekDayPassArrivals = 25;
            weekendPassArrivals = 25;
            weekDayResArrivals = 0;
            weekendResArrivals = 0;
        }
        else{//drukte in de normale situatie.
            weekDayArrivals = 90;
            weekendArrivals = 190;
            weekDayPassArrivals = 90;
            weekendPassArrivals = 25;
            weekDayResArrivals = 0;
            weekendResArrivals = 0;
        }
    }
	
    public void extraIngang() {
    	enterSpeed+=speedPerEntrance;
    }
    
    public void extraUitgang() {
    	exitSpeed+=speedPerExit;
    }
    
    public void extraIngangVerwijderen() {
    	if(enterSpeed > 0) {
    		enterSpeed-=speedPerEntrance;
    	}
    }
    
    public void extraUitgangVerwijderen() {
    	if(exitSpeed > 0) {
    		exitSpeed-=speedPerExit;
    	}
    }
    
    public int getNumberOfPlaces() {
    	return simulatorView.getNumberOfFloors()*simulatorView.getNumberOfRows()*simulatorView.getNumberOfPlaces();
    }
    
    public int getTotalEntranceQueue()
    {
    	return entranceCarQueue.carsInQueue() + entrancePassQueue.carsInQueue();
    }
    
    public int getTotalExitQueue()
    {
    	return exitCarQueue.carsInQueue();
    }
    
    public int estimatedIncomeParkedCars() {
    	return nonPassCarsNow * nonPassHoldersCostPerMinute * 45;
    }
    
    /**
     * Set method welke doorgeeft of er een notificatie wordt afgespeeld.
     */
    private void setPlayingSound(){
    	playingSound = soundPlayer.getIsPlaying();
    }
    
    public int getDay()
    {
    	return day;
    }
    
    public void incrementTickPause() {
    	if(tickPause > 1) {
    		tickPause = tickPause/10;
    	}
    }
    
    public void decrementTickPause() {
    	if(tickPause < 1000)
    	tickPause = tickPause*10;
    }
    
    public int returnTickPause() {
    	return tickPause;
    }
    
    public void incrementPassHolders() {
    	if(abonnementHouders < 540) {
        	abonnementHouders += 5;
    	}
    }
    
    public void decrementPassHolders() {
    	if(abonnementHouders > 0) {
    		abonnementHouders -= 5;
    	}
    }
    
    public int getAbonnementHouders()
    {
    	return abonnementHouders;
    }
}
