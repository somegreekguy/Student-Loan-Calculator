package ctrl;

import java.io.IOException;
import java.io.Writer;
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
		String fixedInterest = context.getInitParameter("fixedInterest");
		String gracePeriod = context.getInitParameter("gracePeriod");
		String principal = "", interest = "", period = "", grace = "";

		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = params.nextElement();
			String value = request.getParameter(param);

			if (param.equals("principal")) {
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
			double graceInterest = grace.equals("on") ? model.computeGraceInterest(principal, gracePeriod, interest, fixedInterest) : 0.0;
			double payment = model.computePayment(principal, period, interest, String.valueOf(graceInterest),
					(grace.equals("on") ? gracePeriod : "-1"), fixedInterest);

			request.getSession().setAttribute("principal", principal);
			context.setAttribute("graceInterest", graceInterest);
			context.setAttribute("payment", payment);

			if (request.getParameter("ajax") != null) {
				Writer out = response.getWriter();
				response.setContentType("application/json");
				response.flushBuffer();

				out.append( "{ \"graceInterest\" : " + graceInterest + ", \"payment\" : " + payment + " }");
			} else {
				request.getRequestDispatcher("/Results.jspx").forward(request, response);
			}
		} catch (Exception e) {
			request.setAttribute("errorMsg", e.getMessage());
			request.getRequestDispatcher("/UI.jspx").forward(request, response);
		}
	}

}
