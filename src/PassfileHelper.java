import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;

/**
 * Helper class responsible for adding to and retrieving from the passwd.txt file.
 * 
 * @author Souheil
 *
 */
public class PassfileHelper {

	private PassfileHelper() {
	};

	/**
	 * This method will add a username, salt, and hash to a text file. 
	 * 
	 * @param username string username to be stored
	 * @param salt 16 byte array salt to be stored, base64 encoding
	 * @param hash 32 byte array hash to be stored, base64 encoding
	 * @param role 
	 */
	public static void addToFile(String username, byte[] salt, byte[] hash, String role) {

		String[] row = {username, Base64.getEncoder().encodeToString(salt), Base64.getEncoder().encodeToString(hash), role};
		String rowAsStr = "";
		
		for (int i = 0; i < row.length; i++) {
			rowAsStr += row[i];
			if (i == row.length-1) {
				rowAsStr += "\n";
			}else {
				rowAsStr += ":";
			}
		}
		
		try {
			FileWriter passfile = new FileWriter("src/passwd.txt", true);
			passfile.write(rowAsStr);
			passfile.close();
		} catch (IOException e) {
			throw new AssertionError("Error opening file: " + e.getMessage(), e);
		}
	}

	/**
	 * Retrieve the row from the passwd.txt file for username
	 * 
	 * @param username
	 *            the username to look for in the passwd.txt file
	 * @return the associated entry for username
	 */
	public static String[] retreive(String username) {
		String[] row = null;
		try {
			FileReader passfile = new FileReader("src/passwd.txt");
			BufferedReader csvReader = new BufferedReader(passfile);
			String line = null;

			while ((line = csvReader.readLine()) != null) {
				row = line.split(":");

				// fetch the correct row
				if (row[0].equals(username)) {
					csvReader.close();
					return row;
				}
			}
			csvReader.close();
		} catch (FileNotFoundException e) {
			throw new AssertionError("Error opening file: " + e.getMessage(), e);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// row not found
		return null;
	}
	
	
}
