package data.training;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

import Friday.FridayProperties;
import data.training.TrainingBlock.BlockObjective;
import data.training.TrainingWeek.Stress;
import data.training.TrainingWeek.Type;
import io.FileReader;

public class TrainingProgramFileUtil {
	
	public static boolean saveNewBlock(TrainingBlock newBlock, String name) {
		
		String path = FridayProperties.getProperties().TRAINING_PROGRAMS_PATH + File.separator + name;
		System.out.println(path);
		boolean worked = false; 
		try {
			File f = new File(path);
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(newBlock);
			fos.close();
			oos.close();
			worked = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return worked;
	}
	
	public static TrainingBlock fetchCurrentBlock() {
		
		TrainingBlock block = null;
		
		try {
			FileInputStream fis = new FileInputStream(FridayProperties.getProperties().CURR_TRAINING_BLOCK_PATH);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			block = (TrainingBlock)ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} 
		
		return block;
	}
	
	public static boolean saveCurrentBlock(TrainingBlock block) {
		boolean worked = false; 
		try {
			
			FileOutputStream fos = new FileOutputStream(FridayProperties.getProperties().CURR_TRAINING_BLOCK_PATH);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(block);
			fos.close();
			oos.close();
			worked = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return worked;
	}
	
	public static boolean saveCompletedBlock(TrainingBlock block) {
		//TODO
		return false;
	}
	
	public static TrainingBlock parseNewProgram(String path) {
		String fileContents = FileReader.getFileContents(path);
		String[] lines = fileContents.split("\n");
		String blockType = lines[0].trim().toUpperCase();
		System.out.println(blockType);
		TrainingBlock block = new TrainingBlock(BlockObjective.valueOf(blockType));
		TrainingWeek currWeek = null;
		TrainingSession currSession = null;
		TrainingMovement currMovement = null;
		for (int i = 1; i < lines.length; i++) {
			if (lines[i].toLowerCase().contains("week")) {
				String[] words = lines[i].split(" ");
				//week <n> stress=<stress> type=<type>
				String stress = null, type = null;
				for (String word : words) {
					if (word.toLowerCase().contains("stress")) {
						stress = word.replaceAll("stress=", "").toUpperCase();
					} else if (word.toLowerCase().contains("type")) {
						type = word.replaceAll("type=", "").toUpperCase();
					}
				}
				currWeek = new TrainingWeek(Stress.valueOf(stress), Type.valueOf(type), i);
				block.addWeeks(currWeek);
			} else if (lines[i].toLowerCase().contains("day")) {
				String dayNum = lines[i].toLowerCase().replaceAll("^(day [1-7])", "");
				System.out.println("dayNum debug: " + dayNum);
				int day = Integer.parseInt(dayNum.replaceAll("day ", ""));
				String[] movements = lines[i].toLowerCase().replaceAll("day [0-9] ", "").split(";(\\s+)?");
				currSession = new TrainingSession(day);
				for (String movement : movements) {
					String[] tokens = movement.split("[ ,]");
					currMovement = getMovement(tokens[0].trim());
					for (int j = 1; j < tokens.length; j++) {
						//TODO: more general prescripition types like percentages
						//reps@rpe or reps@rpexsets 
						//more than one set
						if (tokens[j].trim().isEmpty()) {
							continue;
						}
						System.out.println("curr token: " + tokens[j]);
						int reps = Integer.parseInt(tokens[j].substring(0, tokens[j].indexOf("@")));
						float rpe = -1;
						int numSets = 1;
						if (tokens[j].contains("x")) {
							rpe = Float.parseFloat(tokens[j].substring(tokens[j].indexOf("@") + 1, tokens[j].indexOf("x")));
							numSets = Integer.parseInt(tokens[j].substring(tokens[j].indexOf("x") + 1));
							for (int k = 0; k < numSets; k++) {
								currSession.addMovementPrescripition(currMovement, new TrainingSet(reps, rpe));
							}
						} else {
							rpe = Float.parseFloat(tokens[j].substring(tokens[j].indexOf("@") + 1));
							currSession.addMovementPrescripition(currMovement, new TrainingSet(reps, rpe));
						}
					}
					System.out.println("debug: sesh:" + currSession.writeSession());
					currWeek.addSessions(currSession);
				}
			}
		}
		return block;
	}

	private static TrainingMovement getMovement(String token) {
		TrainingMovement movement = null;
		for (PrimaryMovement primary : PrimaryMovement.values()) {
			HashSet<TrainingMovement> vars = Variations.getVariations(primary);
			for (TrainingMovement v : vars) {
				if (v.toString().equalsIgnoreCase(token)) {
					movement = v;
				}
			}
		}
		return movement;
	}
	
	public static void main(String[] args) {
		Variations.init();
		System.out.println(Variations.seeVariations());
		String testPath = "FridayFiles/test_program.Friday";
		TrainingBlock testBlock = parseNewProgram(testPath);
		System.out.println(testBlock.writeBlock());
		
		saveCurrentBlock(testBlock);
		TrainingBlock readBlock = fetchCurrentBlock();
		System.out.println("read:");
		System.out.println(readBlock.writeBlock());
	}
}
