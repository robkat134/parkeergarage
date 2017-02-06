package parkeergarage;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;

public class StatView extends View {
		
	Simulator simulator;
	private JLabel entranceQueue = new JLabel("entrance queue: ");
	private JLabel exitQueue = new JLabel("exit queue: ");
	private JLabel incomePassTotal = new JLabel("income passholders: €");
	private JLabel incomeNonPassTotal = new JLabel("income nonpassholders: €");
	private JLabel incomeResTotal = new JLabel("income reservations: €");
	private JLabel estimatedIncomePresentCars = new JLabel("estimated income: €");
    private JLabel event = new JLabel("event: ");
    private JLabel enterSpeed = new JLabel("enterspeed: ");
    private JLabel exitSpeed = new JLabel("exitspeed: ");
    private JLabel passHolders = new JLabel("passholders: ");
	
	public StatView(Model model, Simulator Tempsimulator) 
	{
		super(model);
		simulator = Tempsimulator;

		setLayout(null);
		add(entranceQueue);
		add(exitQueue);
		add(enterSpeed);
		add(exitSpeed);
		add(incomePassTotal);
		add(incomeNonPassTotal);
		add(incomeResTotal);
		add(estimatedIncomePresentCars);
		add(event);
		add(passHolders);
		entranceQueue.setBounds(5, 0, 300, 20);
		exitQueue.setBounds(5, 20, 300, 20);
		enterSpeed.setBounds(5, 40, 300, 20);
		exitSpeed.setBounds(5, 60, 300, 20);
		incomePassTotal.setBounds(5, 80, 300, 20);
		incomeNonPassTotal.setBounds(5, 100, 300, 20);
		incomeResTotal.setBounds(5, 120, 300, 20);
		estimatedIncomePresentCars.setBounds(5, 140, 300, 20);
		event.setBounds(5, 160, 300, 20);
		passHolders.setBounds(5, 180, 300, 20);
	}
	
	public void paintComponent(Graphics g) 
	{
		setStats();
		
		g.setColor(Color.decode("#bfbfbf"));
		g.fillRect(0, 0, 229, 1000);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 229, 1);	
		g.fillRect(228, 0, 1, 1000);
	}
	
	public void setStats()
	{
		entranceQueue.setText("entrance queue: " + simulator.getTotalEntranceQueue());
		exitQueue.setText("exit queue: " + simulator.getTotalExitQueue());
		event.setText("event: "+simulator.event);
		enterSpeed.setText("enterspeed: " + simulator.enterSpeed);
		exitSpeed.setText("exitspeed: " + simulator.exitSpeed);
		passHolders.setText("passholders: " + simulator.getAbonnementHouders());
		if (simulator.incomePassHoldersTotal%100 == 0)
		{
			incomePassTotal.setText("income passholders: €" + simulator.incomePassHoldersTotal/100+",00");
		}
		else
		{
			incomePassTotal.setText("income passholders: €" + simulator.incomePassHoldersTotal/100+"," + simulator.incomePassHoldersTotal%100);
		}
		if (simulator.incomeNonPassHoldersTotal%100 == 0)
		{
			incomeNonPassTotal.setText("income nonpassholders: €" + simulator.incomeNonPassHoldersTotal/100+",00");
		}
		else
		{
			incomeNonPassTotal.setText("income nonpassholders: €" + simulator.incomeNonPassHoldersTotal/100+"," + simulator.incomeNonPassHoldersTotal%100);
		}
		if (simulator.incomeReservationTotal%100 == 0)
		{
			incomeResTotal.setText("income reservations: €" + simulator.incomeReservationTotal/100+",00");
		}
		else
		{
			incomeResTotal.setText("income reservations: €" + simulator.incomeReservationTotal/100+"," + simulator.incomeReservationTotal%100);
		}
		if (simulator.estimatedIncomeParkedCars()%100 == 0)
		{
			estimatedIncomePresentCars.setText("estimated income: €" + simulator.estimatedIncomeParkedCars()/100+",00");
		}
		else
		{
			estimatedIncomePresentCars.setText("estimated income: €" + simulator.estimatedIncomeParkedCars()/100+"," + simulator.estimatedIncomeParkedCars()%100);
		}
	}
}