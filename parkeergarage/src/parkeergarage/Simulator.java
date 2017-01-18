package parkeergarage;

import java.util.Random;

public class Simulator {

	private static final String AD_HOC = "1";
	private static final String PASS = "2";
	

	private boolean isRunning = false;

    private int abonnementhouders = 10;
    private int geparkeerdeAbonnementhouders = 0;
    private int geparkeerdeZonderAbonnement = 0;
    private int totaalGeparkeerd = 0;
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

    private int day = 0;
    private int hour = 0;
    private int minute = 0;
    
    private int tickCount = 0;

    private int tickPause = 1;

    int weekDayArrivals= 100; // average number of arriving cars per hour
    int weekendArrivals = 200; // average number of arriving cars per hour
    int weekDayPassArrivals= 50; // average number of arriving cars per hour
    int weekendPassArrivals = 5; // average number of arriving cars per hour

    int enterSpeed = 3; // number of cars that can enter per minute
    int paymentSpeed = 7; // number of cars that can pay per minute
    int exitSpeed = 5; // number of cars that can leave per minute

    public Simulator() {
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
    	System.out.println(isRunning);
        while (isRunning && tickCount < 1440)
        {
        	System.out.println(tickCount);
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
    	handleEntrance();
    }
    void tick() 
    {
    	advanceTime();
    	handleExit();
    	updateViews();
    	totaalGeparkeerd = geparkeerdeZonderAbonnement + geparkeerdeAbonnementhouders;
    	AantalVrijePlekken = TotaalAantalPlekken - totaalGeparkeerd;
    	System.out.println("Zonder abo: " + geparkeerdeZonderAbonnement);
 		System.out.println("Met abo: " + geparkeerdeAbonnementhouders);
 		System.out.println("Totaal geparkeerd: " + totaalGeparkeerd);
 		System.out.println("Totaal aantal vrije plekken:  " + AantalVrijePlekken);
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
        System.out.println("Dag: "+ day);
        System.out.println("tijd " + hour + " : "+ minute );

    }

    private void handleEntrance(){
    	carsArriving();
    	carsEntering(entrancePassQueue);
    	carsEntering(entranceCarQueue);  	
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
    	int numberOfCars=getNumberOfCars(weekDayArrivals, weekendArrivals);
        addArrivingCars(numberOfCars, AD_HOC);    	
    	numberOfCars=getNumberOfCars(weekDayPassArrivals, weekendPassArrivals);
        addArrivingCars(numberOfCars, PASS);    	
    }

    private void carsEntering(CarQueue queue){
        int i=0, keepLoop = 0;
        // Remove car from the front of the queue and assign to a parking space.
    	while (queue.carsInQueue()>0 && 
    		   simulatorView.getNumberOfOpenSpots()>0 && 
    	       i<enterSpeed) 
    	{
            Car car = queue.removeCar();
            // De rij met abonnementhouders kunnen parkeren op de gereserveerde plekken.
            if(queue == entrancePassQueue && geparkeerdeAbonnementhouders < abonnementhouders && abonnementhouders != 0)
            {
            	Location freeLocation = simulatorView.getFirstFreeLocationPass();
            	simulatorView.setCarAt(freeLocation, car);
            	++geparkeerdeAbonnementhouders;
            	passCarsNow++;
	            if(hour == 0 && minute == 0 && keepLoop == 0)
	            {
	            	passCarsToday = 0;
	            	keepLoop = 1;
	            }
	            passCarsToday++;
            }
            //De andere auto's kunnen op de eerst volgende plekken parkeren.
            else if (queue == entranceCarQueue)
            {
                Location freeLocation = simulatorView.getFirstFreeLocation(abonnementhouders);
                simulatorView.setCarAt(freeLocation, car);
                ++geparkeerdeZonderAbonnement;
                nonPassCarsNow++;
	            if(hour == 0 && minute == 0 && keepLoop == 0){
	            	nonPassCarsToday = 0;
	            	keepLoop = 1;
	            }
	            nonPassCarsToday++;
            }
            i++;
            int allPassCarsToday = nonPassCarsToday + passCarsToday;
            int allCarsNow = nonPassCarsNow + passCarsNow;
            //test voor aantal auto's
            System.out.println("Alle gepasseerde auto's "+allPassCarsToday);
        }
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
            	entranceCarQueue.addCar(new AdHocCar());
            }
            break;
    	case PASS:
            for (int i = 0; i < numberOfCars; i++) {
            	entrancePassQueue.addCar(new ParkingPassCar());
            }
            break;	            
    	}
    }
    
    private void carLeavesSpot(Car car){
    	simulatorView.removeCarAt(car.getLocation());
        exitCarQueue.addCar(car);
    }

}
