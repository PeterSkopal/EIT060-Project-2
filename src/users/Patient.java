package users;

/**
 * 
 * @author Skopal
 *
 */
public class Patient extends User {
	private int SSN;
	private Doctor doctor;
	private Nurse nurse;

	public Patient(int SSN, String division, Doctor doctor, Nurse nurse, Database db) {
		super(SSN, division, db);
		this.doctor = doctor;
		this.nurse = nurse;
	}

	/**
	 * Makes sure the inserted user actually treats this certain patient.
	 * 
	 * @param user
	 * @param name
	 * @return true if user actually treats this patient.
	 */
	public boolean hasThisTreator(User user) {
		if (user.equals(doctor) || user.equals(nurse)) {
			return true;
		}
		return false;
	}
	
	public String toString() {
		return "Patients Social Security Number; " + SSN + "Patients division: " + division +  ". Patients Doctor: " + doctor.toString() + "Patients Nurse: " + nurse.toString();
	}
	
	
}
