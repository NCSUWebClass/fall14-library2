/**
 * 
 */
package db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author dfperry
 *
 */
public class DBBuilder {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		 try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 Connection conn = null;
		 try {
			    conn =
			    	DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "draco4prez");
			    PreparedStatement ps = conn.prepareStatement("SELECT * FROM example");
			    ResultSet rs = ps.executeQuery();
			    File file = new File("sql/setup.sql");
			    try {
			    	FileReader reader = new FileReader(file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			   
			    while(rs.next()){
			    	System.out.println(rs.getString(2));
			    }
		 }catch(SQLException se){
			 System.out.println("Woopsie");
		 }
		 
	}

}
