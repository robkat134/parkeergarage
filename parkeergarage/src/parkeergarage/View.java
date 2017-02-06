package parkeergarage;

import javax.swing.*;

@SuppressWarnings("serial")
public class View extends JPanel implements Runnable
{
 private Model model;
 protected int max;
 protected int min = 0;
 protected int startX;
 private Thread t;
 
 	public void start () {
 		if (t == null){
 			t = new Thread (this);
 			t.start ();
     	}
 	}
 	public void run() {
 	}
 
 	public View(Model model){
	 	this.model=model;
	 	model.addView(this);
	 	setSize(200,200);
	 	setVisible(true);
 	}
  
 	public void setModel(Model model){
 		this.model=model;
 	}
  
 	public Model getModel(){
 		return model;
 	}
 	
 	public void updateView(){
 		repaint();
 	}
 	
 	public void sleepThread(){
 		System.out.println(this.getClass());
 		try{
    		Thread.sleep(5000);
    	}
    	catch (InterruptedException e){	
    	}
 	}
}