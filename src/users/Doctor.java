package users;

import java.io.BufferedReader;
import java.io.PrintWriter;

import database.Database;

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
	public Doctor(int currentSSN, String division, Database db, BufferedReader in, PrintWriter out) {
		super(currentSSN, division, db, in, out);
	}

	public boolean create(String patientName, Nurse nurse, String input) {
		Patient patient = new Patient(patientName, patientDivision, this, nurse);
		patient.createRecord(input);
		System.out.println(currentSSN + "created " + patient.toString() + " record.");
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
}
