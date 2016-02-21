package users;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.List;

import database.Database;
import database.Record;
import server.Log;

/**
 * 
 * @author Skopal
 *
 */
public class Patient extends User {

	public Patient(String currentSSN, String division, Database db, BufferedReader in, PrintWriter out) {
		super(currentSSN, division, db, in, out);
	}
	
	public boolean read(String patientSSN) {
		listAllRecords(patientSSN);
		List<Record> patientRecords = db.getRecords(patientSSN);
		try {
			String s = in.readLine();
			int index = Integer.parseInt(s);
			if (index > 0 && index <= patientRecords.size()) {
				Record record = patientRecords.get(index - 1);
				if (record.getPatient().equals(currentSSN)) {
					out.println(record.toString());
					Log.append("User: " + currentSSN + " read record id: " + record.getId() + ", from patient id: " + patientSSN);
					return true;
				} else {
					Log.append("User: " + currentSSN + " tried to read record id: " + record.getId() + ", from patient id: " + patientSSN);
					out.println("You do not have permission. Try another patient.\n");
					return false;
				}
			} else {
				Log.append(currentSSN + " entered an invalid index while browsing between " + patientSSN
						+ " records, while trying to read.");
				
				out.println("No such record exist.");
				return false;
			}
		} catch (Exception e) {
			Log.append(e.toString() + " was thrown, whilst user id: " + currentSSN + "trying to read patient id: " + patientSSN + "'s records.");
			return false;
		}
	}
	
	@Override
	public boolean write(String patientSSN, String data) {
		Log.append("User: " + currentSSN + " tried to write on a record of patient id: " + patientSSN);
		return false;
	}
}
