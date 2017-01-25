package parkeergarage;

import java.security.acl.Owner;
//import java.util.Random;

import java.util.*;
//import java.util.LinkedHashMap;
//import java.util.Map.Entry;

import javax.swing.JLabel;

public class Simulator {

	private static final String AD_HOC = "1";
	private static final String PASS = "2";
	
	//HashMap<Integer, Integer, Integer> drukkeUren = new HashMap<>();
	int[][][] drukkeUren = new int[10][][];
	private boolean isRunning = false;

    private int abonnementhouders = 90;
    private int geparkeerdeAbonnementhouders = 0;
    private int geparkeerdeZonderAbonnement = 0;
    public int totaalGeparkeerd = 0;
    private int AantalVrijePlekken = 540;
    private int TotaalAantalPlekken = 540;
	
    int timeToStay = 0;
    int timeToStayBusy = 15;
    
	private CarQueue entranceCarQueue;
    private CarQueue entrancePassQueue;
    private CarQueue paymentCarQueue;
    private CarQueue exitCarQueue;
    private SimulatorView simulatorView;
    //private GraphView graphView;
    private int carsInQueueAd_hoc = 0;
    
    private int passCarsNow = 0; // auto's met abonnement die er geweest zijn vanaf starten programma.
    private int passCarsToday = 0; // auto's met abonnement die geteld worden tot het einde van de dag (1440 minuten).
    private int nonPassCarsNow = 0; // auto's zonder abonnement die er geweest zijn vanaf starten programma.
    private int nonPassCarsToday = 0; // auto's zonder abonnement die geteld worden tot het einde van de dag (1440 minuten).

    private int day = 0;
    private int hour = 0;
    private int minute = 0;
    private boolean busytime = false;
    private int tickCount = 0;

    private int tickPause = 1000;


    int weekDayArrivals= 100; // average number of arriving cars per hour
    int weekendArrivals = 200; // average number of arriving cars per hour
    int weekDayPassArrivals= 50; // average number of arriving cars per hour
    int weekendPassArrivals = 5; // average number of arriving cars per hour

    int enterSpeedCar = 1; //max number of free cars that can enter 
    int enterSpeedPass = 1; //max number of pass cars that can enter 
    int enterSpeedCarCount = 0; 
    int enterSpeedPassCount = 0;
    int paymentSpeed = 2; // number of cars that can pay per minute
    int exitSpeed = 4; // number of cars that can leave per minute

    public Simulator() {
        entranceCarQueue = new CarQueue();
        entrancePassQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();
        simulatorView = new SimulatorView(3, 6, 30, this);
        //graphView = new GraphView();
        
    }
    public void init(){
    	updateViews();
    	isRunning = true;
    }

