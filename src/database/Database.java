package database;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A serializable database containing a list of medical records.
 * @author Måns
 *
 */
public class Database implements Serializable {

	private ArrayList<Record> recordList;
	private int idCounter;
	
	/**
	 * Creates a new empty database consisting of an arraylist of Record objects.
	 */
	public Database() {
		recordList = new ArrayList<Record>();
		idCounter = 0;
	}
	
	/**
	 * Craetes a 
	 * @param patientName Name of the patient the record belong to.
	 * @param doctor Name of the associated doctor.
	 * @param nurse Name of the associated nurse.
	 * @param data Medical data string.
	 */
	public void createRecord(String patientName, String doctor, String nurse, String data) {
		recordList.add(new Record(patientName, doctor, nurse, data, idCounter));
		idCounter++;
	}
	
	/**
	 * Retrieves the record with the specified id.
	 * @param id
	 * @return Returns the record object if found within the database. If not found null is returned.
	 */
	public Record getRecordById(int id) {
		for(Record r: recordList) {
			if(r.getId() == id) {
				return r;
			}
		}
		return null;
	}
	
	/**
	 * Tries to find the record with the specified record id. If found the record will be deleted from the database.
	 * @param id Record id of the record to be deleted.
	 * @return Returns true if specified record is deleted. If not deleted false is returned.
	 */
	public boolean deleteRecord(int id) {
		Record tempRecord = getRecordById(id);
		if(tempRecord != null) {
			recordList.remove(tempRecord);
			return true;
		}
		return false;
	}
	
	/**
	 * Retrieves all records belonging to a specified patient in an arraylist.
	 * @param patientName
	 * @return Arraylist of all records belonging to patientName.
	 */
	public ArrayList<Record> getRecords(String patientName) {
		ArrayList<Record> tempList = new ArrayList<Record>(); //Create empty record list.
		
		for(Record r: recordList) { //Iterate through all records in database.
			if(r.getPatient() == patientName) //If record belong to specified patient:
				tempList.add(r); //Add record to list.
		}
		return tempList; //Return list of all record belonging to specified patient.
	}
}
