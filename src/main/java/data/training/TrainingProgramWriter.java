//package data.training;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashSet;
//
//import Friday.FridayProperties;
//import data.training.TrainingBlock.BlockObjective;
//import data.training.TrainingWeek.Stress;
//import data.training.TrainingWeek.Type;
//import io.WritableFile;
//
//public class TrainingProgramWriter {
//	
//	public enum State {INIT_PROGRAM, WRITING_BLOCK, WRITING_WEEK, WRITING_SESSION, EXIT_WRITER, WAITING}
//	
//	private State state = State.WAITING;
//	private WritableFile programFile;
//	private boolean programNamed = false;
//	
//	private TrainingBlock currentBlock;
//	private TrainingWeek currentWeek;
//	private TrainingSession currentSession;
//	
//	public String executeStateMachine(String input) {
//		
//		if (state == State.WAITING) {
//			state = State.INIT_PROGRAM;
//			return executeStateMachine(input);
//		}
//		
//		if (input.toLowerCase().contains("exit")) {
//			state = State.EXIT_WRITER;
//		}
//		switch (state) {
//			case INIT_PROGRAM:
//				if (programFile == null) {
//					if (!programNamed) {
//						programNamed = true;
//						return "I'll create a file for this new program. What are we calling it?";
//					}
//					else {
//						Date today = new Date();
//						String path = FridayProperties.getProperties().TRAINING_PROGRAMS_PATH + "/" + input.replaceAll(" ", "_") + "_" + (today.getYear() + 1900) + "|" + today.getMonth();
//						try {
//							Files.createFile(Paths.get(path));
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//						try {
//							programFile = new WritableFile(path);
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//						
//						programNamed = true;
//						state = State.WRITING_BLOCK;
//						return "Okay, what type of block is this?";
//					}
//				}
//			case EXIT_WRITER:
//				writeProgram();
//				resetWriter();
//				return "Okay, I've exited program writing mode.";
//			case WRITING_BLOCK:
//				
//				currentBlock = parseBlock(input.toLowerCase());
//				if (currentBlock == null) {
//					return "I don't understand what you mean by that. Just let me know what kind of block this is: Strength dev, strength display, hypertrophy, pivot, or gpp?";
//				}
//				
//				state = State.WRITING_WEEK;
//				return "Okay, let's start with week 1. What level of stress is this and what kind of training week is it?";
//				
//			case WRITING_WEEK:
//				
//				if (input.toLowerCase().contains("done")) {
//					state = State.EXIT_WRITER;
//					return "Okay. Next week?";
//				}
//				
//				currentWeek = parseWeek(input.toLowerCase());
//				if (currentWeek == null) {
//					return "I had trouble understanding you. Just let me know what level of stress and what kind of training week this is.";
//				}
//				state = State.WRITING_SESSION;
//				currentBlock.addWeeks(currentWeek);
//				return "What's the workout for day 1?";
//				
//			case WRITING_SESSION:
//				
//				if (input.toLowerCase().contains("done")) {
//					state = State.WRITING_WEEK;
//					return "Okay. Next week?";
//				} else {
//					currentSession = parseSession(input.toLowerCase());
//					if (currentSession == null) {
//						return "I didn't get that. Check your variations.";
//					}
//					currentWeek.addSessions(currentSession);
//					return "Okay, next session?";
//				}
//				
//		}
//		return "Something went wrong with my program writing state machine. Please fix me.";
//	}
//	
//	private void writeProgram() {
//		programFile.print(currentBlock.writeBlock());
//	}
//	
//	private void resetWriter() {
//		state = State.WAITING;
//		currentBlock = null;
//		currentWeek = null;
//		currentSession = null;
//		programFile = null;
//	}
//	
//	private TrainingBlock parseBlock(String input) {
//		
//		if (input.contains("strength dev")) {
//			return new TrainingBlock(BlockObjective.STRENGTH_DEV);
//		} else if (input.contains("strength display")) {
//			return new TrainingBlock(BlockObjective.STRENGTH_DISPLAY);
//		} else if (input.contains("hypertrophy")) {
//			return new TrainingBlock(BlockObjective.HYPERTROPHY);
//		} else if (input.contains("pivot") || input.contains("washout")) {
//			return new TrainingBlock(BlockObjective.PIVOT); 
//		} else if (input.contains("gpp")) {
//			return new TrainingBlock(BlockObjective.GPP);
//		}
//		
//		return null;
//	}
//	
//	private TrainingWeek parseWeek(String input) {
//		
//		Stress s = null;
//		Type t = null;
//		
//		if (input.contains("low")) {
//			s = Stress.LOW;
//		} else if (input.contains("med")) {
//			s = Stress.MEDIUM;
//		} else if (input.contains("high")) {
//			s = Stress.HIGH;
//		}
//	
//		if (input.contains("pivot")) {
//			t = Type.PIVOT;
//		} else if (input.contains("dev")) {
//			t = Type.DEVELOPEMENTAL;
//		} else if (input.contains("inten") || input.contains("spec")) {
//			t = Type.INTENSIFICATION;
//		} else if (input.contains("gpp")) {
//			t = Type.GPP;
//		} else if (input.contains("peak")) {
//			t = Type.PEAK;
//		}
//		
//		return (s != null && t != null) ? new TrainingWeek(s, t) : null;
//	}
//	
//	private TrainingSession parseSession(String input) {
//		
//		TrainingSession sesh = new TrainingSession();
//		
//		String[] lines = input.split("\n");
//		
//		for (String line : lines) {
//			String[] tokens = line.split(" ");
//			TrainingMovement movement = getMovement(tokens[0]);
//			ArrayList<TrainingSet> sets = new ArrayList<>();
//			
//			for (int i = 1; i < tokens.length; i++) {
//				String token = tokens[i];
//				String[] split = token.split("@");
//				int reps = Integer.parseInt(split[0]);
//				float rpe = Float.parseFloat(split[1]);
//				sets.add(new TrainingSet(reps, rpe));
//			}
//			
//			sesh.addMovementPrescripition(movement, sets);
//		}
//		
//		return sesh;
//	}
//	
//	private TrainingMovement getMovement(String token) {
//		TrainingMovement movement = null;
//		for (PrimaryMovement primary : PrimaryMovement.values()) {
//			HashSet<TrainingMovement> vars = Variations.getVariations(primary);
//			for (TrainingMovement v : vars) {
//				if (v.toString().equalsIgnoreCase(token)) {
//					movement = v;
//				}
//			}
//		}
//		return movement;
//	}
//
//}
