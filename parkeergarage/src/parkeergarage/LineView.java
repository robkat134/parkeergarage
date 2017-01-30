package parkeergarage;

import java.awt.*;
import java.util.ArrayList;

import javax.sound.sampled.Line;

public class LineView extends View {
	
	Simulator simulator;
	ArrayList<Rect> line = new ArrayList<Rect>();
	ArrayList<Rect> line2 = new ArrayList<Rect>();
	public LineView(Model model, Simulator Tempsimulator) {
		super(model);
		simulator = Tempsimulator;
	}
	public void paintComponent(Graphics g) 
	{
		System.out.println("painting");
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, 1000, 100);
		
		g.setColor(Color.RED);
		System.out.println("paint component word aangeroepen");
		addRectangle();
		System.out.println(line.size());
		for(int i=0;i<line.size();i++)
		{
			g.fillRect(line.get(i).x1,line.get(i).y1,line.get(i).x2,line.get(i).y2); 
		}

		g.setColor(Color.BLUE);
		for(int i=0;i<line2.size();i++)
		{
			g.fillRect(line2.get(i).x1,line2.get(i).y1,line2.get(i).x2,line2.get(i).y2); 
		}
	}
	public void addRectangle() 
	{
		line.add(0,new Rect(startX,95 -(int)(simulator.geparkeerdeZonderAbonnement/5.4), 2, 2));
		System.out.println(line.size());
		if (line.size()>200)
		{
			for(int i=0;i<line.size();i++)
			{
				line.get(i).x1--;
			}
			line.remove(line.size()-1);
			startX = 200;
			//startX--;
		}
		line2.add(0,new Rect(startX,95 -(int)(simulator.geparkeerdeAbonnementhouders/5.4), 2,2));
		if (line2.size()>200)
		{
			for(int i=0;i<line2.size();i++)
			{
				line2.get(i).x1--;
			}
			line2.remove(line2.size()-1);
			startX = 200;
			//startX--;
		}
	}	
}

