package ctrl;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Loan;

/**
 * Servlet implementation class Start
 */
@WebServlet({"/Start","/Startup/*"})
public class Start extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Loan model;

	/**
	* Default constructor.
	*/
	public Start() {
		// TODO Auto-generated constructor stub
	}

	public void init(ServletConfig config) throws ServletException {
		model = new Loan();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/UI.jspx").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get Servlet Context that contains parameters
		ServletContext context = request.getServletContext();

		String principal = context.getInitParameter("principal");
		String interest = context.getInitParameter("interest");
		String fixedInterest = context.getInitParameter("fixedInterest");
		String period = context.getInitParameter("period");
		String gracePeriod = context.getInitParameter("gracePeriod");
		String grace = context.getInitParameter("grace");

		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = params.nextElement();
			String value = request.getParameter(param);

			if (value.equals("")){
				// Error case? Value is empty
			} else if (param.equals("principal")) {
				principal = value;
			} else if (param.equals("interest")) {
				interest = value;
			} else if (param.equals("period")) {
				period = value;
			} else if (param.equals("grace")) {
				grace = value;
			}
		}

		try {
			double graceInterest = 0.0;
			double payment = 0.0;

			graceInterest = grace.equals("on") ? model.computeGraceInterest(principal, gracePeriod, interest, fixedInterest) : 0.0;
			payment = model.computePayment(principal, period, interest, String.valueOf(graceInterest), gracePeriod, fixedInterest);

			context.setAttribute("graceInterest", graceInterest);
			context.setAttribute("payment", payment);

			request.getRequestDispatcher("/Results.jspx").forward(request, response);
		} catch (Exception e) {
			request.setAttribute("errorMsg", e.getMessage());
			request.getRequestDispatcher("/UI.jspx").forward(request, response);
		}
	}

}