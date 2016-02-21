package server;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

import users.Doctor;
import users.Gov;
import users.Nurse;
import users.Patient;
import users.User;

public class server implements Runnable {
    private ServerSocket serverSocket = null;
    private static int numConnectedClients = 0;

    public server(ServerSocket ss) throws IOException {
        serverSocket = ss;
        newListener();
    }

    public void run() {
        try {
            SSLSocket socket=(SSLSocket)serverSocket.accept();
            newListener();
            SSLSession session = socket.getSession();
            PrintWriter out = null;
            BufferedReader in = null;
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            X509Certificate cert = (X509Certificate)session.getPeerCertificateChain()[0];
            String subject = cert.getSubjectDN().getName();   
            
            int ssn = Integer.parseInt(getValByAttributeTypeFromIssuerDN(subject,"CN"));
            String userType = getValByAttributeTypeFromIssuerDN(subject,"OU");
            String division = getValByAttributeTypeFromIssuerDN(subject,"O");
            User user;
            switch (userType) {
            	case "Doctor":
            		user = new Doctor(ssn, division, db);
            		break;
            	case "Nurse":
            		user = new Nurse(ssn, division, db);
            		break;
            	case "Patient":
            		user = new Patient(ssn, division, db);
            		break;
            	case "Government":
            		user = new Gov(ssn, division, db);
            		break;
            	default:
            		out.println("Your usertype is not valid");
            		in.close();
            		out.close();
            		socket.close();
            		numConnectedClients--;
            		break;
            }
            
    	    numConnectedClients++;
            System.out.println("client connected");
            System.out.println("client name (cert subject DN field): " + subject);
            System.out.println(numConnectedClients + " concurrent connection(s)\n");

            String clientMsg = null;
            String msgSplits[] = null;
            while ((clientMsg = in.readLine()) != null) {
			    
            	msgSplits = clientMsg.split(" ");
            	
            	if(msgSplits[0] != null) {
	            	switch(msgSplits[0]) {
				    case "help":
				    	out.println("Available commands:\ncreate <patient id> <nurse id> <doctor id> <data>\nread <patient id>\nwrite <patient id> <data>\ndelete <patient id>\ndelete");
				    	break;
				    case "create":
				    	if(msgSplits.length < 5)
				    		break;
				    	user.create(Integer.parseInt(msgSplits[1]), Integer.parseInt(msgSplits[2]),
				    				Integer.parseInt(msgSplits[3]), Integer.parseInt(msgSplits[4]));
				    	break;
				    case "read":
				    	if(msgSplits.length < 2)
				    		break;
				    	user.read(Integer.parseInt(msgSplits[1]));
				    	break;
				    case "delete":
				    	if(msgSplits.length < 2)
				    		break;
				    	user.delete(Integer.parseInt(msgSplits[1]));
				    	break;
				    case "write":
				    	if(msgSplits.length < 2)
				    		break;
				    	user.write(msgSplits[2], Integer.parseInt(msgSplits[1]));
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

    private void newListener() { (new Thread(this)).start(); } // calls run()

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
            ((SSLServerSocket)ss).setNeedClientAuth(true); // enables client authentication
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

                ks.load(new FileInputStream("../certificates/serverkeystore"), password);  // keystore password (storepass)
                ts.load(new FileInputStream("../certificates/servertruststore"), password); // truststore password (storepass)
                kmf.init(ks, password); // certificate password (keypass)
                tmf.init(ts);  // possible to use keystore as truststore here
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
     * @param dn DN string 
     * @param attributeType The attribute you want to collect. 
     * @return
     */
    private String getValByAttributeTypeFromIssuerDN(String dn, String attributeType)
    {
    	attributeType += "=";
        String[] dnSplits = dn.split(","); 
        for (String dnSplit : dnSplits) 
        {
            if (dnSplit.contains(attributeType)) 
            {
                String[] cnSplits = dnSplit.trim().split("=");
                if(cnSplits[1]!= null)
                {
                    return cnSplits[1].trim();
                }
            }
        }
        return "";
    }
}
