


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

import db.DBBuilder;

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
    private static final String SELECT_STATEMENT = "SELECT pid, entering, eventTime FROM People WHERE timediff(eventTime,?) > 0";
    PreparedStatement select = null;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
    private DBBuilder db;
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
    	//makeDatabase();
    	conn = getConnection();
    	db = new DBBuilder(conn);
    	db.makeDatabase();
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
		String time = request.getParameter("time");
		long temp = Long.parseLong(time);
		Date d = new Date(temp);
		db.addData();
		getEnteringExiting(temp);	
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
	
	/*
	private void addData() {
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
	*/
	private int getEnteringExiting(long oldDate){
		
		int entering=0 , exiting =0;
		/*
		java.util.Date date = new Date();
		String curDate = df.format(date);
		*/
		try{
    		select = conn.prepareStatement(SELECT_STATEMENT);
    		java.sql.Timestamp date = new java.sql.Timestamp(oldDate - 1000);
    		select.setTimestamp(1, date);
    		ResultSet rs = select.executeQuery();
    		while(rs.next()){
    			int i = rs.getInt("entering");
    			if(i == 1){
    				entering++;
    			}
    			else{
    				exiting++;
    			}
    		}
    		
    		numOfPeople =  entering - exiting;
    	}catch(SQLException se){
    		System.out.println("SQL Error");
    	}
		return numOfPeople;
	}
}
