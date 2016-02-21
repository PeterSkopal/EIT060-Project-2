package server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

public class Log {

	public static void append(String message) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("myfile.txt", true)));
			out.println(timestamp() + " - " + message);
			out.close();
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		}
	}

	private static String timestamp() {
		java.util.Date date = new java.util.Date();
		return new Timestamp(date.getTime()).toString();
	}

}
