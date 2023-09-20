package smos.application;

/**
 * Servlet used to initialize system parameters.
 * 
 * @author Bavota Gabriele, Carnevale Filomena.
 *
 */
public class ServletInitialization extends HttpServlet {

	private static final long serialVersionUID = -2542143445249797492L;

	@SuppressWarnings("unused")
	private ServletConfig config;

	/**
	 * Initialize the parameters
	 */
	public void init(ServletConfig config) throws ServletException

	{
		this.config = config;

		// Setto il server smtp specificato nel file di configurazione xml
		Utility.setServerSmtp(config.getInitParameter("serverSmtp"));

		// Setto i parametri necessari alla connection al Database
		Utility.setDriverMySql(config.getInitParameter("driverMySql"));
		Utility.setCompletePathDatabase(config.getInitParameter("fullPathDatabase"));
		Utility.setUserName(config.getInitParameter("userName"));
		Utility.setPassword(config.getInitParameter("password"));
		Utility.setDimensionMaximumPool(Integer.valueOf(config.getInitParameter("maxPoolSize")));
		Utility.setWaitTimeout(Integer.valueOf(config.getInitParameter("waitTimeout")));
		Utility.setActiveTimeout(Integer.valueOf(config.getInitParameter("activeTimeout")));
		Utility.setPoolTimeout(Integer.valueOf(config.getInitParameter("poolTimeout")));
		Utility.setTestoAPieDiPagina(config.getInitParameter("textFooter"));

	}

}
