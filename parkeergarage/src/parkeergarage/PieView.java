package parkeergarage;

import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class PieView extends View {
	Simulator simulator;
	public PieView(Model model, Simulator tempSimulator) {
		super(model);
		simulator = tempSimulator;
	}

	public void paintComponent(Graphics g) {


		g.setColor(Color.GRAY);
		g.fillRect(890, 0, 100, 100);
		
		
		int startAngle = 0;
		double aantal=(simulator.geparkeerdeAbonnementhouders)/1.5;
		int intAantal = (int)aantal;
		g.setColor(Color.BLUE);
		g.fillArc(900, 10, 80, 80, startAngle, intAantal);
		
		startAngle += intAantal;
		double secondAantal = (simulator.geparkeerdeZonderAbonnement)/1.5;
		int intSecondAantal = (int)secondAantal;
		g.setColor(Color.RED);
		g.fillArc(900, 10, 80, 80, startAngle, intSecondAantal);	
		
		startAngle += intSecondAantal;
		double thirdAantal = (simulator.AantalVrijePlekken)/1.5;
		int intThirdAantal = (int)thirdAantal;
		g.setColor(Color.WHITE);
		g.fillArc(900, 10, 80, 80, startAngle, intThirdAantal);
	}	
}

