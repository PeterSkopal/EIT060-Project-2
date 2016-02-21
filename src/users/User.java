package users;

import java.util.List;

/**
 * This class specifies a User of the system. These can be of several types:
 * Nurse, Doctor, Gov & Patient.
 * 
 * @author Skopal
 *
 */
public abstract class User {
	private String name;
	private String division;

	/**
	 * Creates a User
	 * 
	 * @param name
	 * @param division
	 */
	public User(String name, String division) {
		this.name = name;
		this.division = division;
	}

	/**
	 * Prints out patients record if user has permission to read record.
	 * 
	 * @param patient
	 * @return true if the user has permission
	 */
	public boolean read(Patient patient) {
		if (patient.hasThisTreator(CERTIFICATE.typeOfUser())) {
			RECORD patientRecord = RECORDS.get(patient).record();
			System.out.println(patientRecord);
			LOG.append(GetCurrentTimeStamp.getTimeStamp() + ": " + CERTIFICATE.toString() + " read "
					+ patient.toString() + " record.");
			return true;
		}
		System.out.println("You do not have permission. Try another patient.");
		return false;
	}

	/**
	 * Prints out all the records from the users associated Division.
	 * 
	 * @return true if authenticated
	 */
	public boolean readDivision() {
		List<RECORD> myPatientsRecords = RECORDS.makeAListOfEveryPatientRecordIHave(name);
		if (myPatientsRecords.size() > 0) {
			System.out.println(myPatientsRecords);
			LOG.append(GetCurrentTimeStamp.getTimeStamp() + ": " + CERTIFICATE.toString()
					+ " read all the records from her/his division.");
			return true;
		}
		System.out.println("You do not have any patients.");
		return false;
	}

	/**
	 * Appends the input into already existing record
	 * 
	 * @param input
	 * @return true if permission to write is allowed
	 */
	public boolean write(String input, Patient patient) {
		if (patient.hasThisTreator(CERTIFICATE.typeOfUser())) {
			RECORD patientRecord = RECORDS.appendToRecord(patient, input);
			LOG.append(GetCurrentTimeStamp.getTimeStamp() + ": " + CERTIFICATE.toString() " wrote: " + input + ", to " + patient.toString);
			return true;
		}
		System.out.println("You do not have permission. Try another patient.");
		return false;
	}

	/**
	 * Creates a completely new record associated with a certain patient if not
	 * already existing. It is only doctors that can do this.
	 * 
	 * @param patient
	 * @param nurse
	 *            that is associated with the perticular patient
	 * @param input
	 * @return true if successful
	 */
	public boolean create(String patientName, Nurse nurse, String input) {
		return false;
	}

	/**
	 * Deletes record associated with a certain patient
	 * 
	 * @param patient
	 * @return true if successful deletion
	 */
	public boolean delete(Patient patient) {
		return false;
	}

	/**
	 * Returns a string of the users information
	 */
	public abstract String toString();
}
