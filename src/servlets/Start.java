package servlets;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Start
 */
@WebServlet({"/Start","/Startup/*"})
public class Start extends HttpServlet {
	private static final long serialVersionUID = 1L;

		/**
		* Default constructor.
		*/
		public Start() {
			// TODO Auto-generated constructor stub
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
	ServletContext context = getServletContext();

	double principal = Double.parseDouble(context.getInitParameter("principal"));
	double interest = Double.parseDouble(context.getInitParameter("interest"));
	double fixedInterest = Double.parseDouble(context.getInitParameter("fixedInterest"));
	int period = Integer.parseInt(context.getInitParameter("period"));
	int gracePeriod = Integer.parseInt(context.getInitParameter("gracePeriod"));
	boolean grace = false;

	Enumeration<String> params = request.getParameterNames();
	while (params.hasMoreElements()) {
		String param = params.nextElement();
		String value = request.getParameter(param);

		if (value.equals("")){
		} else if (param.equals("principal")) {
			principal = Double.parseDouble(value);
		} else if (param.equals("interest")) {
			interest = Double.parseDouble(value);
		} else if (param.equals("period")) {
			period = Integer.parseInt(value);
		} else if (param.equals("grace")) {
			grace = value.equals("on") ? true : false;
		}
	}

	// Calculations
	double graceInterest = grace ? principal * (( interest/100 + fixedInterest/100 ) / 12 ) * gracePeriod : 0;
	double mnthIntrst = ( interest / 100 ) / 12;
	double actualPeriod = grace ? period - gracePeriod : period;
	double payment = ( mnthIntrst * principal )/( 1 - Math.pow( 1 + mnthIntrst, -actualPeriod ));
	payment = grace ? payment + ( graceInterest / gracePeriod ) : payment;

	context.setAttribute("graceInterest", new DecimalFormat("##.00").format(graceInterest));
	context.setAttribute("payment", new DecimalFormat("##.00").format(payment));
	request.getRequestDispatcher("/Results.jspx").forward(request, response);
	}

}
