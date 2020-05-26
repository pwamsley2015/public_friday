package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class WritableFile extends File {
	
	private PrintWriter writer;

	public WritableFile(String pathname) throws FileNotFoundException, IOException {
		super(pathname);
		FileWriter fw = new FileWriter(this, true);
		writer = new PrintWriter(fw);
	}

	public void println(String s) {
		writer.append(s + "\n");
		writer.flush();
	}
	
	public void print(String s) {
		writer.append(s);
		writer.flush();
	}
	
	public void log(String s) {
		Date d = new Date();
		println(d.toGMTString() + ": " + s);
	}
	
	public void clear() {
		writer.close();
		try {
			writer = new PrintWriter(new FileWriter(this, false));
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer.write(new char[] {});
		writer.flush();
		writer.close();
		try {
			writer = new PrintWriter(new FileWriter(this, true));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
