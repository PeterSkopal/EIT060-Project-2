package database;

/**
 * A medical record for a specific patient. A nurse and a doctor is associated with the record.
 * The record contains medical data which can be appended(?) by the associated nurse and doctor.
 * Simple data structure. Contains get functions to retrieve data and a function to append(?) data.
 * Each record has an unique record id.
 * @author Måns
 *
 */
public class Record {
	
	int id;
	private int patient, doctor, nurse;
	private String division;
	private String data;
	
	public int getPatient() { return patient; }
	public int getDoctor() { return doctor; }
	public int getNurse() { return nurse; }
	public String getDivision() { return division; }
	public String getData() { return data; }
	public int getId() { return id; };
	
	/**
	 * Appends a data string to the medical data string.
	 * @param data
	 */
	public void writeData(String data) {
		this.data += data; //Hur skall detta implementeras?
	}
	
	/**
	 * Creates a new Record file with the specified parameters.
	 * @param patient
	 * @param doctor
	 * @param nurse
	 * @param data
	 */
	public Record(int patient, int doctor, int nurse, String data, int id) {
		this.patient = patient;
		this.doctor = doctor;
		this.nurse = nurse;
		this.data = data;
		this.id = id;
	}
}
