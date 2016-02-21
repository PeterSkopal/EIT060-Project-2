package users;

/**
 *  
 * @author Skopal
 *
 */
public class Nurse extends User {

	/**
	 * Creates a Nurse to the system
	 * 
	 * @param name
	 * @param division
	 */
	public Nurse(int SSN, String division, Database db) {
		super(SSN, division, db);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
}
