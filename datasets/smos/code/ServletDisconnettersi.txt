package smos.application.userManagement;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet utilizzata per effettuare il logout dell'utente.
 * 
 * @author napolitano Vincenzo.
 *
 */
public class ServletDisconnettersi extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doGet(HttpServletRequest pRichiesta, HttpServletResponse pRisposta) throws ServletException, IOException {
		
		pRichiesta.getSession().invalidate();
		pRisposta.sendRedirect("./index.htm");
	}
	
	/**
	 * Definizione del metodo doPost
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doPost(HttpServletRequest pRichiesta, HttpServletResponse pRisposta) throws ServletException, IOException {
		this.doGet(pRichiesta, pRisposta);
	}
}
