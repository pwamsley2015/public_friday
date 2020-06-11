package data.training;

import static Friday.ChatContext.ChatType.TRAINING;

import java.util.Arrays;
import java.util.List;

import Friday.ChatContext;
import Friday.FridayAction;
import Friday.UnrecognizedCommandResponse;
import io.friday.FridayInput;
import io.friday.FridayOutput;
import users.User;

/**
 * A chat context for Training.
 * 
 * @author Patrick Wamsley
 */
public class TrainingChat extends ChatContext {
	
	private static final List<String>	RPE_QUESTION_KEYWORDS	= Arrays.asList(new String[] {"rpe", "@"}),
										NEW_VARIATION_KEYWORDS	= Arrays.asList(new String[] {"new", "variation"}),
										ACTIVE_SESSION_KEYWORDS = Arrays.asList(new String[] {"begin", "active", "start"}),
										SEE_BLOCK_KEYWORDS		= Arrays.asList(new String[] {"see", "block"});
	
	private boolean activeTrainingSessionMode;
	
	private ActiveTrainingSession session;
	
	public TrainingChat(User user) {
		super(TRAINING, user);
		activeTrainingSessionMode = false;
	}

	@Override
	public FridayOutput<?> getResponse() {
		
		//first get the message
		FridayInput<?> message = super.getNextMessage();
		
		if (message == null) {
			System.out.println("message is null");
		}
		
		if (activeTrainingSessionMode) {
			if (session.isSessionCompleted()) {
				activeTrainingSessionMode = false;
				session = null;
			} else {
				return new FridayOutput<>(session.executeStateMachine((String)message.getValue()));
			}
		}
		
		FridayAction response = interpret((String)message.getValue());
		return response.execute();
	}
	
	private FridayAction interpret(String message) {
		
		for (String s : RPE_QUESTION_KEYWORDS) {
			if (message.toLowerCase().contains(s)) { 
				return new RPE_Question(message);
			}
		}
		
		for (String s : NEW_VARIATION_KEYWORDS) {
			if (message.toLowerCase().contains(s)) {
				return new New_Variation(message);
			}
		}
		
		for (String s : ACTIVE_SESSION_KEYWORDS) {
			if (message.toLowerCase().contains(s)) {
				activeTrainingSessionMode = true;
				session = new ActiveTrainingSession(TrainingProgramFileUtil.fetchCurrentBlock(), user); 
				final String activeSeshResponse = session.executeStateMachine(message.toLowerCase());
				return new FridayAction() {
					@Override
					public FridayOutput<String> execute() {
						return new FridayOutput<>(activeSeshResponse);
					}
				};
			}
		}
		
		for (String s : SEE_BLOCK_KEYWORDS) {
			if (message.toLowerCase().contains(s)) {
				return new FridayAction() {
					@Override
					public FridayOutput<String> execute() {
						return new FridayOutput<>(TrainingProgramFileUtil.fetchCurrentBlock().writeBlock());
					}
				};
			}
		}
		
		return new UnrecognizedCommandResponse();
	}
	
	
}
