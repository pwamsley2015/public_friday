package data.training;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import Friday.FridayProperties;
import io.FileReader;
import io.WritableFile;

public class Variations {

	//init when needed (first time Variations is referenced statically)
	static {
		Variations.init();
	}
	
	private static WritableFile variationsFile;
	private static HashMap<PrimaryMovement, HashSet<TrainingMovement>> variations;
	
	public static HashSet<TrainingMovement> getVariations(PrimaryMovement primary) {
		return variations.get(primary);
	}
	
	public static List<TrainingMovement> getAllMovements() {
		List<TrainingMovement> movements = new ArrayList<>();
		variations.values().stream().forEach(e -> movements.addAll(e));
		return movements;
	}
	
	public static boolean init() {
		try {
			variationsFile = new WritableFile(FridayProperties.getProperties().VARIATIONS_PATH);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		variations = new HashMap<>();
		for (PrimaryMovement movement : PrimaryMovement.values()) {
			variations.put(movement, new HashSet<>());
		}
		
		String varFileContents = FileReader.getFileContents(FridayProperties.getProperties().VARIATIONS_PATH);
		if (varFileContents == null) {
			return false;
		}
		
		if (varFileContents.isEmpty()) {
			return true;
		}
		
		String[] lines = varFileContents.split("\n");
		for (String line : lines) {
			if (line.isEmpty()) {
				break;
			}
			String[] primaryVarsSplit = line.split(";");
			String primary = primaryVarsSplit[0].toUpperCase();
			TrainingMovement primaryMovement = new TrainingMovement(primary, PrimaryMovement.valueOf(primary), true);
			variations.get(PrimaryMovement.valueOf(primary)).add(primaryMovement);
			if (primaryVarsSplit.length < 2) {
				continue;
			}
			String[] vars = primaryVarsSplit[1].split(",");
			if (variations.get(PrimaryMovement.valueOf(primary)) != null) {
				for (String variation : vars) {
					if (variation.isEmpty()) {
						break;
					}
					TrainingMovement movement = new TrainingMovement(variation, PrimaryMovement.valueOf(primary), false);
					variations.get(PrimaryMovement.valueOf(primary)).add(movement);
				}
			}
		}
		return true;
	}
	
	public static void newVariation(TrainingMovement movement) {
		//update live hashmap
		if (variations.get(movement.type) == null) {
			variations.put(movement.type, new HashSet<>());
		}
		variations.get(movement.type).add(movement);
		
		//update saved-on-file version
		variationsFile.clear();
		writeUpdatedVariations();
	}
	
	public static void clearVariations() {
		variations.clear();
		variationsFile.clear();
	}
	
	private static void writeUpdatedVariations() {
		for (PrimaryMovement primary : variations.keySet()) {
			variationsFile.print(primary.toString().toUpperCase() + ";");
			for (TrainingMovement v : variations.get(primary)) {
				variationsFile.print(v.variation + ",");
			}
			variationsFile.println("");
		}
	}
	
	public static String seeVariations() {
		System.out.println("Variations: " + variations);
		StringBuilder s = new StringBuilder();
		for (PrimaryMovement primary : variations.keySet()) {
			s.append(primary.toString() + ";");
			for (TrainingMovement v : variations.get(primary)) {
				s.append(v.variation + ", ");
			}
			s.append("\n");
		}
		return s.toString();
	}
	
}
