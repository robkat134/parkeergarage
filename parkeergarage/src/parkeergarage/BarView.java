package parkeergarage;

import java.awt.*;
import java.util.ArrayList;

import javax.sound.sampled.Line;

public class BarView extends View {
	
	Simulator simulator;
	ArrayList<Rect> line = new ArrayList<Rect>();
	public BarView(Model model, Simulator Tempsimulator) {
		super(model);
		simulator = Tempsimulator;
		initBars();
	}
	public void paintComponent(Graphics g) 
	{
		g.setColor(Color.decode("#627c58"));
		g.fillRect(0, 0, 330, 100);
		
		g.setColor(Color.ORANGE);
		for(int i=0;i<line.size();i++)
		{
			g.fillRect(line.get(i).x1,line.get(i).y1,line.get(i).x2,line.get(i).y2); 
		}
	}
	public void initBars() 
	{
		line.add(0,new Rect(0,100-30, 20, 30));
		line.add(1,new Rect(25,100-100, 20, 100));
		line.add(2,new Rect(50,100-50, 20, 50));
		line.add(3,new Rect(75,100-70, 20, 70));
		line.add(4,new Rect(100,100-40, 20, 40));
		line.add(5,new Rect(125,100-35, 20, 35));
		line.add(6,new Rect(150,100-89, 20, 89));
	}	
}

