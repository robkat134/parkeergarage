package parkeergarage;

import java.awt.*;

public class LineView extends View {
	
	int max;
	int min = 0;
	
	public LineView(Model model, Simulator simulator) {
		super(model);
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, 200, 200);
		g.setColor(Color.GRAY);
	}
	
	
}