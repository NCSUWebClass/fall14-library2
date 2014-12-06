


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.naming.InitialContext;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.*;
import javax.sql.DataSource;
import java.sql.*;
/**
 * Servlet implementation class WebClassServlet
 */
@WebServlet("/WebClassServlet/*")
public class WebClassServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int MAX_CAPACITY = 100;
	private int numOfPeople;
    private Connection conn; 
    private static final String SELECT_STATEMENT = "SELECT pid, entering, eventTime FROM People WHERE eventTime < ? AND eventTime > ?";
    PreparedStatement select = null;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
	/**
     * Default constructor. 
     */
    public WebClassServlet() {
    	/**************************************************
    	 * The below is commented out because its kind of useless until we get the Arduino
    	 * working. It connects to a database and gets data from it based on current time (so it gets
    	 * all data before it). Dun dun dun...
    	 * 
    	 **************************************************/
    	
    	conn = getConnection();
    	makeDatabase();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*
		response.setContentType("application/json");
		PrintWriter out  = response.getWriter();
		String jsonObj = "{numOfPeople:" + numOfPeople + "}";
		System.out.println("Send json object");
		out.print(jsonObj);
		
		*/
		String requestParam = request.getParameter("CurrentCapacity");
		
		
		
		int currentCapacity = Integer.parseInt(requestParam);
		/*
		 * numOfPeople = numOfPeople - currentCapacity;
		 */
		numOfPeople = setEnterExit(currentCapacity);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.write(""+ numOfPeople);
		
	}

	private int setEnterExit(int currentCapacity) {
		Random rand = new Random();
		/* How we should do it 
		int result = rand.nextInt(MAX_CAPACITY) - currentCapacity;
		*/
		int range = MAX_CAPACITY - currentCapacity;
		int result = rand.nextInt(range) - 13;
		return result;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	private Connection getConnection(){
		try {
			InitialContext ic = new InitialContext();
			DataSource ds = (DataSource) ic.lookup("java:/comp/env/jdbc/footballfield");
			if(ds == null){
				System.out.println("Error with DataSource");
			}
			return ds.getConnection();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void makeDatabase() {
		List<String> query = new ArrayList<String>();
		String makeDb = "CREATE DATABASE IF NOT EXISTS footballfield";
		String useDb = "use footballfield";
		String makeTable = "CREATE TABLE IF NOT EXISTS people (" + 
				"pid int(11) NOT NULL AUTO_INCREMENT," +
				"entering int(11) NOT NULL," +
				"eventTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
				"PRIMARY KEY (`pid`)" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8";
		query.add(makeDb);
		query.add(useDb);
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
	
	private void addData() {
		String si = "INSERT INTO people(entering) VALUES (?)";
		PreparedStatement ps;
		int j;
		for(int i = 0; i < 5; i++) {
			Random rand = new Random();	
			j = rand.nextInt(1);
			try {
				ps = conn.prepareStatement(si);
				ps.setInt(1, j);
				ps.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	private int getEnteringExiting(String oldDate){
		
		int entering=0 , exiting =0;
		java.util.Date date = new Date();
		String curDate = df.format(date);
    	try{
    		select = conn.prepareStatement(SELECT_STATEMENT);
    		select.setString(1, curDate);
    		select.setString(2, oldDate);
    		System.out.println(select.toString());
    		ResultSet rs = select.executeQuery();
    		while(rs.next()){
    			int i = rs.getInt(2);
    			if(i == 1){
    				entering++;
    			}
    			else{
    				exiting++;
    			}
    		}
    		numOfPeople =  entering - exiting;
    		System.out.println(numOfPeople);
    	}catch(SQLException se){
    		System.out.println("SQL Error");
    	}
		return numOfPeople;
	}
}
