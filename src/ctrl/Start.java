package ctrl;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.regex.Pattern;

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
@WebServlet({"/Start","/Start/*","/Startup/*"})
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
		String path = request.getPathInfo();
		if (request.getServletPath().equals("/Start") && path != null && Pattern.matches("/Fixed[A-Z]", path)) {
			String letter = path.substring(path.length() - 1);
			request.getSession().setAttribute("category", letter);
			request.setAttribute("category", true);
		} else {
			request.getSession().setAttribute("category", null);
		}

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

		if (request.getSession().getAttribute("category") != null) {
			String temp = request.getSession().getAttribute("category").toString();

			if (Pattern.matches("[A-F]", temp)) { period = "120"; }
			else if (Pattern.matches("[G-K]", temp)) { period = "180"; }
			else if (Pattern.matches("[L-O]", temp)) { period = "240"; }
			else if (Pattern.matches("[P-Z]", temp)) { period = "360"; }

			request.setAttribute("period", Integer.parseInt(period)/12);
			request.setAttribute("interest", interest);
			request.setAttribute("category", true);
			grace = "off";
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
