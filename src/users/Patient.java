package users;

import java.io.BufferedReader;
import java.io.PrintWriter;
import Database.Database;

/**
 * 
 * @author Skopal
 *
 */
public class Patient extends User {

	public Patient(int currentSSN, String division, Database db, BufferedReader in, PrintWriter out) {
		super(currentSSN, division, db, in, out);
	}

	public String toString() {
		return "Patients Social Security Number: " + currentSSN + "Patients division: " + division +  ". Patients Doctor: " + doctor.toString() + "Patients Nurse: " + nurse.toString();
	}
	
	
}
