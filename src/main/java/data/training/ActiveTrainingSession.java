package data.training;

import java.util.Date;

import com.twilio.TwilioHelper;

import Friday.Clock;
import Friday.ScheduledFridayAction;
import data.Contacts;

public class ActiveTrainingSession {
	
	public enum State {BEGINNING_SESSION, WARMING_UP, WORKING_SETS, ENDING_SESSION}
	
	private State state;
	private TrainingBlock block;
	private TrainingSession session;
	private TrainingSet nextSet;
	private int restTime;
	private int movementIndex;
	
	private boolean firstMovement = true;
	private boolean sessionCompleted = false;
	
	private ScheduledFridayAction lastSetReminder;
	
	public ActiveTrainingSession(TrainingBlock block, int restTime) {
		this.block = block;
		this.session = block.getCurrSession();
		state = State.BEGINNING_SESSION;
		this.restTime = restTime;
	}
	
	public ActiveTrainingSession(TrainingBlock block) {
		this(block, 4);
	}

	public String executeStateMachine(String contents) {
		
		contents = contents.toLowerCase();
		
		switch (state) {
			case BEGINNING_SESSION:
				session.beginSession();
				state = State.WARMING_UP;
				movementIndex = 1;
				String movementPres = session.getNextMovementPrescripition();
				nextSet = session.getNextSet();
				return "Okay, I'm ready to help you with your training session. Today you start with: " + movementPres + "\nLet me know when you're done warming up. Have a good workout :~).";
			case WARMING_UP:
				//TODO: estimated weights for the day, answer RPE questions
				if (contents.contains("done")) {
					state = State.WORKING_SETS;
					return "Let's get it";
				} else {
					return "I'm just waiting for you to finish warming up. Don't leave me waiting too long...";
				}
			case WORKING_SETS:
				//input= <weight>@<rpe> <notes>
				Date now = new Date();
				if (lastSetReminder != null) {
					if (now.before(lastSetReminder.getScheduledTime())) {
						lastSetReminder.cancel();
					}
				}
				
				int weight = Integer.parseInt(contents.substring(0, contents.indexOf("@")));
				float rpe = -1;
				String notes = "";
				String rest = contents.substring(contents.indexOf("@") + 1);
				if (rest.contains(" ")) {
					rpe = Float.parseFloat(rest.substring(0, rest.indexOf(" ")));
					notes = rest.substring(rest.indexOf(" ") + 1);
				} else {
					rpe = Float.parseFloat(rest);
					notes = "[no notes]";
				}
				
				nextSet.didSet(weight, rpe, notes);
				if (session.currentMovementHasNextSet()) {
					Date next = new Date();
					next.setMinutes(next.getMinutes() + restTime);
					lastSetReminder = new NextSetReminder(next, restTime);
					
					float lastSetE1RM = nextSet.getE1RM(); 
					nextSet = session.getNextSet();
					float nextSetRpe = nextSet.getPrescribedRpe();
					int nextSetReps = nextSet.getReps();
					System.out.println("Next set: " + nextSetReps + "@" + nextSetRpe);
					float nextWeight = RPE.getPercentage(nextSetRpe, nextSetReps) * lastSetE1RM;
					
					return "Okay, I've logged that for you. Your next set is " + nextSetReps + "@" + nextSetRpe + ". The math says to use roughly " + Math.round(nextWeight) + "lbs.";
				} else if (session.isSessionCompleted()) {
					state = State.ENDING_SESSION;
					return "Nice job. What was the session RPE? I can also log general notes for the session, if you'd like.";
				} else {
					state = State.WARMING_UP;
					String mp = session.getNextMovementPrescripition();
					nextSet = session.getNextSet();
					return "Nice job. Here's your prescripition for your " + indexToEnglish(movementIndex++) + " movement.\n" + mp + "\nLet me know when you're warmed up.";
				}
			case ENDING_SESSION:{
				session.sessionComplete(Float.parseFloat(contents.replaceAll("[^\\d.]", "")), contents);
				endSession();
				return "Okay, nicely done.";
			}
				
		}
		return null;
	}
	
	private static String indexToEnglish(int index) {
		if (index == 1)
			return "first";
		if (index == 2) 
			return "second";
		if (index == 3) 
			return "third";
		if (index == 4) 
			return "fourth";
		
		return index + "th";
	}
	
	private void endSession() {
		System.out.println(block.writeBlock());
		//update block
		if (!TrainingProgramFileUtil.saveCurrentBlock(block)) {
			TwilioHelper.sendText(Contacts.getMe(), "Sorry, for some reason I failed to save the current training block");
			System.out.println("Failed to save training block.");
		}
		sessionCompleted = true;
	}
	
	public boolean isSessionCompleted() {
		return sessionCompleted;
	}
	
	
	public static class NextSetReminder extends ScheduledFridayAction {

		private int restTime;
		
		public NextSetReminder(Date scheduledTime, int restTime) {
			super(scheduledTime);
			this.restTime = restTime;
			Clock.getClock().addScheduleAction(this);
		}

		@Override
		public String execute() {
			return "Sorry to nag you, but it's been " + restTime + " minutes since your last set. You don't want to be here all day, do you? Don't reply to that... just do your next set.";
		}
		
	}

}
