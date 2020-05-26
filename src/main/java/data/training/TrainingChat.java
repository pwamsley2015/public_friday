package data.training;

import static Friday.ChatContext.ChatType.TRAINING;

import java.util.Arrays;
import java.util.List;

import Friday.ChatContext;
import Friday.FridayAction;
import Friday.FridayRecieveMessage;
import Friday.UnrecognizedCommandResponse;

/**
 * A chat context for Training.
 * 
 * @author Patrick Wamsley
 */
public class TrainingChat extends ChatContext {
	
	private static final List<String>	RPE_QUESTION_KEYWORDS	= Arrays.asList(new String[] {"rpe", "@"}),
										NEW_VARIATION_KEYWORDS	= Arrays.asList(new String[] {"new", "variation"}),
										ACTIVE_SESSION_KEYWORDS = Arrays.asList(new String[] {"begin", "active", "start"}),
										WRITE_PROGRAM_KEYWORDS	= Arrays.asList(new String[] {"write", "program", "mode", "writing"}),
										SEE_BLOCK_KEYWORDS		= Arrays.asList(new String[] {"see", "block"});
	
	private boolean programWritingMode, activeTrainingSessionMode;
	
//	private TrainingProgramWriter programWriter;
	private ActiveTrainingSession session;
	
	public TrainingChat() {
		super(TRAINING);
		programWritingMode = false;
		activeTrainingSessionMode = false;
//		programWriter = new TrainingProgramWriter();
	}

	@Override
	public String getResponse() {
		
		//first get the message
		FridayRecieveMessage message = super.getNextMessage();
		
		if (message == null) {
			System.out.println("message is null");
		}
		
//		if (programWritingMode) {
//			return programWriter.executeStateMachine(message.getContents());
//		}
		
		if (activeTrainingSessionMode) {
			if (session.isSessionCompleted()) {
				activeTrainingSessionMode = false;
				session = null;
			} else {
				return session.executeStateMachine(message.getContents());
			}
		}
		
		FridayAction response = interpret(message.getContents());
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
		
		for (String s : WRITE_PROGRAM_KEYWORDS) {
			if (message.toLowerCase().contains(s)) {
				programWritingMode = true;
				return new FridayAction() {
					@Override
					public String execute() {
						return "Sorry, program writing is currently disabled.";
//						return "Okay, Entering program writing mode. For now, let's just write new programs. If you need to edit, do so to the file directly.";
					}
				};
			}
		}
		
		for (String s : ACTIVE_SESSION_KEYWORDS) {
			if (message.toLowerCase().contains(s)) {
				activeTrainingSessionMode = true;
				session = new ActiveTrainingSession(TrainingProgramFileUtil.fetchCurrentBlock()); 
				final String activeSeshResponse = session.executeStateMachine(message.toLowerCase());
				return new FridayAction() {
					@Override
					public String execute() {
						return activeSeshResponse;
					}
				};
			}
		}
		
		for (String s : SEE_BLOCK_KEYWORDS) {
			if (message.toLowerCase().contains(s)) {
				return new FridayAction() {
					@Override
					public String execute() {
						return TrainingProgramFileUtil.fetchCurrentBlock().writeBlock();
					}
				};
			}
		}
		
		return new UnrecognizedCommandResponse();
	}
	
	
}
