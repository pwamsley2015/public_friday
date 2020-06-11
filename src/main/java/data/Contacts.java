package data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.twilio.type.PhoneNumber;

import Friday.FridayProperties;
import io.FileReader;
import io.WritableFile;

public class Contacts {

	private Contacts(){}

	private static HashMap<String, PhoneNumber> phoneBook;

	private static PhoneNumber me;

	private static WritableFile contactsFile;

	static {
		phoneBook = new HashMap<>();
		try {
			contactsFile = new WritableFile(FridayProperties.getProperties().CONTACT_BOOK_PATH);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		init();
	}

	public static void init() {

		String[] lines = FileReader.getFileContents(FridayProperties.getProperties().CONTACT_BOOK_PATH).split("\n");
		for (String line : lines) {
			String[] entry = line.split("-");
			if (entry.length > 1) {
				phoneBook.put(entry[0], new PhoneNumber(entry[1]));
			}
		}
		FridayProperties props = FridayProperties.getProperties();
		setMe(props.USER_NAME, props.USER_NUMBER);
	}

	public static void add(String name, String phoneNumber) {
		phoneBook.put(name, new PhoneNumber(phoneNumber));
		contactsFile.println(name + "-" + phoneNumber);
	}

	public static void setMe(String name, String phoneNumber) {
		add(name, phoneNumber);
		me = new PhoneNumber(phoneNumber);
	}

	public static void setMe(String name, PhoneNumber phoneNumber) {
		phoneBook.put(name, phoneNumber);
		me = phoneNumber;
	}

	public static PhoneNumber getMe() {
		return me;
	}

	public static boolean isMe(PhoneNumber num) {
		return me.equals(num);
	}

	public ArrayList<PhoneNumber> getNumbers(String... names) {
		ArrayList<PhoneNumber> numbers = new ArrayList<>();
		for (String name : names) {
			numbers.add(phoneBook.get(name));
		}
		return numbers;
	}

	public static HashMap<String, PhoneNumber> seeBook() {
		return phoneBook;
	}

	public static PhoneNumber searchForName(String name) {

		//		System.out.println("Searching for name: " + name);

		for (String key : phoneBook.keySet()) {
			if (key.contains(name.toLowerCase())) {
				return phoneBook.get(key);
			}
		}
		return null;
	}
}
