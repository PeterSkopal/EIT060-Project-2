package database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A serializable database containing a list of medical records.
 * @author M�ns
 *
 */
public class Database implements Serializable {

	private HashMap<Integer,ArrayList<Record>> recordList;
	private int idCounter;
	
	/**
	 * Creates a new empty database consisting of an arraylist of Record objects.
	 */
	public Database() {
		recordList = new HashMap<Integer, ArrayList<Record>>();
		idCounter = 0;
	}
	
	/**
	 * Creates a record for a patient.
	 * @param patientName Name of the patient the record belong to.
	 * @param doctor Name of the associated doctor.
	 * @param nurse Name of the associated nurse.
	 * @param data Medical data string.
	 */
	public void createRecord(int patient, int doctor, int nurse, String data) {
		if(recordList.get(patient) == null) {
			recordList.put(patient, new ArrayList<Record>());
		}
		
		recordList.get(patient).add(new Record(patient, doctor, nurse, data, idCounter));
		idCounter++;
	}
	
//	/**
//	 * Retrieves the record from a specified patient with the specified record id.
//	 * @param id
//	 * @return Returns the record object if found within the database. If not found null is returned.
//	 */
//	public Record getRecordById(int patient, int id) {
//		for(Record r: recordList.get(patient)) {
//			if(r.getId() == id) {
//				return r;
//			}
//		}
//		return null;
//	}
	
	/**
	 * Tries to find the record with the specified record id. If found the record will be deleted from the database.
	 * @param id Record id of the record to be deleted.
	 * @return Returns true if specified record is deleted. If not deleted false is returned.
	 */
	public boolean deleteRecord(int patient, Record r) {
		if(r != null) {
			ArrayList<Record> patientList;
			if((patientList = recordList.get(patient)) == null)
				return false;
			
			patientList.remove(r);
			return true;
		}
		return false;
	}
	
	/**
	 * Retrieves all records belonging to a specified patient in an arraylist.
	 * @param patientSSN
	 * @return Arraylist of all records belonging to patientName.
	 */
	public ArrayList<Record> getRecords(int patientSSN) {
		return recordList.get(patientSSN); //Return list of all record belonging to specified patient.
	}
}