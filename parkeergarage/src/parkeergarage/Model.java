package parkeergarage;

import java.util.*;

public class Model {
	
	private List<View> views;
	public Model() {
		views =  new ArrayList<View>();
	}
	
	public void addView(View view) {
		views.add(view);
	}
	
	private void notifyViews() {
		for(View v: views) v.updateView();
	}
}