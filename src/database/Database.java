package database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A serializable database containing a list of medical records.
 *
 * @author M�ns
 *
 */
public class Database implements Serializable {

	private HashMap<String, ArrayList<Record>> recordList;
	private int idCounter;
	private String databaseFilepath;

	/**
	 * Creates a new empty database consisting of an arraylist of Record
	 * objects.
	 */
	public Database(String databaseFilepath) {
		recordList = new HashMap<String, ArrayList<Record>>();
		idCounter = 0;
		this.databaseFilepath = databaseFilepath;
	}

	/**
	 * Filepath for saving on file.
	 *
	 * @return Returns filepath as a String.
	 */
	public String getFilePath() {
		return databaseFilepath;
	}

	/**
	 * Creates a record for a patient.
	 *
	 * @param patientName
	 *            Name of the patient the record belong to.
	 * @param doctorSSN
	 *            Name of the associated doctor.
	 * @param nurseSSN
	 *            Name of the associated nurse.
	 * @param data
	 *            Medical data string.
	 */
	public void createRecord(String patientSSN, String doctorSSN, String nurseSSN, String division, String data) {
		if (recordList.get(patientSSN) == null) {
			recordList.put(patientSSN, new ArrayList<Record>());
		}

		recordList.get(patientSSN).add(new Record(patientSSN, doctorSSN, nurseSSN, division, data, idCounter));
		idCounter++;
	}

	/**
	 * Tries to find the record with the specified record id. If found the
	 * record will be deleted from the database.
	 *
	 * @param id
	 *            Record id of the record to be deleted.
	 * @return Returns true if specified record is deleted. If not deleted false
	 *         is returned.
	 */
	public boolean deleteRecord(String patientSSN, Record r) {
		if (r != null) {
			ArrayList<Record> patientList = recordList.get(patientSSN);
			if (patientList.contains(r)) {
				patientList.remove(r);
				if(patientList.isEmpty()) {
					recordList.remove(patientSSN);
				}
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Retrieves all records belonging to a specified patient in an arraylist.
	 *
	 * @param patientSSN
	 * @return Arraylist of all records belonging to patientName.
	 */
	public ArrayList<Record> getRecords(String patientSSN) {
		return recordList.get(patientSSN); // Return list of all record
											// belonging to specified patient.
	}
}
