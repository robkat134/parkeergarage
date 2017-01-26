package parkeergarage;

import java.awt.*;

public class LineView extends View {
	
	Simulator simulator;
	int max;
	int min = 0;
	int startX = 50;
	//Rectangle[] line = new Rectangle[100];
	public LineView(Model model, Simulator Tempsimulator) {
		super(model);
		simulator = Tempsimulator;
	}
	
	public void paintComponent(Graphics g) {
		System.out.println("painting");
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 100, 100);
		g.setColor(Color.BLUE);
		System.out.println("paint component word aangeroepen");
		g.fillRect(startX,(int)(simulator.AantalVrijePlekken), 1, simulator.TotaalAantalPlekken-simulator.AantalVrijePlekken);
		//System.out.println("lijn: "+line[0]);
		startX++;
		//line[line.length] = new Rectangle(startX + line.length,(int)(simulator.AantalVrijePlekken), 1, 1);
	}	
}