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
	public Doctor(String currentSSN, String division, Database db, BufferedReader in, PrintWriter out) {
		super(currentSSN, division, db, in, out);
	}

	public boolean create(String patientSSN, String nurseSSN, String input) {
		db.createRecord(patientSSN, currentSSN, nurseSSN, division, input);
		saveDatabase();
		out.println((currentSSN + " created medical record for patient " + patientSSN));
		return true;
	}
}
