package User;

/**
 * This class is designed to handle access control policies
 * 
 * @author Souheil
 *
 */
public class AccessControlHelper {

	private AccessControlHelper() {
	}

	private static boolean isBusinessHours = false;

	/**
	 * 
	 * @param e
	 * @param c
	 * @return
	 */
	public boolean accessClientAccount(Employee e, Client c) {
		if (e.getRole().equals("Technical Support") && c.isTechSupAccess()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param u
	 * @return
	 */
	public boolean canAccessSys(User u) {
		if (u.getRole().equals("Teller")) {
			if (!isBusinessHours()) {
				return false;
			}
		}
		return true;
	}
	
//	public boolean clientAccess(Client c, boolean owner, String o) {
//		if (o.equals("Account Balance") || o.equals("Investments Portfolio") || o.equals("Financial Advisor Contact details"))
//	}

	/**
	 * @return the isBusinessHours
	 */
	public static boolean isBusinessHours() {
		return isBusinessHours;
	}

	/**
	 * @param isBusinessHours
	 *            the isBusinessHours to set
	 */
	public static void setBusinessHours() {
		AccessControlHelper.isBusinessHours = !isBusinessHours;
	}

}
