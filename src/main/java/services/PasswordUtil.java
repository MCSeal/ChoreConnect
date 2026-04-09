package services;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

	// make the password unreadable before saving it
	public static String hashPassword(String plainPassword) {
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
	}

	// check the typed password against the saved hash
	public static boolean checkPassword(String plainPassword, String hashedPassword) {
		return BCrypt.checkpw(plainPassword, hashedPassword);
	}
}