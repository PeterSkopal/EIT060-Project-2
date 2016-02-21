package users;

import Database.*;

/**
 * 
 * @author Skopal
 *
 */
public class Doctor extends User {

	/**
	 * Creates a Doctor to the system
	 * 
	 * @param SSN
	 * @param division
	 */
	public Doctor(int SSN, String division, Database db) {
		super(SSN, division, db);
	}

	public boolean create(String patientName, Nurse nurse, String input) {
		Patient patient = new Patient(patientName, patientDivision, this, nurse);
		patient.createRecord(input);
		System.out.println(GetCurrentTimeStamp.getTimeStamp() + ": " + CERTIFICATE.toString() + "created " + patient.toString() + " record.");
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
}
