package Friday;

import com.twilio.type.PhoneNumber;

import Friday.Startup.StartupConfiq;
import data.training.Variations;
import io.FileReader;


public class FridayProperties {
	
	public static final String PATH = "FridayFiles/props.Friday";
	
	private static FridayProperties instance;
	private static boolean isInit = false;
	
	//Twilio
	public String	TWILLIO_ACCOUNT_SID, 
					TWILLIO_AUTH_TOKEN,
					TWILLIO_FRIDAY_NUM_SID;
	
	public String USER_NAME;
	
	public PhoneNumber	FRIDAY_NUMBER,
						USER_NUMBER;
	
	//file locations
	public String	CONTACT_BOOK_PATH,
					CLOCK_SAVE_PATH,
					VARIATIONS_PATH,
					TRAINING_PROGRAMS_PATH,
					STARTUP_PATH,
					WEIGHT_LOG_PATH,
					CURR_TRAINING_BLOCK_PATH;
	
	//confiq
	public StartupConfiq DEFAULT_STARTUP;
	
	
	public static FridayProperties getProperties() {
		if (!isInit) {
			instance = new FridayProperties();
			isInit = true;
			
			String propsFile = FileReader.getFileContents(PATH);
			
			String[] properties = propsFile.split("\n");
			for (String prop : properties) {
				String[] pair = prop.split("=");
				String fieldName = pair[0];
				String val = pair[1];
				
				if (fieldName.equals("TWILLIO_ACCOUNT_SID")) {
					instance.TWILLIO_ACCOUNT_SID = val;
				} else if (fieldName.equals("TWILLIO_AUTH_TOKEN")) {
					instance.TWILLIO_AUTH_TOKEN = val;
				} else if (fieldName.equals("TWILLIO_FRIDAY_NUM_SID")) {
					instance.TWILLIO_FRIDAY_NUM_SID = val;
				} else if (fieldName.equals("FRIDAY_NUMBER")) {
					instance.FRIDAY_NUMBER = new PhoneNumber("+1" + val);
				} else if (fieldName.equals("USER_NUMBER")) {
					instance.USER_NUMBER = new PhoneNumber("+1" + val);
				} else if (fieldName.equals("USER_NAME")) {
					instance.USER_NAME = val;
				} else if (fieldName.equals("CONTACT_BOOK_PATH")) {
					instance.CONTACT_BOOK_PATH = val;
				} else if (fieldName.equals("CLOCK_SAVE_PATH")) {
					instance.CLOCK_SAVE_PATH = val;
				} else if (fieldName.equals("VARIATIONS_PATH")) {
					instance.VARIATIONS_PATH = val;
				} else if (fieldName.equals("TRAINING_PROGRAMS_PATH")) {
					instance.TRAINING_PROGRAMS_PATH = val;
				} else if (fieldName.equals("STARTUP_PATH")) {
					instance.STARTUP_PATH = val;
				} else if (fieldName.equals("WEIGHT_LOG_PATH")) {
					instance.WEIGHT_LOG_PATH = val;
				} else if (fieldName.equals("CURR_TRAINING_BLOCK_PATH")) {
					instance.CURR_TRAINING_BLOCK_PATH = val;
				} else if (fieldName.equals("DEFAULT_STARTUP")) {
					instance.DEFAULT_STARTUP = StartupConfiq.valueOf(val.toUpperCase());
				}
			}
		}
		return instance;
	}
	
	

}
