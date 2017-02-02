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
    
	public Controller(Simulator simulator) {
		this.simulator=simulator;
		
		setSize(200, 200);
		plus1 =new JButton("+1");
		plus1.addActionListener(this);
		plus100 =new JButton("+100");
		plus100.addActionListener(this);
		run =new JButton("run");
		run.addActionListener(this);
		extraUitgang = new JButton("extra uitgang");
		extraUitgang.addActionListener(this);
		extraIngang = new JButton("extra ingang");
		extraIngang.addActionListener(this);
		setLayout(null);
		add(plus1);
		add(plus100);
		add(run);
		add(extraUitgang);
		add(extraIngang);
		plus1.setBounds(10, 10, 70, 30);
		plus100.setBounds(85, 10, 70, 30);
		run.setBounds(160, 10, 70, 30);
		extraUitgang.setBounds(10, 70, 120, 30);
		extraIngang.setBounds(135, 70, 120, 30);;
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
			simulator.start();
		}
		if(e.getSource() == extraUitgang)
		{
			simulator.start();
		}
		if(e.getSource() == extraIngang)
		{
			simulator.start();
		}
	}
}
