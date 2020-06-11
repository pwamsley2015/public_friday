package Friday;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import data.diet.WeightLogEntry;
import gui.GuiLauncher;
import gui.HomeScreen;
import io.FileReader;
import io.Logger;
import io.WritableFile;
import io.friday.FridayIO;
import io.friday.FridayInputSource;
import io.friday.FridayOutput;
import io.friday.FridayOutputSource;
import io.friday.implementations.ConsoleInput;
import io.friday.implementations.ConsoleOutput;
import io.friday.implementations.TextInput;
import io.friday.implementations.TextOutput;

public class Startup {

	public static enum StartupConfiq {TEXT, CONSOLE, GUI}

	/**
	 * Parses run arguments to launch Friday with requested IO sources, and init's the FridayIO object for the primaryUser
	 * @param args Launch Arguments given to Friday
	 * @param inputs - {@code Set<FridayInputSource>} to be populated
	 * @param ouputs - {@code Set<FridayOutputSource} to be populated
	 * 
	 * @see Friday.Friday
	 * @see io.friday.FridayIO
	 * @see io.friday.FridayInputSource
	 * @see io.friday.FridayOutputSource
	 * @return {@code FridayIO} of Friday.primaryUser
	 */
	public static FridayIO startup(String[] args, Set<FridayInputSource> inputs, Set<FridayOutputSource> outputs) {	
		
		System.out.println("Recieved args: " + Arrays.toString(args));
		
		startupCommands(args);
		
		// -it - accept input from texts
		// -ic - accept input from console
		// -ot - output via text
		// -oc - output via console
		// -g - GUI mode
		FridayOutputSource primaryOutput = null;
		for (String arg : args) {
			
			if (arg.contains("g")) {
				new Thread(new GuiLauncher()).start(); 
				while (HomeScreen.getHomeScreen() == null) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return HomeScreen.getHomeScreen().getGuiIO();
			}
			
			//---open inputs---//
			if (arg.contains("-i")) {
				if (arg.contains("c")) {
					inputs.add(openInput(StartupConfiq.CONSOLE));
				}
				if (arg.contains("t")) {
					inputs.add(openInput(StartupConfiq.TEXT));
				}
			}
			//---open outputs---//
			if (arg.contains("-o")) {
				FridayOutputSource outputSource = null;
				if (arg.contains("c")) {
					outputSource = openOutput(StartupConfiq.CONSOLE);
				}
				if (arg.contains("t")) {
					outputSource = openOutput(StartupConfiq.TEXT);
				}
				
				if (primaryOutput == null) {
					primaryOutput = outputSource;
				}
				
				if (outputSource != null) {
					outputs.add(outputSource);
				}
			}
		}
		
		StartupConfiq defaultConfiq = FridayProperties.getProperties().DEFAULT_STARTUP;
		if (inputs.isEmpty()) {
			inputs.add(openInput(defaultConfiq));
		}
		
		if (outputs.isEmpty()) {
			primaryOutput = openOutput(defaultConfiq);
			outputs.add(primaryOutput);
		}
		
		FridayIO io = new FridayIO(Friday.primaryUser, primaryOutput, inputs);
		System.out.println("PrimaryUser IO init:" + io);
		return io;
	}
	
	private static FridayInputSource openInput(StartupConfiq type) {
		FridayInputSource is = null;
		switch (type) {
			case CONSOLE:
				is = new ConsoleInput(Friday.primaryUser);
				break;
			case TEXT:
				is = new TextInput();
				break;
		}
		is.init();
		return is;
	}
	
	private static FridayOutputSource openOutput(StartupConfiq type) {
		FridayOutputSource os = null;
		switch (type) {
			case CONSOLE:
				os = new ConsoleOutput();
				break;
			case TEXT:
				os = new TextOutput();
				break;
		}
		os.init();
		return os;
	}

	private static void execute(String command) {
		if (command.contains(";")) {
			String[] split = command.split(";");
			String[] args = split[1].split(" ");
			executeCommandWithArgs(split[0], args);
		} else {
			executeCommand(command);
		}
	}

	private static void executeCommand(String command) {

	}

	private static void executeCommandWithArgs(String command, String... args) {

		if (command.toLowerCase().contains("remind")) {
			try {
				//remind  "message"
				Clock.getClock().addScheduleAction(new ScheduledFridayAction(new Date(), Friday.primaryUser) {
					@Override
					public FridayOutput<String> execute() {
						return new FridayOutput<>("While I was asleep you asked me to remind you of something. Luckily I'm a good listener. Here it is:\n\n" + args[0]);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
				Logger.log("Failed to execute startup reminder", Startup.class);
			}
		}
		if (command.toLowerCase().contains("bw")) {
			WeightLogEntry entry; 
			try {
				entry = new WeightLogEntry(args[1], new WritableFile(FridayProperties.getProperties().WEIGHT_LOG_PATH));
				Clock.getClock().addScheduleAction(new ScheduledActionWrapper<WeightLogEntry>(entry, new Date(), Friday.primaryUser));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void startupCommands(String[] args) {
		String startupContents = FileReader.getFileContents(FridayProperties.getProperties().STARTUP_PATH);

		if (startupContents.isEmpty()) {
			System.out.println("No start up commands.");
		} else {

			String[] commands = startupContents.split("\n");		
			for (String command : commands) {
				System.out.println("startup command: " + command);
				execute(command);
			}

			Logger.log("Executed startup commands.", Startup.class);
			WritableFile f = null;
			try {
				f = new WritableFile(FridayProperties.getProperties().STARTUP_PATH);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			f.clear();
		}
	}

}
