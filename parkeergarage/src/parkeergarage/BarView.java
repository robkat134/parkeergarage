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
	}
	public void paintComponent(Graphics g) 
	{
		initBars();
		g.setColor(Color.decode("#EEEEEE"));
		g.fillRect(0, 0, 330, 100);
		g.setColor(Color.BLACK);
		g.fillRect(0, 80, 330, 1);
		g.fillRect(0, 0, 330, 1);
		g.fillRect(0, 0, 1, 100);
		g.setColor(Color.ORANGE);
		for(int i=0;i<line.size();i++)
		{
			g.fillRect(line.get(i).x1,line.get(i).y1,line.get(i).x2,line.get(i).y2); 
		}
	}
	public void initBars() 
	{
		//line.add(0,new Rect(5,(80-(simulator.moneyReceived()/10*8)), 20, (simulator.moneyReceived()/10*8)));
		line.add(0,new Rect(5,80-((int)simulator.moneyReceived()/10*8), 20, ((int)simulator.moneyReceived()/10*8)));
		line.add(1,new Rect(30,80-5, 20, 5));
		line.add(2,new Rect(55,80-7, 20, 7));
		line.add(3,new Rect(80,80-4, 20, 4));
		line.add(4,new Rect(105,80-3, 20, 3));
		line.add(5,new Rect(130,80-8, 20, 8));
		line.add(6,new Rect(155,80-8, 20, 8));
	}	
}

