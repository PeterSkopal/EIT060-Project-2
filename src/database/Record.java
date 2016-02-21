package database;

/**
 * A medical record for a specific patient. A nurse and a doctor is associated
 * with the record. The record contains medical data which can be appended(?) by
 * the associated nurse and doctor. Simple data structure. Contains get
 * functions to retrieve data and a function to append(?) data. Each record has
 * an unique record id.
 * 
 * @author M�ns
 *
 */
public class Record {

	int id;
	private int patientSSN, doctorSSN, nurseSSN;
	private String division;
	private String data;

	public int getPatient() {
		return patientSSN;
	}

	public int getDoctor() {
		return doctorSSN;
	}

	public int getNurse() {
		return nurseSSN;
	}

	public String getDivision() {
		return division;
	}

	public String getData() {
		return data;
	}

	public int getId() {
		return id;
	};

	/**
	 * Appends a data string to the medical data string.
	 * 
	 * @param data
	 */
	public void writeData(String data) {
		this.data += data;
	}

	/**
	 * Creates a new Record file with the specified parameters.
	 * 
	 * @param patient
	 * @param doctor
	 * @param nurse
	 * @param data
	 */
	public Record(int patient, int doctor, int nurse, String data, int id) {
		this.patientSSN = patient;
		this.doctorSSN = doctor;
		this.nurseSSN = nurse;
		this.data = data;
		this.id = id;
	}

	public String toString() {
		String s = "Record ID: " + id + "\nPatient: " + patientSSN + "\nDivision: " + division
				+ "\nResponsible Doctor: " + doctorSSN + "\nResponsoble Nurse: " + nurseSSN + "\nInformation: " + data;
		return s;
	}
}
