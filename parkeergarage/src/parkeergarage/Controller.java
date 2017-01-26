package parkeergarage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

//@SuppressWarnings("serial")
public class Controller extends JPanel implements ActionListener {
	private Simulator simulator;
	private JButton plus1;
    private JButton plus100;
    private JButton run;
    
	public Controller(Simulator simulator) {
		this.simulator=simulator;
		
		setSize(200, 200);
		plus1 =new JButton("+1");
		plus1.addActionListener(this);
		plus100 =new JButton("+100");
		plus100.addActionListener(this);
		run =new JButton("run");
		run.addActionListener(this);
		
		add(plus1);
		add(plus100);
		add(run);
		plus1.setBounds(50, 10, 70, 30);
		plus100.setBounds(140, 10, 70, 30);
		run.setBounds(229, 10, 70, 30);

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
	}
}