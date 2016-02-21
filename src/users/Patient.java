package users;

import java.io.BufferedReader;
import java.io.PrintWriter;

import database.Database;

/**
 * 
 * @author Skopal
 *
 */
public class Patient extends User {

	public Patient(String currentSSN, String division, Database db, BufferedReader in, PrintWriter out) {
		super(currentSSN, division, db, in, out);
	}
}
