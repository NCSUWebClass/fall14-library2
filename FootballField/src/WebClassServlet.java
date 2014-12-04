

import java.io.IOException;
import java.io.PrintWriter;
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
	private static final String DB_URL = "jdbc:mysql://localhost:3306/test";
	private static final String USER_AND_PASS = "user=Football&password=field";
    private Connection conn; 
    private static final String SELECT_STATEMENT = "SELECT * FROM ?";
    PreparedStatement select = null;
	/**
     * Default constructor. 
     */
    public WebClassServlet() {
    	conn = getConnection();
    	try{
    		select = conn.prepareStatement(SELECT_STATEMENT);
    		select.setString(1, "Example");
    		ResultSet rs = select.executeQuery();
    		while(rs.next()){
    			String s = rs.getString(1);
    			System.out.println(s);
    		}
    	}catch(SQLException se){
    		System.out.println("SQL Error");
    	}
    	
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
			DataSource ds = (DataSource) ic.lookup("java:/comp/env/jdbc/ff");
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

}
