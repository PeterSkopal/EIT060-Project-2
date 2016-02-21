package users;

/**
 * 
 * @author Skopal
 *
 */
public class Gov extends User {

	public Gov() {
	}

	/**
	 * Deletes record associated with a certain patient
	 * 
	 * @param patient
	 * @return true if successful deletion
	 */
	public boolean delete(Patient patient) {
		boolean removed = RECORDS.get(patient).removeRecord();
		if (removed) {
			System.out.println(GetCurrentTimeStamp.getTimeStamp() + ": " + CERTIFICATE.toString() + "removed " + patient.toString() + " record.");
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
}
