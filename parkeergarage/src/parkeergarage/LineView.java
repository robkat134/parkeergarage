package parkeergarage;

import java.awt.*;
import java.util.ArrayList;

public class LineView extends View {
	
	Simulator simulator;
	int max;
	int min = 0;
	int startX = 50;
	ArrayList<Rect> line = new ArrayList<Rect>();
	public LineView(Model model, Simulator Tempsimulator) {
		super(model);
		simulator = Tempsimulator;
	}
	
	public void paintComponent(Graphics g) {
		System.out.println("painting");
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 1000, 100);
		g.setColor(Color.BLUE);
		System.out.println("paint component word aangeroepen");
		g.fillRect(10, 0, 1, 100);
		startX++;
		line.add(new Rect(startX,(int)(simulator.AantalVrijePlekken/5.4), 1, (int)(simulator.TotaalAantalPlekken-simulator.AantalVrijePlekken/5.4)));
		System.out.println(line.size());
		for(int i=0;i<line.size();i++)
		{
			g.fillRect(line.get(i).x1,line.get(i).y1,line.get(i).x2,line.get(i).y2); 
		}
	}	
}