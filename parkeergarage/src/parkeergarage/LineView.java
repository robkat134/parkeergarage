package parkeergarage;

import java.awt.*;

public class LineView extends View {
	
	public LineView(Model model) {
		super(model);
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, 200, 200);
		g.setColor(Color.GRAY);
	}
}