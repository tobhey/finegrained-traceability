package smos.application;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import smos.utility.Utility;

/**
 * Servlet utilizzata per inizializzare i parametri del sistema.
 * 
 * @author Bavota Gabriele, Carnevale Filomena.
 *
 */
public class ServletInizializzazione extends HttpServlet {

	private static final long serialVersionUID = -2542143445249797492L;
	
	@SuppressWarnings("unused")
	private ServletConfig config;
	
	 /**
     * Inizializza i parametri
     */
    public void init(ServletConfig config) throws ServletException 

    {
    	this.config = config;
    	               
        
        //Setto il server smtp specificato nel file di configurazione xml
        Utility.settareServerSmtp(config.getInitParameter("serverSmtp"));
        
        //Setto i parametri necessari alla connessione al Database
        Utility.settareDriverMySql(config.getInitParameter("driverMySql"));
        Utility.settareCompletoPathDatabase(config.getInitParameter("fullPathDatabase"));
        Utility.settareUtenteNome(config.getInitParameter("userName"));
        Utility.settarePassword(config.getInitParameter("password"));
        Utility.settareDimensioneMassimaPool(Integer.valueOf(config.getInitParameter("maxPoolSize")));
        Utility.settareAspettareTimeout(Integer.valueOf(config.getInitParameter("waitTimeout")));
        Utility.settareAttivoTimeout(Integer.valueOf(config.getInitParameter("activeTimeout")));
        Utility.settarePoolTimeout(Integer.valueOf(config.getInitParameter("poolTimeout")));
        Utility.settareTestoAPieDiPagina(config.getInitParameter("textFooter"));
        
        
	}

}
