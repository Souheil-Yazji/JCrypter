package User;

public class Client extends User{

	public boolean isPremium, techSupportAccess;

	
	public Client(String username, String role) {
		super(username, role);
		this.isPremium = false;
		this.techSupportAccess = false;
		if (role.equals("Premium Client")){
			this.setPremium();
		}
	}
	
	/**
	 * @return the premium status
	 */
	public boolean isPremium() {
		return isPremium;
	}

	/**
	 * @param isPremium the premium status to set
	 */
	public void setPremium() {
		this.isPremium = !isPremium;
	}
	
	/**
	 * @return the techSupportAccess status
	 */
	public boolean isTechSupAccess() {
		return techSupportAccess;
	}

	/**
	 * @param techSupportAccess the techSupportAccess status to set
	 */
	public void setTechSupAccess() {
		this.techSupportAccess = !techSupportAccess;
	}

}
