package micrium.user.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class TestFilter
 */
@WebFilter("/CacheFilter")
public class CacheFilter implements Filter {
	
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CacheFilter.class);

	/**
	 * Default constructor.
	 */
	public CacheFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		// HttpServletRequest req = (HttpServletRequest) request;

		// String path = req.getRequestURI();

		HttpServletResponse httpResponse = (HttpServletResponse) response;

		chain.doFilter(request, response);

		// httpResponse.addHeader("x-ua-compatible", "IE=8");
//		httpResponse.addHeader("X-Frame-Options", "deny");
//		httpResponse.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
//		httpResponse.addHeader("Pragma", "no-cache");
//		httpResponse.setDateHeader("Expires", 0);
		
		
		CabeceraHttp.ponerCabeceras(httpResponse,request);
		
		request.setCharacterEncoding("UTF-8");
		
		try{
			if(ReferrerHttp.isReferrerAjeno(request)){
				log.info("Se va a redireccionar hacia un error la peticion maligna");
				httpResponse.sendRedirect(loginFilter.rederingErrorHacker);
				//chain.doFilter(request, response);
				return;
			}else{
				if( ReferrerHttp.isPageError(request)){
					log.debug("Por tratarse de la página de error se cancela todo el resto del analisis");
					chain.doFilter(request, response);
					return;
				}
			}
		}catch(Exception ex){
			log.error("Error aumentado"+ex);
		}

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
