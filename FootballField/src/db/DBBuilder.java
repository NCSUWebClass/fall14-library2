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
import java.util.Random;

/**
 * @author dfperry
 *
 */
public class DBBuilder {

	/**
	 * @param args
	 */
	private static PreparedStatement ps;
	static Connection conn;

	
	public DBBuilder(Connection conn){
		this.conn = conn;
	}
	public void makeDatabase() {
		List<String> query = new ArrayList<String>();
		//String makeDb = "CREATE DATABASE IF NOT EXISTS footballfield";
		//String useDb = "use footballfield";
		String makeTable = "CREATE TABLE IF NOT EXISTS people (" + 
				"pid int(11) NOT NULL AUTO_INCREMENT," +
				"entering int(11) NOT NULL," +
				"eventTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
				"PRIMARY KEY (`pid`)" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8";
		//query.add(makeDb);
		//query.add(useDb);
		query.add(makeTable);
		PreparedStatement ps;
		for(int i = 0; i< query.size(); i++){
			try {
				ps = conn.prepareStatement(query.get(i));
				ps.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void addData() {
		String si = "INSERT INTO people(entering) VALUES (?)";
		PreparedStatement ps;
		int j;
		for(int i = 0; i < 5; i++) {
			Random rand = new Random();	
			j = rand.nextInt(2);
			try {
				ps = conn.prepareStatement(si);
				ps.setInt(1, j);
				ps.execute();
				//System.out.println(ps.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
