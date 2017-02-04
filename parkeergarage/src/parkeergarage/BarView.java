package parkeergarage;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.sound.sampled.Line;
import javax.swing.JLabel;

public class BarView extends View {
	
	Simulator simulator;
	ArrayList<Rect> line = new ArrayList<Rect>();
	private int day;
	private int previousDay;
	private int[] week = new int[7];
	private float[] perdayProcent = new float[7];
	private float total;
	private JLabel monday = new JLabel("mon");
	private JLabel tuesday = new JLabel("tue");
	private JLabel wednesday = new JLabel("wed");
	private JLabel thursday = new JLabel("thu");
	private JLabel friday = new JLabel("fri");
	private JLabel saturday = new JLabel("sat");
	private JLabel sunday = new JLabel("sun");
	
	private JLabel highestLabel = new JLabel("");
	private JLabel highestLabel2 = new JLabel("");
	private JLabel highestLabel3 = new JLabel("");

	
	public BarView(Model model, Simulator Tempsimulator) {
		super(model);
		simulator = Tempsimulator;
		
		setLayout(null);
		add(monday);
		add(tuesday);
		add(wednesday);
		add(thursday);
		add(friday);
		add(saturday);
		add(sunday);
		add(highestLabel);
		add(highestLabel2);
		add(highestLabel3);
		monday.setBounds(10, 85, 30, 10);
		tuesday.setBounds(42, 85, 30, 10);
		wednesday.setBounds(68, 85, 30, 10);
		thursday.setBounds(102, 85, 30, 10);
		friday.setBounds(133, 85, 30, 10);
		saturday.setBounds(160, 85, 30, 10);
		sunday.setBounds(190, 85, 30, 10);
		highestLabel.setBounds(225, 5, 100, 10);
		highestLabel2.setBounds(225, 42, 100, 10);
		highestLabel3.setBounds(225, 75, 100, 10);
	}
	public void paintComponent(Graphics g) 
	{
		initBars();
		g.setColor(Color.decode("#bfbfbf"));
		g.fillRect(0, 0, 330, 100);
		g.setColor(Color.BLACK);
		g.fillRect(0, 80, 220, 1);
		g.fillRect(0, 0, 330, 1);
		g.fillRect(220, 0, 1, 100);
		g.setColor(Color.ORANGE);
		for(int i=0;i<line.size();i++)
		{
			g.fillRect(line.get(i).x1,line.get(i).y1,line.get(i).x2,line.get(i).y2); 
		}
	}
	public void initBars() 
	{
		float highest = 0;
		total = 0;
		day = simulator.getDay();
		if (previousDay == 6 && day == 0)
		{
			for (int i = 0; i < week.length; i++) 
			{
				perdayProcent[i] = 0;
				week[i]=0;
			}
			total = 0;
		}
		week[day]= simulator.incomeNonPassHoldersPerDay + simulator.incomePassHoldersPerDay + simulator.incomeReservationPerDay;
		for (int i = 0; i < week.length; i++) 
		{
			total += week[i];
		}
		for (int i = 0; i < week.length; i++) 
		{
			perdayProcent[i] = (float)(week[i]/total);
			if( highest < perdayProcent[i] ){
				highest = perdayProcent[i];
			}
		}
		
		highestLabel.setText(""+(int)(highest*100)+"%");
		highestLabel2.setText(""+(int)(highest*100/2)+"%");
		highestLabel3.setText("0%");
		System.out.println(perdayProcent[0]);


		line.clear();
		line.add(0,new Rect( 10, 80 - (int)(75 * perdayProcent[0]/highest), 20, (int)(perdayProcent[0]*75/highest) ));
		line.add(1,new Rect( 40, 80 - (int)(75 * perdayProcent[1]/highest), 20, (int)(perdayProcent[1]*75/highest) ));
		line.add(2,new Rect( 70, 80 - (int)(75 * perdayProcent[2]/highest), 20, (int)(perdayProcent[2]*75/highest) ));
		line.add(3,new Rect( 100, 80 - (int)(75 * perdayProcent[3]/highest), 20, (int)(perdayProcent[3]*75/highest) ));
		line.add(4,new Rect( 130, 80 - (int)(75 * perdayProcent[4]/highest), 20, (int)(perdayProcent[4]*75/highest) ));
		line.add(5,new Rect( 160, 80 - (int)(75 * perdayProcent[5]/highest), 20, (int)(perdayProcent[5]*75/highest) ));
		line.add(6,new Rect( 190, 80 - (int)(75 * perdayProcent[6]/highest), 20, (int)(perdayProcent[6]*75/highest) ));
		previousDay = day;
	}	
}


