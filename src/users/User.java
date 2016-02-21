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
	protected int currentSSN;
	protected String division;
	protected Database db;
	protected PrintWriter out;
	protected BufferedReader in;

	public int getSSN() {
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
	public User(int currentSSN, String division, Database db, BufferedReader in, PrintWriter out) {
		this.currentSSN = currentSSN;
		this.division = division;
		this.in = in;
		this.out = out;
	}

	/**
	 * Prints out one record belonging to a Patient if user has permission to
	 * read record.
	 * 
	 * @param patient
	 * @return true if the user has permission
	 */
	public boolean read(int patientSSN) {
		List<Record> patientRecords = db.getRecords(patientSSN);

		for (int i = 1; i <= patientRecords.size(); i++) {
			out.println(i + ": " + patientRecords.get(i - 1).getId());
		}

		System.out.println("Choose which record you want to read.");
		String s = in.readLine();
		int index = Integer.parseInt(s);
		if (index >= 0 && index < patientRecords.size()) {
			Record record = patientRecords.get(index - 1);
			if (record.getDoctor() == currentSSN || record.getNurse() == currentSSN
					|| record.getPatient() == currentSSN) {
				out.println(record.toString());
				Log.append(currentSSN + " read: " + record.getId() + "from" + patientSSN);
				return true;
			} else {
				Log.append(currentSSN + " tried to read: " + record.getId() + ", from " + patientSSN);

				out.println("You do not have permission. Try another patient.");
				return false;
			}
		} else {
			Log.append(currentSSN + " entered an invalid index while browsing between " + patientSSN
					+ " records, while trying to read.");

			out.println("No such record exist.");
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
	public boolean write(int patientSSN, String data) {
		List<Record> patientRecords = db.getRecords(patientSSN);

		for (int i = 1; i <= patientRecords.size(); i++) {
			out.println(i + ": " + patientRecords.get(i - 1).getId());
		}

		out.println("Choose which record you want to write.");
		String s = in.readLine();
		int index = Integer.parseInt(s);
		if (index >= 0 && index < patientRecords.size()) {
			Record record = patientRecords.get(index - 1);
			if (record.getDoctor() == currentSSN || record.getNurse() == currentSSN) {
				record.writeData(data);
				Log.append(GetCurrentTimeStamp.getTimeStamp() + ": " + currentSSN + " wrote: " + data + ", to "
						+ patientSSN);
				return true;
			} else {
				Log.append(GetCurrentTimeStamp.getTimeStamp() + ": " + currentSSN + " tried to write: " + data + ", to "
						+ patientSSN);

				out.println("You do not have permission. Try another patient.");
				return false;
			}
		} else {
			Log.append(GetCurrentTimeStamp.getTimeStamp() + ": " + currentSSN
					+ " entered an invalid index while browsing between " + patientSSN
					+ " records, while trying to write.");

			out.println("No such record exist.");
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
	public boolean create(int patientSSN, int doctorSSN, int nurseSSN, String division, String data) {
		return false;
	}

	/**
	 * Deletes record associated with a certain patient
	 * 
	 * @param patient
	 * @return true if successful deletion
	 */
	public boolean delete(int recordID) {
		Log.append(GetCurrentTimeStamp.getTimeStamp() + ": " + currentSSN + " tried to delete " + patient.toString());
		return false;
	}

	/**
	 * 
	 * @return
	 */
	private boolean saveDatabase() {
		try {
			FileOutputStream fileOut = new FileOutputStream(db.getFilePath());
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(db);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in /tmp/employee.ser");
		} catch (IOException i) {
			i.printStackTrace();
			return false;
		}
		return true;
	}
}
