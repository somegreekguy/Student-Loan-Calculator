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
		if (request.getQueryString() ==  null || request.getParameter("restart") != null) {
			request.getRequestDispatcher("/UI.jspx").forward(request, response);
			return;
		}

		// Get Servlet Context that contains parameters
		ServletContext context = request.getServletContext();

		String principal = context.getInitParameter("principal");
		String interest = context.getInitParameter("interest");
		String fixedInterest = context.getInitParameter("fixedInterest");
		String period = context.getInitParameter("period");
		String gracePeriod = context.getInitParameter("gracePeriod");
		String grace = context.getInitParameter("grace");
		Boolean userPrincipal = false;

		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = params.nextElement();
			String value = request.getParameter(param);

			if (param.equals("principal")) {
				principal = value;
				userPrincipal = true;
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
			double payment = model.computePayment(principal, period, interest, String.valueOf(graceInterest), gracePeriod, fixedInterest);

			if (userPrincipal) {
				request.getSession().setAttribute("principal", principal);
			}

			Writer out = response.getWriter();
			response.flushBuffer();

			out.append("{ \"graceInterest\" : " + graceInterest + ", \"payment\" : " + payment + " }");
		} catch (Exception e) {
			System.out.println("Error'd Out.");
			request.setAttribute("errorMsg", e.getMessage());
			request.getRequestDispatcher("/UI.jspx").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get Servlet Context that contains parameters
		ServletContext context = request.getServletContext();
		String fixedInterest = context.getInitParameter("fixedInterest");
		String gracePeriod = context.getInitParameter("gracePeriod");
		Boolean userPrincipal = false;

		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = params.nextElement();
			String value = request.getParameter(param);

			if (param.equals("principal")) {
				principal = value;
				userPrincipal = true;
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
			double payment = model.computePayment(principal, period, interest, String.valueOf(graceInterest), gracePeriod, fixedInterest);

			request.getSession().setAttribute("principal", principal);
			context.setAttribute("graceInterest", graceInterest);
			context.setAttribute("payment", payment);

			request.getRequestDispatcher("/Results.jspx").forward(request, response);
		} catch (Exception e) {
			request.setAttribute("errorMsg", e.getMessage());
			request.getRequestDispatcher("/UI.jspx").forward(request, response);
		}
	}

}
