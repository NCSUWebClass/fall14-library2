

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.http.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.*;

/**
 * Servlet implementation class WebClassServlet
 */
@WebServlet("/WebClassServlet/*")
public class WebClassServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int MAX_CAPACITY = 100;
	private int numOfPeople;
    /**
     * Default constructor. 
     */
    public WebClassServlet() {
        // TODO Auto-generated constructor stub
    	
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

}
