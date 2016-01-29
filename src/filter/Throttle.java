package filter;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
/**
 * Servlet Filter implementation class Throttle
 */
@WebFilter(
		dispatcherTypes = {
				DispatcherType.REQUEST
		}, 
		urlPatterns = { 
				"/Start",
				"/Startup/"
		}, 
		servletNames = {  
				"Start"
		})
public class Throttle implements Filter {

    /**
     * Default constructor. 
     */
    public Throttle() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		long last = httpRequest.getSession().getLastAccessedTime();
		long time = System.currentTimeMillis();
		
		if (time - last < 5000 && time - last > 10) {
			request.getRequestDispatcher("/Throttle.jspx").forward(request,response);
		} else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
