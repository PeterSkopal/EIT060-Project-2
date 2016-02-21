package users;

import java.util.List;

import Database.*;

/**
 * This class specifies a User of the system. These can be of several types:
 * Nurse, Doctor, Gov & Patient.
 * 
 * @author Skopal
 *
 */
public abstract class User {
	private int SSN;
	private String division;
	private Database db;

	public int getSSN() {
		return SSN;
	}

	public String getDivision() {
		return division;
	}

	/**
	 * Creates a User
	 * 
	 * @param name
	 * @param division
	 */
	public User(int SSN, String division, Database db) {
		this.SSN = SSN;
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
			List<Record> patientRecords = db.getRecords(patient.getSSN());
			System.out.println(patientRecords);
			LOG.append(GetCurrentTimeStamp.getTimeStamp() + ": " + CERTIFICATE.toString() + " read "
					+ patient.toString() + " record.");
			return true;
		}
		System.out.println("You do not have permission. Try another patient.");
		return false;
	}

	// /**
	// * Prints out all the records from the users associated Division.
	// *
	// * @return true if authenticated
	// */
	// public boolean readDivision() {
	// List<RECORD> myPatientsRecords =
	// RECORDS.makeAListOfEveryPatientRecordIHave(name);
	// if (myPatientsRecords.size() > 0) {
	// System.out.println(myPatientsRecords);
	// LOG.append(GetCurrentTimeStamp.getTimeStamp() + ": " +
	// CERTIFICATE.toString()
	// + " read all the records from her/his division.");
	// return true;
	// }
	// System.out.println("You do not have any patients.");
	// return false;
	// }

	/**
	 * Appends data into already existing record
	 * 
	 * @param patient
	 * @param data
	 * @return true if permission to write is allowed
	 */
	public boolean write(int patientSSN, String data) {
		List<Record> patientRecords = db.getRecords(patientSSN);

		for (int i = 1; i <= patientRecords.size(); i++) {
			System.out.println(i + ": " + patientRecords.get(i - 1).getId());
		}

		System.out.println("Choose which record you want to write.");
		String s = InputStream(); // Här får vi en siffra från inputen
		int index = Integer.parseInt(s);
		if (index >= 0 && index < patientRecords.size()) {
			Record record = patientRecords.get(index - 1);
			if (record.getDoctor() == currentSSN || record.getNurse() == currentSSN) {
				
			}
			record.writeData(data);
			LOG.append(GetCurrentTimeStamp.getTimeStamp() + ": " + CERTIFICATE.toString() + " wrote: " + data + ", to "
					+ patientSSN);
		} else {
			System.out.println("No such record exist.");
		}

		return true;
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
	public boolean create(int patientSSN, int doctorSSN, int nurseSSN, String data) {
		return false;
	}

	/**
	 * Deletes record associated with a certain patient
	 * 
	 * @param patient
	 * @return true if successful deletion
	 */
	public boolean delete(Patient patient) {
		LOG.append(GetCurrentTimeStamp.getTimeStamp() + ": " + CERTIFICATE.toString() + " tried to delete "
				+ patient.toString());
		return false;
	}

	/**
	 * Returns a string of the users information
	 */
	public abstract String toString();
}
