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
public class Gov extends User {

	public Gov(String currentSSN, String division, Database db, BufferedReader in, PrintWriter out) {
		super(currentSSN, division, db, in, out);
	}

	/**
	 * Deletes record associated with a certain patient
	 * 
	 * @param patient
	 * @return true if successful deletion
	 */
	public boolean delete(String patientSSN) {
		List<Record> patientRecords = db.getRecords(patientSSN);

		for (int i = 1; i <= patientRecords.size(); i++) {
			out.println(i + ": " + patientRecords.get(i - 1).getId());
		}

		out.println("Choose which record you want to delete.");
		try {
			String s = in.readLine();
			int index = Integer.parseInt(s);
			if (index >= 0 && index < patientRecords.size()) {
				Record record = patientRecords.get(index - 1);
				boolean success = db.deleteRecord(patientSSN, record);
				if (success) {
					Log.append(currentSSN + " deleted " + record.getId() + " from patient " + patientSSN);
					return true;
				} else {
					Log.append(currentSSN + " tried to delete" + record.getId() + " from patient " + patientSSN + " but failed");
					return false;
				}
			} else {
				Log.append(currentSSN
						+ " entered an invalid index (" + index + ") while browsing between " + patientSSN
						+ "'s records, whilst trying to delete one of them.");
				
				out.println("No such record exist.");
			}
		} catch (Exception e) {
			Log.append(e.toString() + " was thrown, whilst trying to delete one of " + patientSSN + "'s records.");
		}
		return false;
	}
}
