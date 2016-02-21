package users;

import java.io.BufferedReader;
import java.io.PrintWriter;
import Database.Database;

/**
 *  
 * @author Skopal
 *
 */
public class Nurse extends User {

	/**
	 * Creates a Nurse to the system
	 * 
	 * @param name
	 * @param division
	 */
	public Nurse(int currentSSN, String division, Database db, BufferedReader in, PrintWriter out) {
		super(currentSSN, division, db, in, out);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
}
