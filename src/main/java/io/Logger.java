package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Patrick Wamsley
 */
@SuppressWarnings("rawtypes")
public class Logger {
	
	public static final String FILE_PATH = "FridayFiles"; //TODO: change (make a property)
	
	public static HashMap<Class, WritableFile> logs = new HashMap<>();
	public static HashMap<Class, WritableFile> writeFiles = new HashMap<>();
	
	public static void write(String s, Class caller) {
		getFile(caller, false).println(s);
	}
	
	public static void log(String s, Class caller) {
		getFile(caller, true).log(s);
	}
	
	public static void deleteFile(Class caller, boolean isLog) {
		WritableFile file = getFile(caller, isLog);
		HashMap<Class, WritableFile> map = isLog ? logs : writeFiles;
		
		file.delete(); 
		map.remove(caller);
	}
	
	public static WritableFile getFile(Class caller, boolean isLog) {
		HashMap<Class, WritableFile> map = isLog ? logs : writeFiles;
		WritableFile file = map.get(caller);
		if (file == null) {
			try {
				file = new WritableFile(FILE_PATH + File.separator + caller.getCanonicalName() + (isLog ? "_log" : "") + ".txt");
//				if (!file.createNewFile()) {
//					System.err.println("File: " + file.getName() + " already exists. Clearing instead.");
//					file.clear();
//				}
				map.put(caller, file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

}
