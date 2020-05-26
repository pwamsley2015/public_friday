package io;

import java.io.BufferedReader;
import java.io.IOException;

public class FileReader {
	
	public static String getFileContents(String filePath) {
		StringBuilder resultBuilder = new StringBuilder();
		BufferedReader br;
        String currLine;
        
        try {
        	java.io.FileReader reader = new java.io.FileReader(filePath);
            br = new BufferedReader(reader);
 
            while ((currLine = br.readLine()) != null) {
                resultBuilder.append(currLine);
                resultBuilder.append("\n");
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return resultBuilder.toString().trim();
	}
 
}
