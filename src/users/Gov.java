package users;

import java.io.BufferedReader;
import java.io.PrintWriter;

import Database.Database;

/**
 * 
 * @author Skopal
 *
 */
public class Gov extends User {

	public Gov(int currentSSN, String division, Database db, BufferedReader in, PrintWriter out) {
		super(currentSSN, division, db, in, out);
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
