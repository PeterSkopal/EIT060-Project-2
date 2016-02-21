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

	private HashMap<Integer, ArrayList<Record>> recordList;
	private int idCounter;
	private String databaseFilepath;

	/**
	 * Creates a new empty database consisting of an arraylist of Record
	 * objects.
	 */
	public Database(String databaseFilepath) {
		recordList = new HashMap<Integer, ArrayList<Record>>();
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
	 * @param doctor
	 *            Name of the associated doctor.
	 * @param nurse
	 *            Name of the associated nurse.
	 * @param data
	 *            Medical data string.
	 */
	public void createRecord(int patient, int doctor, int nurse, String division, String data) {
		if (recordList.get(patient) == null) {
			recordList.put(patient, new ArrayList<Record>());
		}

		recordList.get(patient).add(new Record(patient, doctor, nurse, division, data, idCounter));
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
	public boolean deleteRecord(int patient, Record r) {
		if (r != null) {
			ArrayList<Record> patientList;
			if ((patientList = recordList.get(patient)) == null)
				return false;

			patientList.remove(r);
			return true;
		}
		return false;
	}

	/**
	 * Retrieves all records belonging to a specified patient in an arraylist.
	 * 
	 * @param patientSSN
	 * @return Arraylist of all records belonging to patientName.
	 */
	public ArrayList<Record> getRecords(int patientSSN) {
		return recordList.get(patientSSN); // Return list of all record
											// belonging to specified patient.
	}
}