    public void run() 
    {
    	//System.out.println(isRunning);
        while (isRunning && tickCount < 1440)
        {
        	//System.out.println(tickCount);
        	tick();
        	tickCount++;
        	if (tickCount >= 1000)
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
    void tick() 
    {
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
    	//System.out.println(isRunning);
    	run();
    }

    private void setEnterSpeed(){
    enterSpeedCarCount = 0;
	enterSpeedPassCount = 0;
    }
    
    private void advanceTime(){
        // Advance the time by one minute.
        minute++;
        setEnterSpeed();
		System.out.println(busyHour());
		System.out.println(day);
		
        while (minute > 59) {
            minute -= 60;
            hour++;
            busyHour();
            handleExitFast();
        }
        while (hour > 23) {
            hour -= 24;
            day++;
        }
        while (day > 6) {
            day -= 7;
        }
        displayTime();
       // System.out.println("Dag: "+ day);
      //  System.out.println("tijd " + hour + " : "+ minute );

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
    	carsArriving();
    	if(entrancePassQueue.carsInQueue() > 0 && enterSpeedPassCount < enterSpeedPass){
    		carsEntering(entrancePassQueue);
    		enterSpeedPassCount++;
    		//System.out.println("Count: "+enterSpeedPassCount);
    	}if (entranceCarQueue.carsInQueue() > 0 && enterSpeedCarCount < enterSpeedCar) {
    		carsEntering(entranceCarQueue);  	
    	    enterSpeedCarCount++;
    	}
    	int allCarsToday = nonPassCarsToday + passCarsToday;
        int allCarsNow = nonPassCarsNow + passCarsNow;
    	
        //System.out.println("Alle gepasseerde auto's vandaag: "+allCarsToday);
       // System.out.println("Alle gepasseerde auto's van start: "+allCarsNow);
    }
    
    private void handleExit(){
        carsReadyToLeave();
        carsPaying();
        carsLeaving();
    }
    private void handleExitFast(){
    for(int i = 0 ; i < totaalGeparkeerd; i++ ){
    	carsReadyToLeave();
        carsPaying();
        carsLeaving();
    }
   }
    
    private void updateViews(){
    	simulatorView.tick();
        // Update the car park view.
        simulatorView.updateView();	
    }
    
    private void carsArriving(){
    	int numberOfCars=getNumberOfCars(weekDayArrivals, weekendArrivals);
        addArrivingCars(numberOfCars, AD_HOC);    	
    	numberOfCars=getNumberOfCars(weekDayPassArrivals, weekendPassArrivals);
        addArrivingCars(numberOfCars, PASS);   
    }

    private void carsEntering(CarQueue queue){
        int keepLoop = 0;
        // Remove car from the front of the queue and assign to a parking space.
    	if(simulatorView.getNumberOfOpenSpots()>0)
    	{
            Car car = queue.removeCar();
            
            if(hour == 0 && minute == 0 && keepLoop == 0)
            {
            	passCarsToday = 0;
            	nonPassCarsToday = 0;
            	keepLoop = 1;
            }
            // De rij met abonnementhouders kunnen parkeren op de gereserveerde plekken.
            if(queue == entrancePassQueue && geparkeerdeAbonnementhouders < abonnementhouders) // && abonnementhouders != 0
            {
            	//setCarsInQueue("Pass", false);
            	getCarsInQueue();
            	Location freeLocation = simulatorView.getFirstFreeLocationPass();
            	simulatorView.setCarAt(freeLocation, car);
            	++geparkeerdeAbonnementhouders;
            	passCarsNow++;
	            passCarsToday++;
            }
            //De andere auto's kunnen op de eerst volgende plekken parkeren.
            else if (queue == entranceCarQueue)
            {
            	//setCarsInQueue("Car", false);
            	getCarsInQueue();
                Location freeLocation = simulatorView.getFirstFreeLocation(abonnementhouders);
                simulatorView.setCarAt(freeLocation, car);
                ++geparkeerdeZonderAbonnement;
                nonPassCarsNow++;
	            nonPassCarsToday++;
            }
    	}
            //test voor aantal auto's
    }
    
    private void carsReadyToLeave(){
        // Add leaving cars to the payment queue.
        Car car = simulatorView.getFirstLeavingCar();
        while (car!=null) {
        	if (car.getHasToPay()){
	            car.setIsPaying(true);
	            paymentCarQueue.addCar(car);
	            --geparkeerdeZonderAbonnement;
        	}
        	else {
        		carLeavesSpot(car);
        		geparkeerdeAbonnementhouders--;
        	}
            car = simulatorView.getFirstLeavingCar();
        }
    }
        

    private void carsPaying(){
        // Let cars pay.
    	int i=0;
    	while (paymentCarQueue.carsInQueue()>0 && i < paymentSpeed){
            Car car = paymentCarQueue.removeCar();
            // TODO Handle payment.
            carLeavesSpot(car);
            i++;
    	}
    }
    
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
            	for (int i = 0; i < numberOfCars; i++) {
            		if(busyHour()){
            			entranceCarQueue.addCar(new AdHocCar(busyHour(), timeToStayBusy));}
            		else{
            			entranceCarQueue.addCar(new AdHocCar(busyHour(), timeToStay));}
                	getCarsInQueue();
            }
            break;
    	case PASS:
            for (int i = 0; i < numberOfCars; i++) {
            	entrancePassQueue.addCar(new ParkingPassCar(busyHour()));
            	getCarsInQueue();
            }
            break;	    
            
      
    	}
    }
    
	private void carLeavesSpot(Car car){
    	simulatorView.removeCarAt(car.getLocation());
        exitCarQueue.addCar(car);
    }
    
    private void getCarsInQueue(){
    	//System.out.println("Free auto's in de wachtrij : " +entranceCarQueue.carsInQueue());
    	//System.out.println("Pass auto's in de wachtrij : " +entrancePassQueue.carsInQueue());
    	
    }
    
    private Boolean busyHour(){
    	int busyday = 0;
    	int busyhour = 0;
    	int tillbusyday = 0;
    	int tillbusyhour = 0;
    	int[][] datas = new int[][]{
    		  {3, 18, 3, 21}, //zondagmiddag van 12 tot 18 uur
			  {4, 18, 4, 24}, //vrijdagavond van 18 tot 24 uur
			  {5, 18, 5, 24}, //vrijdagavond van 18 tot 24 uur
			  {6, 12, 6, 18}, //zondagmiddag van 12 tot 18 uur
			  
			};
			for(int i = 0; i<datas.length; i++){
				busyday = datas[i][0];
				busyhour = datas[i][1];
				tillbusyday = datas[i][2];
				tillbusyhour = datas[i][3];
					if(day > busyday && day < tillbusyday){
					    return true;
					} else if (day == busyday && hour >= busyhour && day < tillbusyday){
						return true;
					} else if (day == busyday && hour >= busyhour && hour <= tillbusyhour){
						calculateTimeStaying(tillbusyhour);
						return true;
						} else {}		
				}
			return false;
		}
    private int calculateTimeStaying(int time){
    	if(time > 0){
    		timeToStayBusy = (time - hour) * 60;
    	} else {
    		timeToStayBusy = 240;
    	}
    	return timeToStay;
    }
}


