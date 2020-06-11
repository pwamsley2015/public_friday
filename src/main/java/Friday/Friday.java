 package Friday;

import java.util.HashSet;
import java.util.Set;

import io.friday.FridayIO;
import io.friday.FridayInputSource;
import io.friday.FridayOutput;
import io.friday.FridayOutputSource;
import users.User;

public class Friday {
	
	private static Set<User> users;
	public static final User primaryUser = new User(FridayProperties.getProperties().USER_NAME);
	private static Set<FridayOutputSource> outputs;
	private static Set<FridayInputSource> inputs;
	
	public static void main(String[] args) {
		
		//---init primaryUser---///
		primaryUser.setPhoneNumber(FridayProperties.getProperties().USER_NUMBER);
		users = new HashSet<>();
		users.add(primaryUser);
		
		outputs = new HashSet<>();
		inputs = new HashSet<>();
		
		FridayIO primaryUserIO = Startup.startup(args, inputs, outputs);
		primaryUser.initIO(primaryUserIO);
		primaryUserIO.sendOutput(new FridayOutput<>("Hey Boss."));
		
		
		//begin Friday's clock
		new Thread(Clock.getClock()).start();
					
		//Begin waiting for inputs
		while (true) {
			primaryUserIO.waitForNextAndRespond();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	

}
