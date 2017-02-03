package parkeergarage;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.sound.sampled.Line;

public class BarView extends View {
	
	Simulator simulator;
	ArrayList<Rect> line = new ArrayList<Rect>();
	private int day;
	private int[] week = new int[7];
	private float[] perdayProcent = new float[7];
	private float total;
	
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
		total = 0;
		day = simulator.getDay();
		week[day]= simulator.incomeNonPassHoldersPerDay + simulator.incomePassHoldersPerDay + simulator.incomeReservationPerDay;
		for (int i = 0; i < week.length; i++) 
		{
			total += week[i];
		}
		for (int i = 0; i < week.length; i++) 
		{
			perdayProcent[i] = (float)(week[i]/total);
		}
		System.out.println(perdayProcent[0]);
		line.clear();
		line.add(0,new Rect(5,80, 20, (int)(-75 * perdayProcent[0])));
		line.add(1,new Rect(30,80, 20, (int)(-75 * perdayProcent[1])));
		line.add(2,new Rect(55,80, 20, (int)(-75 * perdayProcent[2])));
		line.add(3,new Rect(80,80, 20, (int)(-75 * perdayProcent[3])));
		line.add(4,new Rect(105,80, 20, (int)(-75 * perdayProcent[4])));
		line.add(5,new Rect(130,80, 20, (int)(-75 * perdayProcent[5])));
		line.add(6,new Rect(155,80, 20, (int)(-75 * perdayProcent[6])));
		line.add(7,new Rect(0,50, 100, -20));
	}	
}

