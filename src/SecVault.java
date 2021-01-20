import java.util.*;

public class SecVault {

	private static int LOGIN = 1;
	private static int REG = 2;
	static Scanner in = new Scanner(System.in);

	private SecVault() {
	};

	/**
	 * prompt the user for a username and password then authenticate
	 */
	private static void login() {

		String un, pw;
		 java.io.Console c = System.console();
		 un= c.readLine("Username: ");
		 pw = new String(c.readPassword("Password: "));

//		System.out.println("Enter username: ");
//		un = in.next();
//		System.out.println("Enter password: ");
//		pw = in.next();

		// authenticate pw
		boolean isAuthed = auth(un, pw);
		if (isAuthed == true) {
			System.out.println("login successful");
		} else {
			System.out.println("login failed, check username and password");
		}

	}

	/**
	 * Used upon login to authenticate the username and password.
	 * @param username user's username, used to password file data extraction
	 * @param pw password to be verified
	 * @return true if the password is correct, false otherwise
	 */
	private static boolean auth(String username, String pw) {

		// stored hash is the hashed password stored upon registration, authHash is the
		// hashed password to be verified
		byte[] salt = null, storedHash = null;

		String[] userEntry = PassfileHelper.retreive(username);
		if (userEntry == null) {
			System.out.println("User not found");
			return false;
		}
		// get salt/hash from user entry
		salt = userEntry[1].getBytes();
		storedHash = userEntry[2].getBytes();

		// verify password, decode base64 from storage
		return Password.verifyPass(pw, Base64.getDecoder().decode(salt), Base64.getDecoder().decode(storedHash));
	}

	/**
	 * Register the user, prompt them for a username, password, verify the password and their role.
	 * Assume the user is a trusted actor. 
	 */
	private static void register() {
		String un, pw, pw2, role;
		 java.io.Console c = System.console();
		 un= c.readLine("Username: ");
		 pw = new String(c.readPassword("Password: "));
		 pw2 = new String(c.readPassword("Password: "));
//		System.out.println("Enter username: ");
//		un = in.next();
//		System.out.println("Enter password: ");
//		pw = in.next();
//		System.out.println("Confirm password: ");
//		pw2 = in.next();

		 if (!(pw.equals(pw2))) {
			System.out.println("The passwords entered do not match");
			return;
		}
		if (!Password.passChecker(un, pw)) {
			System.out.println("Weak password, please select another.");
			return;
		}
		System.out.println("Enter Role: ");
		role = in.next();

		byte[] salt = Password.createSalt();
		byte[] hashedPW = Password.hash(pw, salt);

		PassfileHelper.addToFile(un, salt, hashedPW, role);

	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Password.insertWeakPassword();
		
		while (true) {
			Scanner in = new Scanner(System.in);
			int step = -1;

			// user must always either login, register or exit
			while (!(step != 1 ^ step != 2)) {
				System.out.println("Please select a number to begin: \n 0. Exit \n 1. Login \n 2. Register \n");
				step = in.nextInt();

				if (step == 0) {
					in.close();
					System.exit(step);
				}
			}

			if (step == LOGIN) {
				login();
			} else if (step == REG) {
				register();
			}
		}
	}
}
