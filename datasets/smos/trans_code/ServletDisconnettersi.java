package smos.application.userManagement;

import java.io.IOException;

/**
 * Servlet used to log out the user.
 * 
 * @author napolitano Vincenzo.
 *
 */
public class ServletLogOut extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply)
			throws ServletException, IOException {

		pRequest.getSession().invalidate();
		pReply.sendRedirect("./index.htm");
	}

	/**
	 * Definition of the doPost method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doPost(HttpServletRequest pRequest, HttpServletResponse pReply)
			throws ServletException, IOException {
		this.doGet(pRequest, pReply);
	}
}
