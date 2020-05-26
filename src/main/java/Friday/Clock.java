package Friday;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;

import com.twilio.TwilioHelper;

import data.Contacts;
import io.Logger;

/**
 * Singleton class used as Friday's clock. Keeps track of time and to schedule events at certain times.
 * @author Patrick Wamsley
 */
public class Clock implements Runnable {
	
	private Date currentTimestamp;
	private PriorityQueue<ScheduledFridayAction> scheduledActionsQueue;
//	private ReentrantLock queueLock;
	
	private static final Clock clock = new Clock(); 
	
	private Clock() {
		initQueue();
	}
	
	public static Clock getClock() {
		return clock;
	}
	
	@Override
	public void run() {
		currentTimestamp = new Date();
//		scheduledActionsQueue = new PriorityQueue<>();
//		queueLock = new ReentrantLock();
		
		while (true) {
			//update time value
			currentTimestamp.setTime(System.currentTimeMillis());
			
			//check the queue for next Action
//			queueLock.lock();
			
			if (scheduledActionsQueue.isEmpty()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			
			ScheduledFridayAction nextAction = scheduledActionsQueue.peek();
			if (nextAction.isCanceled()) {
				scheduledActionsQueue.poll();
				continue;
			}
			
			Date nextActionTime = nextAction.getScheduledTime();
			if (!currentTimestamp.before(nextActionTime)) {
				//remove the action from the queue, execute the action and send the response text
				TwilioHelper.sendText(Contacts.getMe(), scheduledActionsQueue.poll().execute());
				save(); //clears the poll'd scheduled action from the saved file
				//do not sleep as there may be another event scheduled for right now
//				queueLock.unlock();
			} else {
//				queueLock.unlock();
				
				//wait for the next second
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
 			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void initQueue() {
		try {
			FileInputStream fis = new FileInputStream(FridayProperties.getProperties().CLOCK_SAVE_PATH);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			scheduledActionsQueue = (PriorityQueue<ScheduledFridayAction>)ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (scheduledActionsQueue == null) {
				scheduledActionsQueue = new PriorityQueue<>();
			}
		}
	}
	
	public void addScheduleAction(ScheduledFridayAction action) {
//		queueLock.unlock();
//		if (scheduledActionsQueue == null) {
//			System.out.println("queue");
//		} else if (action == null) {
//			System.out.println(action);
//		}
		if (!action.isCanceled()) {
			scheduledActionsQueue.add(action);
			save();
		}
//		queueLock.lock();
	}
	
	private void save() {
		try {
			Logger.log("Attempting to save scheduled action queue.", this.getClass());
			FileOutputStream fos = new FileOutputStream(FridayProperties.getProperties().CLOCK_SAVE_PATH);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(scheduledActionsQueue);
			
			fos.close();
			oos.close();
			Logger.log("Saved scheduled action queue.", this.getClass());
		} catch (IOException e) {
			e.printStackTrace();
			Logger.log("Failed to save scheduled action queue", this.getClass());
		} 
	}
	
	protected String status() {
		return "There are currently " + scheduledActionsQueue.size() + " scheduled actions in the queue.";
	}
}
