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
		plus1.setBounds(10, 10, 70, 30);
		plus100.setBounds(85, 10, 70, 30);
		run.setBounds(160, 10, 95, 30);
		week.setBounds(85, 45, 80, 30);
		day.setBounds(10, 45, 70, 30);
		faster.setBounds(105, 87, 90, 30);
		slower.setBounds(10, 87, 90, 30);
		addExit.setBounds(10, 130, 100, 30);
		addEntrance.setBounds(115, 130, 100, 30);
		removeExit.setBounds(10, 165, 100, 30);
		removeEntrance.setBounds(115, 165, 100, 30);
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
	}
	
	private void graphGrid(Graphics g) {
		g.setColor(Color.decode("#bfbfbf"));
		g.fillRect(0, 0, 260, 400);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 260, 1);
		g.fillRect(0, 80, 260, 1);
		g.fillRect(0, 125, 260, 1);
		g.fillRect(260, 0, 1, 400);
	}
	
	public void paintComponent(Graphics g) {
		graphGrid(g);
	}
}
