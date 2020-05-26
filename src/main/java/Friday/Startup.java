package Friday;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import Friday.chat_io_types.ConsoleIO;
import Friday.chat_io_types.FridayIO;
import Friday.chat_io_types.TextIO;
import gui.GuiLauncher;
import gui.HomeScreen;
import io.FileReader;
import io.Logger;
import io.WritableFile;

public class Startup {

	public static enum StartupConfiq {TEXT, CONSOLE, GUI}

	public static FridayIO startup(String[] args) {

		//get startup file
		String startupContents = FileReader.getFileContents(FridayProperties.getProperties().STARTUP_PATH);

		if (startupContents.isEmpty()) {
			System.out.println("No start up commands.");
		} else {

			String[] commands = startupContents.split("\n");		
			for (String command : commands) {
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
		
		if (args.length == 0) {
			return createIO(FridayProperties.getProperties().DEFAULT_STARTUP);
		} else if (args.length > 1) {
			throw new IllegalArgumentException();
		} else {
			String arg = args[0];
			if (arg.equalsIgnoreCase("-t")) {
				return createIO(StartupConfiq.TEXT);
			} else if (arg.equalsIgnoreCase("-c")) {
				return createIO(StartupConfiq.CONSOLE);
			} else if (arg.equalsIgnoreCase("-g")) {
				return createIO(StartupConfiq.GUI);
			}
		}
		
		//unreachable
		assert "peanut butter" == "good";
		return createIO(FridayProperties.getProperties().DEFAULT_STARTUP);
	}
	
	private static FridayIO createIO(StartupConfiq confiq) {
		switch(confiq) {
			case CONSOLE:
				return new ConsoleIO();
			case TEXT:
				return new TextIO();
			case GUI:
				GuiLauncher l = new GuiLauncher();
				new Thread(l).start();
				while (HomeScreen.getHomeScreen() == null) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return HomeScreen.getHomeScreen().getGuiIO();
		}
		return null;
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
				Clock.getClock().addScheduleAction(new ScheduledFridayAction(new Date()) {
					@Override
					public String execute() {
						return "While I was asleep you asked me to remind you of something. Luckily I'm a good listener. Here it is:\n\n" + args[0];
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
				Logger.log("Failed to execute startup reminder", Startup.class);
			}
		}

	}

}
