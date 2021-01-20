import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**
 * Password helper class. The password checker is also implemented in this class. * 
 * Hashing is done through Password-Based Key Derivation Function 2 (PBKDF2).
 * This is a slow hash function which in this case uses SHA-512 while
 * safeguarding against brute force attacks or rainbow table attacks by reducing
 * the possible guesses per second
 * 
 * @author Souheil
 *
 */
public class Password {

	private static final Random RANDOM = new SecureRandom();
	private static final int PBKDF2_ITERATIONS = 10000;
	private static final int KEY_LENGTH = 256;
	private static ArrayList<String> weakPasswords = new ArrayList<String>();
	private static char[] spec = { '!', '@', '#', '$', '%', '?', '*' };

	private Password() {
	}

	/**
	 * Securely generated pseudorandom 16byte/128bit salt to be used for hashing,
	 * length used is suggested by NIST A longer salt effectively increases the
	 * computational complexity of attacking passwords which in turn increases the
	 * candidate set exponentially.
	 * 
	 * @return a 16 byte salt
	 */
	public static byte[] createSalt() {
		byte[] salt = new byte[16];
		RANDOM.nextBytes(salt);
		return salt;
	}

	/**
	 * This method hashes the input password with the provided salt. Uses PBKDF2 With Hmac SHA512 with 10000 iterations
	 * and a key length of 256.
	 * 
	 * @param pw the password to be encrypted
	 * @param salt 16 byte salt 
	 * @return a hashed password encoded to a 32 byte array
	 */
	public static byte[] hash(String pw, byte[] salt) {
		char[] password = pw.toCharArray();
		PBEKeySpec spec = new PBEKeySpec(password, salt, PBKDF2_ITERATIONS, KEY_LENGTH); // password based encryption(PBE) key
		Arrays.fill(password, Character.MIN_VALUE); // destroy password in memory
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			return skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new AssertionError("Error while hashing: " + e.getMessage(), e);
		} finally {
			spec.clearPassword();
		}
	}

	/**
	 * This method compares the hash values of a provided password with the associated user's stored password.
	 * 
	 * @param pw the password provided by the user attempting a login
	 * @param salt the stored salt
	 * @param storedHash the stored encrypted password
	 * @return true if the password hashes match, false otherwise
	 */
	public static boolean verifyPass(String pw, byte[] salt, byte[] storedHash) {
		char[] password = pw.toCharArray();
		// hash input password + salt for Hashed input password
		byte[] passHash = hash(pw, salt);
		Arrays.fill(password, Character.MIN_VALUE); // destroy password in memory

		// no need to check bytes if lengths are not equal
		if (passHash.length != storedHash.length)
			return false;
		// Check each byte in stored hash from passwd.txt with the input hashed password
		for (int i = 0; i < passHash.length; i++) {
			if (passHash[i] != storedHash[i])
				return false;
		}
		return true;
	}

	/**
	 * This method checks the password provided against the requirements provided with the SecVault system.
	 * 
	 * @param un the username of the user
	 * @param pw the password to be checked
	 * @return true if the password passes all criteria, false otherwise
	 */
	public static boolean passChecker(String un, String pw) {
		
		if (pw.length() < 8)
			return false;
		if (pw.equals(un))
			return false;
		for (String s : weakPasswords) {
			if (pw.equals(s))
				return false;
		}

		boolean up = false, low = false, num = false, isSpec = false;
		for (char c : pw.toCharArray()) {
			if (Character.isLowerCase(c)) {
				low = true;
			} else if (Character.isUpperCase(c)) {
				up = true;
			} else if (Character.isDigit(c)) {
				num = true;
			} else {
				for (char s : spec) {
					if (s == c)
						isSpec = true;
				}
			}
		}
		if (!(up && low && num && isSpec))
			return false;
		return true;
	}

	/**
	 * this list would clearly have to be stored on some non-destructible format
	 * such as a csv file on a server. For simplicity and to comply with assignment
	 * requirements, this is provided to add weak passwords but will be destroyed
	 * upon exiting the JVM.
	 * 
	 * @param weakPassword
	 *            the weakPassword to add
	 */
	public static void insertWeakPassword() {
		weakPasswords.add("Password1");
		weakPasswords.add("Qwerty123");
		weakPasswords.add("Qaz123wsx");
	}

}
