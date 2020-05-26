package offline_commands;

import java.io.IOException;

import io.WritableFile;

/**
 * A small program used to edit parameters Friday looks at during launch. For instance, A new {@code ScheduledFridayAction} can be given to friday without having to launch Friday. 
 * 
 * @see Friday.Friday
 * @author Patrick Wamsley
 */
public class GiveCommand {
	
	/**
	 * @param args[0] - Path to startup.Friday file
	 * @param args[1] - command to give friday 
	 * @param args[2...n] additional parameters for that command
	 */
	public static void main(String... args) {
		
		WritableFile startup = null;
		
		try {
			startup = new WritableFile(args[0]);
		} catch (IOException e) {
			System.err.println("Failed to open startup.Friday");
		}
		
		if (startup == null) {
			System.exit(1);
		}
		
		startup.print(args[1]);
		
		if (args.length > 2) {
			startup.print(";");
			for (int i = 2; i < args.length; i++) {
				startup.print(" " + args[i]);
			}
		}
		
		startup.print("\n");
		
	}

}
