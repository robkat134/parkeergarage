package parkeergarage;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class LineView extends View {
	
	Simulator simulator;
	ArrayList<Rect> line = new ArrayList<Rect>();
	ArrayList<Rect> line2 = new ArrayList<Rect>();
	ArrayList<Rect> line3 = new ArrayList<Rect>();
	
	private JLabel UpperLimit = new JLabel("540");
	private JLabel MiddleLimit = new JLabel("270");
	private JLabel LowerLimit = new JLabel("0");
	
	
	
	public LineView(Model model, Simulator Tempsimulator) 
	{
		super(model);
		simulator = Tempsimulator;
		setLayout(null);
		//setSize(50,50);
		//setBounds(0,0,200,200);
//		JPanel graphInfo = new JPanel();
//		graphInfo.setLayout(new BorderLayout());
//		graphInfo.setSize(40, 80);
//		graphInfo.setBounds(200, 0, 40, 100);
		add(UpperLimit,BorderLayout.NORTH);
		UpperLimit.setBounds(205, 0, 200, 26);
		add(MiddleLimit,BorderLayout.CENTER);
		MiddleLimit.setBounds(205, 0, 200, 96);
		add(LowerLimit,BorderLayout.SOUTH);
		LowerLimit.setBounds(205, 0, 200, 166);
        //add(graphInfo,BorderLayout.EAST);
	}
	public void paintComponent(Graphics g) 
	{
		initGraph(g);
		addRectangle();
		
		g.setColor(Color.RED);
		for(int i=0;i<line.size();i++)
		{
			g.fillRect(line.get(i).x1,line.get(i).y1,line.get(i).x2,line.get(i).y2); 
		}
		g.setColor(Color.BLUE);
		for(int i=0;i<line2.size();i++)
		{
			g.fillRect(line2.get(i).x1,line2.get(i).y1,line2.get(i).x2,line2.get(i).y2); 
		}
		g.setColor(Color.BLACK);
		for(int i=0;i<line3.size();i++)
		{
			g.fillRect(line3.get(i).x1,line3.get(i).y1,line3.get(i).x2,line3.get(i).y2); 
		}
	}
	/**
	 * @param g
	 */
	private void initGraph(Graphics g) {
		g.setColor(Color.decode("#bfbfbf"));
		g.fillRect(0, 0, 230, 100);
		g.setColor(Color.BLACK);
		g.fillRect(201, 0, 1, 100);
//		g.fillRect(0, 0, 1, 100);
		g.fillRect(0, 0, 230, 1);
		g.fillRect(230, 0, 1, 100);
		g.fillRect(0, 100, 230, 1);
		g.setColor(Color.decode("#D6D6D6"));
		g.fillRect(0, 15, 201, 1);
		g.fillRect(0, 50, 201, 1);
		g.fillRect(0, 85, 201, 1);
	}
	public void addRectangle() 
	{
		line.add(0,new Rect(startX,85 -(int)(simulator.nonPassCarsNow/7.2), 2, 2));
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
		line2.add(0,new Rect(startX,85 -(int)((simulator.passCarsNowWithReservedSpot + simulator.passCarsNowWithReservedSpot)/7.2), 2,2));
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
		line3.add(0,new Rect(startX,85 -(int)((simulator.passCarsNowWithoutReservedSpot+simulator.passCarsNowWithReservedSpot + simulator.nonPassCarsNow)/7.2), 2,2));
		if (line3.size()>200)
		{
			for(int i=0;i<line3.size();i++)
			{
				line3.get(i).x1--;
			}
			line3.remove(line3.size()-1);
			startX = 199;
			//startX--;
		}
	}	
}

