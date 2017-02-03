package parkeergarage;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
    private JButton extraUitgang;
    private JButton extraIngang;
    private JButton extraUitgangVerwijderen;
    private JButton extraIngangVerwijderen;
    private JButton sneller;
    private JButton trager;
    
	public Controller(Simulator simulator) {
		this.simulator=simulator;
		
		setSize(200, 400);
		plus1 =new JButton("+1");
		plus1.addActionListener(this);
		plus100 =new JButton("+100");
		plus100.addActionListener(this);
		run =new JButton("play/pause");
		run.addActionListener(this);
		extraUitgang = new JButton("+uitgang");
		extraUitgang.addActionListener(this);
		extraIngang = new JButton("+ingang");
		extraIngang.addActionListener(this);
		extraUitgangVerwijderen = new JButton("-uitgang");
		extraUitgangVerwijderen.addActionListener(this);
		extraIngangVerwijderen = new JButton("-ingang");
		extraIngangVerwijderen.addActionListener(this);
		sneller = new JButton("sneller");
		sneller.addActionListener(this);
		trager = new JButton("trager");
		trager.addActionListener(this);
		setLayout(null);
		add(plus1);
		add(plus100);
		add(run);
		add(extraUitgang);
		add(extraIngang);
		add(extraUitgangVerwijderen);
		add(extraIngangVerwijderen);
		add(sneller);
		add(trager);
		plus1.setBounds(10, 10, 70, 30);
		plus100.setBounds(85, 10, 70, 30);
		run.setBounds(160, 10, 95, 30);
		sneller.setBounds(105, 45, 90, 30);
		trager.setBounds(10, 45, 90, 30);
		extraUitgang.setBounds(10, 120, 100, 30);
		extraIngang.setBounds(115, 120, 100, 30);
		extraUitgangVerwijderen.setBounds(10, 155, 100, 30);
		extraIngangVerwijderen.setBounds(115, 155, 100, 30);
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
		if(e.getSource() == run)
		{
			simulator.toggleRunning();
		}
		if(e.getSource() == sneller)
		{
			simulator.incrementTickPause();
			System.out.println("tickpause: " + simulator.returnTickPause());
		}
		if(e.getSource() == trager)
		{
			simulator.decrementTickPause();
			System.out.println("tickpause: " + simulator.returnTickPause());
		}
		if(e.getSource() == extraUitgang)
		{
			simulator.extraUitgang();
			System.out.println("uitgangen: "+simulator.exitSpeed);
		}
		if(e.getSource() == extraIngang)
		{
			simulator.extraIngang();
			System.out.println("ingangen: "+simulator.enterSpeed);
		}
		if(e.getSource() == extraUitgangVerwijderen)
		{
			simulator.extraUitgangVerwijderen();

			System.out.println("uitgangen: "+simulator.exitSpeed);
		}
		if(e.getSource() == extraIngangVerwijderen)
		{
			simulator.extraIngangVerwijderen();
			System.out.println("ingangen: "+simulator.enterSpeed);
		}
	}
}
