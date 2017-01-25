package parkeergarage;

import javax.swing.*;

@SuppressWarnings("serial")
public class View extends JPanel
{
 private Model model;
 protected int max;
 protected int min = 0;
  
 	public View(Model model)
 	{
	 	this.model=model;
	 	model.addView(this);
	 	setSize(200,200);
	 	setVisible(true);
 	}
  
 	public void setModel(Model model)
 	{
 		this.model=model;
 	}
  
 	public Model getModel()
 	{
 		return model;
 	}
 	
 	public void updateView()
 	{
 	repaint();
 	}
 	 
 	public void initStats(int maximum)
 	{
 		max = maximum;
 	}
 //
}