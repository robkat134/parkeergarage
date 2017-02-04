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


		g.setColor(Color.decode("#bfbfbf"));
		g.fillRect(0, 0, 100, 100);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 100, 1);
		
		
		int startAngle = 0;
		double aantal = (simulator.passCarsNowWithReservedSpot)/1.5;
		int intAantal = (int)aantal;
		g.setColor(Color.BLUE);
		g.fillArc(10, 10, 80, 80, startAngle, intAantal);
		
		startAngle += intAantal;
		double secondAantal = (simulator.passCarsNowWithoutReservedSpot)/1.5;
		int intSecondAantal = (int)secondAantal;
		g.setColor(Color.decode("#56a2ff"));
		g.fillArc(10, 10, 80, 80, startAngle, intSecondAantal);
		
		startAngle += intSecondAantal;
		double thirdAantal = (simulator.nonPassCarsNow)/1.5;
		int intThirdAantal = (int)thirdAantal;
		g.setColor(Color.RED);
		g.fillArc(10, 10, 80, 80, startAngle, intThirdAantal);	
		
		startAngle += intThirdAantal;
		double fourthAantal = ( simulator.getNumberOfPlaces()-(simulator.nonPassCarsNow + simulator.passCarsNowWithoutReservedSpot + simulator.passCarsNowWithReservedSpot) )/1.5;
		int intFourthAantal = (int)fourthAantal;
		g.setColor(Color.WHITE);
		g.fillArc(10, 10, 80, 80, startAngle, intFourthAantal);
	}	
}

