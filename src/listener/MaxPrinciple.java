package listener;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * Application Lifecycle Listener implementation class MaxPrinciple
 *
 */
@WebListener
public class MaxPrinciple implements HttpSessionAttributeListener {

	/**
	 * Default constructor.
	 */
	public MaxPrinciple() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpSessionAttributeListener#attributeAdded(HttpSessionBindingEvent)
	 */
	public void attributeAdded(HttpSessionBindingEvent arg0) {
		if (arg0.getName().equals("principal")) { addPrincipal(arg0); }
	}

	/**
	 * @see HttpSessionAttributeListener#attributeRemoved(HttpSessionBindingEvent)
	 */
	public void attributeRemoved(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
	 */
	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		if (arg0.getName().equals("principal")) { addPrincipal(arg0); }
	}

	private void addPrincipal(HttpSessionBindingEvent arg0) {
		ServletContext context = arg0.getSession().getServletContext();

		try {
			Double value = 0.0;
			Double newValue = Double.parseDouble(arg0.getSession().getAttribute("principal").toString());

			if (context.getAttribute("maxPrincipal") != null) {
				Double oldValue = Double.parseDouble(context.getAttribute("maxPrincipal").toString());
				value = newValue > oldValue ? newValue : oldValue;
			} else {
				value = newValue > value ? newValue : value; // Will not replace with negative
			}

			context.setAttribute("maxPrincipal", value);
		} catch (Exception e) {
			System.out.println("error:" + e.getMessage() + "\nAt:" + e.getCause());
		}
	}

}
