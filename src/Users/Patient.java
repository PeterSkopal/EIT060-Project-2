package Users;
/**
 * 
 * @author Skopal
 *
 */
public class Patient {
	private Doctor doctor;
	private Nurse nurse;

	public Patient(String name, String division, Doctor doctor, Nurse nurse) {
		super();
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
		return "Patients name; " + name + "Patients division: " + division +  ". Patients Doctor: " + doctor.toString() + "Patients Nurse: " + nurse.toString();
	}
}
