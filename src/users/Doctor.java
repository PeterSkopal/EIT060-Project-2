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
				if (record.getDoctor().equals(currentSSN) || record.getDivision().equals(division)) {
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

	public boolean create(String patientSSN, String nurseSSN, String input) {
		db.createRecord(patientSSN, currentSSN, nurseSSN, division, input);
		saveDatabase();
		out.println((currentSSN + " created medical record for patient " + patientSSN));
		Log.append("Doctor: " + currentSSN + " created medical record for patient " + patientSSN);
		return true;
	}

	@Override
	public boolean write(String patientSSN, String data) {
		listAllRecords(patientSSN);
		List<Record> patientRecords = db.getRecords(patientSSN);
		try {
			String s = in.readLine();
			int index = Integer.parseInt(s);
			if (index > 0 && index <= patientRecords.size()) {
				Record record = patientRecords.get(index - 1);
				if (record.getDoctor().equals(currentSSN)) {
					record.writeData(data);
					saveDatabase();
					Log.append("User: " + currentSSN + " wrote data: " + data + ", to record id: " + record.getId() + ", to patient id: "
							+ patientSSN);
					out.println("Successfully updated record");
					return true;
				} else {
					Log.append("User: " + currentSSN + " tried to write: " + data + ", to record id: " + record.getId() + ", to patient id: "
							+ patientSSN);

					out.println("You do not have permission. Try another patient.");
					return false;
				}
			} else {
				Log.append(currentSSN
						+ " entered an invalid index while browsing between " + patientSSN
						+ " records, while trying to write.");

				out.println("No such record exist.");
				return false;
			}
		} catch (Exception e) {
			Log.append(e.toString() + " was thrown, whilst writing to " + patientSSN + "'s records.");
			return false;
		}
	}
}
