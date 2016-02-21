package users;
/**
 * 
 * @author Skopal
 *
 */
public class Doctor {

	/**
	 * Creates a Doctor to the system
	 * 
	 * @param name
	 * @param division
	 */
	public Doctor(String name, String division) {
		super();
	}

	public boolean create(String patientName, Nurse nurse, String input) {
		Patient patient = new Patient(patientName, patientDivision, this, nurse);
		patient.createRecord(input);
		System.out.println(GetCurrentTimeStamp.getTimeStamp() + ": " + CERTIFICATE.toString() + "created " + patient.toString() + " record.");
		return true;
	}
}
