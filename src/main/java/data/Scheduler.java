package data;

import java.util.ArrayList;

public class Scheduler {
	
	private static ArrayList<CalendarItem> calandarItems;
	
	static {
		calandarItems = new ArrayList<>();
	}
	
	public void addCalandarItem(CalendarItem newItem) {
		calandarItems.add(newItem);
	}
	
	

}
