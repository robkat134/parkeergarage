package parkeergarage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.security.acl.Owner;
import javax.swing.*;
import java.util.Random;

public class Simulator implements Runnable{

	private Thread thread;
	private static final String AD_HOC = "1";
	private static final String PASS = "2";
	

	private boolean isRunning = false;

	protected int abonnementhouders = 80;
    protected int geparkeerdeAbonnementhouders = 0;
    protected int geparkeerdeZonderAbonnement = 0;
    protected int totaalGeparkeerd = 0;
    protected int AantalVrijePlekken = 0;
    protected int TotaalAantalPlekken = 540;
	
	private CarQueue entranceCarQueue;
    private CarQueue entrancePassQueue;
    private CarQueue paymentCarQueue;
    private CarQueue exitCarQueue;
    private SimulatorView simulatorView;
    private View lineView;
    private View PieView;
    private View barView;
    private Controller controller;
    private Model model;
    private JFrame screen;
    
    private int carsInQueueAd_hoc = 0;
    
    private int passCarsNow = 0; // auto's met abonnement die er geweest zijn vanaf starten programma.
    private int passCarsToday = 0; // auto's met abonnement die geteld worden tot het einde van de dag (1440 minuten).
    private int nonPassCarsNow = 0; // auto's zonder abonnement die er geweest zijn vanaf starten programma.
    private int nonPassCarsToday = 0; // auto's zonder abonnement die geteld worden tot het einde van de dag (1440 minuten).
    public int allCarsNow = 0;
    public int allCarsToday = 0;

    private int day = 0;
    public int hour = 0;
    private int minute = 0;
    
    private int tickCount = 0;

    private int tickPause = 10;


    int weekDayArrivals= 100; // average number of arriving cars per hour
    int weekendArrivals = 200; // average number of arriving cars per hour
    int weekDayPassArrivals= 50; // average number of arriving cars per hour
    int weekendPassArrivals = 5; // average number of arriving cars per hour

    int enterSpeed = 3; // number of cars that can enter per minute
    int paymentSpeed = 7; // number of cars that can pay per minute
    int exitSpeed = 5; // number of cars that can leave per minute

    public int totaalOntvangen = 0;
    public float totaalOntvangenFloat = 0.00f;
	//prijs per minuut in centen.
	private int prijsPerMinuut = 2;
	//aantal minuten dat een ad hoc auto binnen is geweest, wordt gebruikt om de prijs te berekenen.
	private int stayMinutes = 0;
	
    public Simulator() {
        entranceCarQueue = new CarQueue();
        entrancePassQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();
        model = new Model();
        simulatorView = new SimulatorView(3, 6, 30, this, model);
        lineView = new LineView(model, this);
        PieView = new PieView(model, this);
        barView = new BarView(model, this);
        controller = new Controller(this);
        
        screen = new JFrame("Line View");
        screen.setSize(838, 747);
        screen.setResizable(true);
        screen.setLayout(null);
        
        screen.add(simulatorView);
        screen.add(controller);
        screen.add(lineView);
        screen.add(PieView);
        screen.add(barView);
        
        Insets insets = screen.getInsets();
        simulatorView.setBounds(insets.left, insets.top, 820, 500);
        controller.setBounds(insets.left, 500 + insets.top, 600, 100);
        lineView.setBounds(490 + insets.left, 500 + insets.top, 230, 100);
        PieView.setBounds(720 + insets.left, 500 + insets.top, 100, 100);
        barView.setBounds(490 + insets.left, 600 + insets.top, 330, 100);
        //screen.getContentPane().add(simulatorView,BorderLayout.NORTH);
        //screen.getContentPane().add(controller,BorderLayout.SOUTH);
        //screen.getContentPane().add(lineView,BorderLayout.CENTER);
        //screen.getContentPane().add(PieView, BorderLayout.CENTER);
        //screen.getContentPane().add(barView, BorderLayout.CENTER);
        screen.setVisible(true);    
    }
    
    public void init(){
    	//updateViews();
    	isRunning = true;
    }

