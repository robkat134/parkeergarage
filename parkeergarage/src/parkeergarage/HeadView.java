package parkeergarage;

import java.awt.Graphics;
import javax.swing.*;

/*
 * deze class word
 */

public class HeadView extends View {
	public JLabel parkedCars = new JLabel("Parked Cars: ");
    public JLabel time = new JLabel("Time: ");
    private Simulator simulator;
    
    public HeadView(Model model, Simulator tempSimulator) {
    	super(model);
    	simulator = tempSimulator;
    	
    	setLayout(null);
    	setSize(838, 50);
    	
    	add(parkedCars);
    	add(time);
    	parkedCars.setBounds(300, 0, 200, 40);
    	time.setBounds(500, 0, 200, 40);
    }
    
    public void updateHeadView(){
    	parkedCars.setText("Parked cars: "+simulator.totalCarsToday);
    }
    
    public void paintComponent(Graphics g) {
    	updateHeadView();
    }
}
