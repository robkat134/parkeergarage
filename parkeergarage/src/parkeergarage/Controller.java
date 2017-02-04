package parkeergarage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

//@SuppressWarnings("serial")
public class Controller extends JPanel implements ActionListener {
	private Simulator simulator;
	private Model model;
	private JButton plus1;
    private JButton plus100;
    private JButton run;
    private JButton stop;
    private JButton day;
    private JButton week;
    private JButton addExit;
    private JButton addEntrance;
    private JButton removeExit;
    private JButton removeEntrance;
    private JButton faster;
    private JButton slower;
    private JButton toggleParkOnNotReservedSpot;
    private JButton addPassHolder;
    private JButton removePassHolder;
    
	public Controller(Simulator simulator) {
		this.simulator=simulator;
		
		setSize(260, 400);
		plus1 =new JButton("+1");
		plus1.addActionListener(this);
		plus100 =new JButton("+100");
		plus100.addActionListener(this);
		run =new JButton("play/pause");
		run.addActionListener(this);
		day = new JButton("1 day");
		day.addActionListener(this);
		week = new JButton("1 week");
		week.addActionListener(this);
		addExit = new JButton("+exit");
		addExit.addActionListener(this);
		addEntrance = new JButton("+entrance");
		addEntrance.addActionListener(this);
		removeExit = new JButton("-exit");
		removeExit.addActionListener(this);
		removeEntrance = new JButton("-entrance");
		removeEntrance.addActionListener(this);
		faster = new JButton("faster");
		faster.addActionListener(this);
		slower = new JButton("slower");
		slower.addActionListener(this);
		toggleParkOnNotReservedSpot = new JButton("free parking");
		toggleParkOnNotReservedSpot.addActionListener(this);
		addPassHolder = new JButton("+pass");
		addPassHolder.addActionListener(this);
		removePassHolder = new JButton("-pass");
		removePassHolder.addActionListener(this);
		setLayout(null);
		add(plus1);
		add(plus100);
		add(run);
		add(day);
		add(week);
		add(addExit);
		add(addEntrance);
		add(removeExit);
		add(removeEntrance);
		add(faster);
		add(slower);
		add(toggleParkOnNotReservedSpot);
		add(addPassHolder);
		add(removePassHolder);
		plus1.setBounds(10, 5, 70, 20);
		plus100.setBounds(85, 5, 70, 20);
		run.setBounds(160, 5, 95, 20);
		week.setBounds(85, 30, 80, 20);
		day.setBounds(10, 30, 70, 20);
		faster.setBounds(105, 55, 90, 20);
		slower.setBounds(10, 55, 90, 20);
		removeEntrance.setBounds(10, 145, 100, 20);
		addEntrance.setBounds(115, 145, 100, 20);
		removeExit.setBounds(10, 170, 100, 20);
		addExit.setBounds(115, 170, 100, 20);
		toggleParkOnNotReservedSpot.setBounds(10, 85, 110, 20);
		removePassHolder.setBounds(10, 115, 100, 20);
		addPassHolder.setBounds(115, 115, 100, 20);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e.getSource());
		System.out.println(simulator);
		if(e.getSource() == plus100)
		{
			simulator.tickFor(100);
		}
		if(e.getSource() == plus1)
		{
			simulator.tickFor(1);
		}
		if(e.getSource() == day)
		{
			simulator.tickFor(1440);
		}
		if(e.getSource() == week)
		{
			simulator.tickFor(10080);
		}
		if(e.getSource() == run)
		{
			simulator.toggleRunning();
		}
		if(e.getSource() == faster)
		{
			simulator.incrementTickPause();
			System.out.println("tickpause: " + simulator.returnTickPause());
		}
		if(e.getSource() == slower)
		{
			simulator.decrementTickPause();
			System.out.println("tickpause: " + simulator.returnTickPause());
		}
		if(e.getSource() == addExit)
		{
			simulator.extraUitgang();
			System.out.println("uitgangsnelheid: "+simulator.exitSpeed);
		}
		if(e.getSource() == addEntrance)
		{
			simulator.extraIngang();
			System.out.println("ingangsnelheid: "+simulator.enterSpeed);
		}
		if(e.getSource() == removeExit)
		{
			simulator.extraUitgangVerwijderen();

			System.out.println("uitgangsnelheid: "+simulator.exitSpeed);
		}
		if(e.getSource() == removeEntrance)
		{
			simulator.extraIngangVerwijderen();
			System.out.println("ingangsnelheid: "+simulator.enterSpeed);
		}
		if(e.getSource() == toggleParkOnNotReservedSpot)
		{
			simulator.toggleParkOnNotReservedSpot();
		}
		if(e.getSource() == addPassHolder)
		{
			simulator.incrementPassHolders();
		}
		if(e.getSource() == removePassHolder)
		{
			simulator.decrementPassHolders();
		}
	}
	
	private void graphGrid(Graphics g) {
		g.setColor(Color.decode("#bfbfbf"));
		g.fillRect(0, 0, 260, 400);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 260, 1);
		g.fillRect(0, 80, 260, 1);
		g.fillRect(0, 110, 260, 1);
		g.fillRect(0, 140, 260, 1);
		g.fillRect(260, 0, 1, 400);
	}
	
	public void paintComponent(Graphics g) {
		graphGrid(g);
	}
}
