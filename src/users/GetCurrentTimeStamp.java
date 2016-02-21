package users;

import java.sql.Timestamp;
/**
 * 
 * @author Skopal
 *
 */
public class GetCurrentTimeStamp {
    public static String getTimeStamp() {
        java.util.Date date= new java.util.Date();
    	return new Timestamp(date.getTime()).toString();
    }
}
