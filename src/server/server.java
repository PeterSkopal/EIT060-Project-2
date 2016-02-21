package server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.security.KeyStore;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import javax.security.cert.X509Certificate;

import database.Database;
import users.Doctor;
import users.Gov;
import users.Nurse;
import users.Patient;
import users.User;

public class server implements Runnable {
	private ServerSocket serverSocket = null;
	private Database db = null;
	private static int numConnectedClients = 0;

	public static final String databaseFilepath = "database.ser";

	public server(ServerSocket ss) throws IOException {
		initializeDatabase();
		serverSocket = ss;
		newListener();
	}

	private boolean initializeDatabase() {
		try {
			FileInputStream fileIn = new FileInputStream(databaseFilepath);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			db = (Database) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) // If file now found. Create new.
		{
			db = new Database(databaseFilepath);
			return true;
		} catch (ClassNotFoundException c) {
			System.out.println("Database class not found");
			c.printStackTrace();
			return false;
		}
		return true;
	}

	public void run() {
		try {
			SSLSocket socket = (SSLSocket) serverSocket.accept();
			newListener();
			SSLSession session = socket.getSession();
			PrintWriter out = null;
			BufferedReader in = null;
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			X509Certificate cert = (X509Certificate) session.getPeerCertificateChain()[0];
			String subject = cert.getSubjectDN().getName();

			String ssn = getValByAttributeTypeFromIssuerDN(subject, "CN");
			String userType = getValByAttributeTypeFromIssuerDN(subject, "OU");
			String division = getValByAttributeTypeFromIssuerDN(subject, "O");
			User user = null;
			switch (userType) {
			case "Doctor":
				System.out.println("Doctor has connected");
				user = new Doctor(ssn, division, db, in, out);
				break;
			case "Nurse":
				System.out.println("Nurse has connected");
				user = new Nurse(ssn, division, db, in, out);
				break;
			case "Patient":
				System.out.println("Patient has connected");
				user = new Patient(ssn, division, db, in, out);
				break;
			case "Government":
				System.out.println("Governemt has connected");
				user = new Gov(ssn, division, db, in, out);
				break;
			default:
				System.out.println("Invalid user has connected");
				out.println("Your usertype is not valid");
				in.close();
				out.close();
				socket.close();
				numConnectedClients--;
				break;
			}
			if (user == null) {
				// Handle invalid usertype
			}

			numConnectedClients++;
			Log.append("User: " + user.getSSN() + " connected to server");
			System.out.println("client name (cert subject DN field): " + subject);
			System.out.println(numConnectedClients + " concurrent connection(s)\n");

			String clientMsg = null;
			String msgSplits[] = null;
			while ((clientMsg = in.readLine()) != null) {

				msgSplits = clientMsg.split(" ");

				if (msgSplits[0] != null) {
					switch (msgSplits[0]) {
					case "help":
						out.println(
								"Available commands:\n"
								+ "create\t<patient id>\t<nurse id>\t<data>\n"
								+ "read\t<patient id>\n"
								+ "write\t<patient id>\t<data>\n"
								+ "delete\t<patient id>\n");
						break;
					case "create":
						if (msgSplits.length < 3)
							break;
						user.create(msgSplits[1], msgSplits[2], msgSplits[3]);
						break;
					case "read":
						if (msgSplits.length < 2)
							break;
						user.read(msgSplits[1]);
						break;
					case "delete":
						if (msgSplits.length < 2)
							break;
						user.delete(msgSplits[1]);
						break;
					case "write":
						if (msgSplits.length < 2)
							break;
						user.write(msgSplits[1], msgSplits[2]);
					default:
						out.println("Unrecognized command.\n");
					}
				}
				out.flush();
				System.out.println("done\n");
			}
			in.close();
			out.close();
			socket.close();
			numConnectedClients--;
			System.out.println("client disconnected");
			System.out.println(numConnectedClients + " concurrent connection(s)\n");
		} catch (IOException e) {
			System.out.println("Client died: " + e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	private void newListener() {
		(new Thread(this)).start();
	} // calls run()

	public static void main(String args[]) {
		System.out.println("\nServer Started\n");
		int port = -1;
		if (args.length >= 1) {
			port = Integer.parseInt(args[0]);
		}
		String type = "TLS";
		try {
			ServerSocketFactory ssf = getServerSocketFactory(type);
			ServerSocket ss = ssf.createServerSocket(port);
			((SSLServerSocket) ss).setNeedClientAuth(true); // enables client
															// authentication
			new server(ss);
		} catch (IOException e) {
			System.out.println("Unable to start Server: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static ServerSocketFactory getServerSocketFactory(String type) {
		if (type.equals("TLS")) {
			SSLServerSocketFactory ssf = null;
			try { // set up key manager to perform server authentication
				SSLContext ctx = SSLContext.getInstance("TLS");
				KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
				TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
				KeyStore ks = KeyStore.getInstance("JKS");
				KeyStore ts = KeyStore.getInstance("JKS");
				char[] password = "password".toCharArray();

				ks.load(new FileInputStream("../certificates/serverkeystore"), password); // keystore
																							// password
																							// (storepass)
				ts.load(new FileInputStream("../certificates/servertruststore"), password); // truststore
																							// password
																							// (storepass)
				kmf.init(ks, password); // certificate password (keypass)
				tmf.init(ts); // possible to use keystore as truststore here
				ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
				ssf = ctx.getServerSocketFactory();
				return ssf;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return ServerSocketFactory.getDefault();
		}
		return null;
	}

	/**
	 * Gets value from DN string of X509 certificate
	 * 
	 * @param dn
	 *            DN string
	 * @param attributeType
	 *            The attribute you want to collect.
	 * @return
	 */
	private String getValByAttributeTypeFromIssuerDN(String dn, String attributeType) {
		attributeType += "=";
		String[] dnSplits = dn.split(",");
		for (String dnSplit : dnSplits) {
			if (dnSplit.contains(attributeType)) {
				String[] cnSplits = dnSplit.trim().split("=");
				if (cnSplits[1] != null) {
					return cnSplits[1].trim();
				}
			}
		}
		return "";
	}
}