    public void run() 
    {
    	//System.out.println(isRunning);
        while (tickCount < 14400)
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
    	((LineView) lineView).addRectangle();
    	lineView.startX++;
    	model.notifyViews();
    	simulatorView.setCarsParked();
    	simulatorView.setInkomsten();
    	totaalGeparkeerd = geparkeerdeZonderAbonnement + geparkeerdeAbonnementhouders;
    	AantalVrijePlekken = TotaalAantalPlekken - totaalGeparkeerd;
    	handleEntrance();
    }
    void tick() 
    {
    	advanceTime();
    	handleExit();
    	updateViews();
    	lineView.startX++;
    	model.notifyViews();
    	simulatorView.setCarsParked();
    	simulatorView.setInkomsten();
    	totaalGeparkeerd = geparkeerdeZonderAbonnement + geparkeerdeAbonnementhouders;
    	AantalVrijePlekken = TotaalAantalPlekken - totaalGeparkeerd;
//    	simulatorView.parkedCars = new JLabel("Totaal geparkeerd: " + totaalGeparkeerd);
//    	System.out.println("Zonder abo: " + geparkeerdeZonderAbonnement);
// 		System.out.println("Met abo: " + geparkeerdeAbonnementhouders);
// 		System.out.println("Totaal geparkeerd: " + totaalGeparkeerd);
// 		System.out.println("Totaal aantal vrije plekken:  " + AantalVrijePlekken);
//      System.out.println("Huidige inkomsten: �"+totaalOntvangen);
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
    	carsEntering(entrancePassQueue);
    	carsEntering(entranceCarQueue);  	
        
    	allCarsToday = nonPassCarsToday + passCarsToday;
        allCarsNow = nonPassCarsNow + passCarsNow;
    	
//        System.out.println("Alle gepasseerde auto's vandaag: "+allCarsToday);
//        System.out.println("Alle gepasseerde auto's van start: "+allCarsNow);
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
        if(AD_HOC == "1"){
        	setCarsInQueueAd_hoc(1);
//        	System.out.println("Ad_hoc auto's in de wachtrij : " + getCarsInQueueAd_hoc());

        }
    }

    private void carsEntering(CarQueue queue){
        int i=0, keepLoop = 0;
        if (queue == entranceCarQueue){
        setCarsInQueueAd_hoc(-1);
//    	System.out.println("Ad_hoc auto's in de wachtrij : " + getCarsInQueueAd_hoc());
        }
        // Remove car from the front of the queue and assign to a parking space.
    	while (queue.carsInQueue()>0 && 
    		   simulatorView.getNumberOfOpenSpots()>0 && 
    	       i<enterSpeed) 
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
            	Location freeLocation = simulatorView.getFirstFreeLocationPass();
            	simulatorView.setCarAt(freeLocation, car);
            	++geparkeerdeAbonnementhouders;
            	passCarsNow++;
	            passCarsToday++;
            }
            //De andere auto's kunnen op de eerst volgende plekken parkeren.
            else if (queue == entranceCarQueue)
            {
                Location freeLocation = simulatorView.getFirstFreeLocation(abonnementhouders);
                simulatorView.setCarAt(freeLocation, car);
                ++geparkeerdeZonderAbonnement;
                nonPassCarsNow++;
	            nonPassCarsToday++;
            }
            i++;

            //test voor aantal auto's
           
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
            moneyReceived();
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
    private void setCarsInQueueAd_hoc(int number){
    	if(number > 0){
    		carsInQueueAd_hoc += 1;
    	} else {
    		carsInQueueAd_hoc -= 1;
    	}
    }
    
    private int getCarsInQueueAd_hoc(){
    	return carsInQueueAd_hoc;
    }
    
	
	public float moneyReceived(){
		stayMinutes = AdHocCar.getStayMinutes();
    	int ontvangen = prijsPerMinuut * stayMinutes;
        totaalOntvangen = totaalOntvangen + ontvangen;
        // totaalOntvangenFloat = (float) totaalOntvangen/100;
        return totaalOntvangen; //;Float;
	}
    
    public void start() {
    	if(thread==null) {
    		thread=new Thread(this);
    		thread.start();
    	}
    }
    
    public void extraUitgang() {
    	
    }
    
    public void extraIngang() {
    	
    }
}


