/*package parkeergarage;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.*;

public class GraphView extends JFrame{
	
	public GraphView() {
	}
	private class CarParkView extends JPanel {
		public void paintComponent(Graphics g) {
            if (carParkImage == null) {
                return;
            }
    
            Dimension currentSize = getSize();
            if (size.equals(currentSize)) {
                g.drawImage(carParkImage, 0, 0, null);
            }
            else {
                // Rescale the previous image.
                g.drawImage(carParkImage, 0, 0, currentSize.width, currentSize.height, null);
            }
        }
	}
}*/