/**
 * added.
 */
package db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dfperry
 *
 */
public class DBBuilder {

	/**
	 * @param args
	 */
	private static PreparedStatement ps;
	private static List<String> queries;
	private static String mySQLDriver = "com.mysql.jdbc.Driver";
	private static String url  = "jdbc:mysql://localhost/test";
	private static String user = "root";
	private static String password = "draco4prez";

	
	
	public static void main(String[] args) {
		 try {
			Class.forName(mySQLDriver).newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 Connection conn = null;
		 try {
			    conn =
			    	DriverManager.getConnection(url, user, password);
			    try {
			    	queries = parseSQLFile("sql/setup.sql");
			    	for(int i =0; i < queries.size(); i++){
			    		String s = queries.get(i);
			    		PreparedStatement ps = conn.prepareStatement(queries.get(i));
			    		ps.execute();
			    	}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 }catch(SQLException se){
			 System.out.println("Woopsie");
		 }
		 
	}
	private static List<String> parseSQLFile(String filepath) throws FileNotFoundException, IOException {
		List<String> queries = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)));
		String line = "";
		String currentQuery = "";
		while ((line = reader.readLine()) != null) {
			for (int i = 0; i < line.length(); i++) {
				if (line.charAt(i) == ';') {
					queries.add(currentQuery);
					currentQuery = "";
				} else
					currentQuery += line.charAt(i);
			}
		}
		reader.close();
		return queries;
	}


}
