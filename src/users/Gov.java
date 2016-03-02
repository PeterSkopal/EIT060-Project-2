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

	public boolean read(String patientSSN) {
		if(!listAllRecords(patientSSN)) {
			return false;
		}
		List<Record> patientRecords = db.getRecords(patientSSN);
		try {
			String s = in.readLine();
			int index = Integer.parseInt(s);
			if (index > 0 && index <= patientRecords.size()) {
				Record record = patientRecords.get(index - 1);
				out.println(record.toString());
				Log.append("User: " + currentSSN + " read record id: " + record.getId() + ", from patient id: "
						+ patientSSN);
				return true;
			} else {
				Log.append(currentSSN + " entered an invalid index while browsing between " + patientSSN
						+ " records, while trying to read.");

				out.println("No such record exist.");
				return false;
			}
		} catch (Exception e) {
			Log.append(e.toString() + " was thrown, whilst user id: " + currentSSN + "trying to read patient id: "
					+ patientSSN + "'s records.");
			return false;
		}
	}

	/**
	 * Deletes record associated with a certain patient
	 *
	 * @param patient
	 * @return true if successful deletion
	 */
	public boolean delete(String patientSSN) {
		if(!listAllRecords(patientSSN)) {
			return false;
		}
		List<Record> patientRecords = db.getRecords(patientSSN);
		try {
			String s = in.readLine();
			int index = Integer.parseInt(s);
			if (index > 0 && index <= patientRecords.size()) {
				Record record = patientRecords.get(index - 1);
				boolean success = db.deleteRecord(patientSSN, record);
				saveDatabase();
				if (success) {
					out.println("Successfully deleted record");
					Log.append(currentSSN + " deleted " + record.getId() + " from patient " + patientSSN);
					return true;
				} else {
					out.println("You do not have access to delete that record");
					Log.append(currentSSN + " tried to delete" + record.getId() + " from patient " + patientSSN
							+ " but failed");
					return false;
				}
			} else {
				Log.append(currentSSN + " entered an invalid index (" + index + ") while browsing between " + patientSSN
						+ "'s records, whilst trying to delete one of them.");

				out.println("No such record exist.");
			}
		} catch (Exception e) {
			Log.append(e.toString() + " was thrown, whilst trying to delete one of " + patientSSN + "'s records.");
		}
		return false;
	}

	@Override
	public boolean write(String patientSSN, String data) {
		out.println("You do not have access to write to records");
		Log.append("User: " + currentSSN + " tried to write on a record of patient id: " + patientSSN);
		return false;
	}
}
