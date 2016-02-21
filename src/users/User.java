package users;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.List;

import database.Database;
import database.Record;
import server.Log;

/**
 * This class specifies a User of the system. These can be of several types:
 * Nurse, Doctor, Gov & Patient.
 * 
 * @author Skopal
 *
 */
public abstract class User {
	protected String currentSSN;
	protected String division;
	protected Database db;
	protected PrintWriter out;
	protected BufferedReader in;

	public String getSSN() {
		return currentSSN;
	}

	public String getDivision() {
		return division;
	}

	/**
	 * Creates a User
	 * 
	 * @param currentSSN
	 * @param division
	 * @param dataBase
	 */
	public User(String currentSSN, String division, Database db, BufferedReader in, PrintWriter out) {
		this.currentSSN = currentSSN;
		this.division = division;
		this.in = in;
		this.out = out;
		this.db = db;
	}

	/**
	 * Prints out one record belonging to a Patient if user has permission to
	 * read record.
	 * 
	 * @param patient
	 * @return true if the user has permission
	 */
	public boolean read(String patientSSN) {
		List<Record> patientRecords = db.getRecords(patientSSN);
		if(patientRecords == null) {
			out.println("No records found");
			return false;
		}
		out.print("Choose which record you want to read.\n");
		
		for (int i = 1; i <= patientRecords.size(); i++) {
			out.print(i + "|\tDoctor: " + patientRecords.get(i - 1).getDoctor() + "\tDivision: " + patientRecords.get(i - 1).getDivision() + "\n");
		}
		out.println();

		try {
			String s = in.readLine();
			int index = Integer.parseInt(s);
			if (index > 0 && index <= patientRecords.size()) {
				Record record = patientRecords.get(index - 1);
				if (record.getDoctor().equals(currentSSN) || record.getNurse().equals(currentSSN)
						|| record.getPatient().equals(currentSSN)) {
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

	/**
	 * Appends data into already existing record
	 * 
	 * @param patient
	 * @param data
	 * @return true if permission to write is allowed
	 */
	public boolean write(String patientSSN, String data) {
		List<Record> patientRecords = db.getRecords(patientSSN);

		out.println("Choose which record you want to write.");
		for (int i = 1; i <= patientRecords.size(); i++) {
			out.println(i + ": " + patientRecords.get(i - 1).getId());
		}

		try {
			String s = in.readLine();
			int index = Integer.parseInt(s);
			if (index >= 0 && index < patientRecords.size()) {
				Record record = patientRecords.get(index - 1);
				if (record.getDoctor().equals(currentSSN) || record.getNurse().equals(currentSSN)) {
					record.writeData(data);
					saveDatabase();
					Log.append("User: " + currentSSN + " wrote data: " + data + ", to record id: " + record.getId() + ", to patient id: "
							+ patientSSN);
					return true;
				} else {
					Log.append(currentSSN + " tried to write: " + data + ", to record id: " + record.getId() + ", to patient id: "
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
	public boolean create(String patientSSN, String nurseSSN, String data) {
		out.println("You don't have enough permissions to create a medical record.\n");
		return false;
	}

	/**
	 * Deletes record associated with a certain patient
	 * 
	 * @param patient
	 * @return true if successful deletion
	 */
	public boolean delete(String recordID) {
		out.println("You do not have permission to delete a record\n");
		Log.append(currentSSN + " tried to delete " + recordID);
		return false;
	}

	/**
	 * 
	 * @return
	 */
	protected boolean saveDatabase() {
		try {
			FileOutputStream fileOut = new FileOutputStream(db.getFilePath());
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(db);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in " + db.getFilePath());
		} catch (IOException i) {
			i.printStackTrace();
			return false;
		}
		return true;
	}
}
