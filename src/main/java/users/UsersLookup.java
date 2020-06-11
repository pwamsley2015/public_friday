package users;

import java.util.HashSet;
import java.util.Set;

import com.twilio.type.PhoneNumber;

public class UsersLookup {

	private static Set<User> users = new HashSet<>();
	
	public static void newUser(User user) {
		users.add(user);
	}
	
	public static User getUser(String name) {
		for (User u : users) {
			if (u.getName().equals(name)) {
				return u;
			}
		}
		return null;
	}

	public static User getUser(PhoneNumber from) {
		for (User u : users) {
			if (u.getPhoneNumber().equals(from)) {
				return u;
			}
		}
		return null;
	}
	
	
	
}
