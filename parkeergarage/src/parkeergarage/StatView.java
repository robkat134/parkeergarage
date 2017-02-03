package parkeergarage;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;

public class StatView extends View {
		
	Simulator simulator;
	private JLabel entranceQueue = new JLabel("entrance queue: ");
	private JLabel incomePassTotal = new JLabel("income passholders: ");
	private JLabel incomeNonPassTotal = new JLabel("income nonpassholders: ");
	private JLabel incomeResTotal = new JLabel("income reservations: ");
	private JLabel estimatedIncomePresentCars = new JLabel("estimated income: ");
	
	public StatView(Model model, Simulator Tempsimulator) 
	{
		super(model);
		simulator = Tempsimulator;
		
		setSize(210, 300);
		setLayout(null);
		add(entranceQueue);
		add(incomePassTotal);
		add(incomeNonPassTotal);
		add(incomeResTotal);
		add(estimatedIncomePresentCars);
		entranceQueue.setBounds(0, 0, 200, 20);
		incomePassTotal.setBounds(0, 20, 200, 20);
		incomeNonPassTotal.setBounds(0, 40, 200, 20);
		incomeResTotal.setBounds(0, 60, 200, 20);
		estimatedIncomePresentCars.setBounds(0, 80, 210, 20);
	}
	
	public void paintComponent(Graphics g) 
	{
		setStats();
		
		g.setColor(Color.decode("#EEEEEE"));
		g.fillRect(0, 0, 210, 300);
	}
	
	public void setStats()
	{
		entranceQueue.setText("entrance queue: " + simulator.getTotalEntranceQueue());
		//incomePassTotal.setText("income passholders: " + simulator.incomePassHoldersTotal);
		//incomeNonPassTotal.setText("income nonpassholders: " + simulator.incomeNonPassHoldersTotal);
		//incomePassTotal.setText("income reservations: " + simulator.incomeReservationTotal);
		if (simulator.estimatedIncomeParkedCars()%100 == 0)
		{
			estimatedIncomePresentCars.setText("estimated income: " + simulator.estimatedIncomeParkedCars()/100+",00");
		}
		else
		{
			estimatedIncomePresentCars.setText("estimated income: " + simulator.estimatedIncomeParkedCars()/100+"," + simulator.estimatedIncomeParkedCars()%100);
		}
	}
}