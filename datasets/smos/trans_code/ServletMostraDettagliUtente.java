package smos.application.userManagement;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet used to retrieve user details.
 * 
 * @author Napolitano Vincenzo.
 *
 */
public class ServletShowDetailsUser extends HttpServlet {

	private static final long serialVersionUID = 1726037792193089501L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "./persistentDataManagement/userManagement/showUserDetails.jsp";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();
		ManagerUser managerUser = ManagerUser.getInstance();
		User loggedUser = (User) session.getAttribute("loggedUser");

		try {
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}
			if (!managerUser.eAdministrator(loggedUser)) {
				errorMessage = "L'User collegato non ha accesso alla " + "funzionalita'!";
				gotoPage = "./error.jsp";
			}

			int userId = Integer.valueOf(pRequest.getParameter("userId"));
			User user = managerUser.getUserPerId(userId);

			session.setAttribute("user", user);
		} catch (NumberFormatException numberFormatException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + numberFormatException.getMessage();
			gotoPage = "./error.jsp";
			numberFormatException.printStackTrace();
		} catch (ValueInvalidException invalidValueException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
		} catch (EntityNotFoundException entityNotFoundException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + entityNotFoundException.getMessage();
			gotoPage = "./error.jsp";
			entityNotFoundException.printStackTrace();
		} catch (ConnectionException connectionException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
		} catch (SQLException SQLException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + SQLException.getMessage();
			gotoPage = "./error.jsp";
			SQLException.printStackTrace();
		} catch (IOException ioException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
		}

		session.setAttribute("errorMessage", errorMessage);
		try {
			pReply.sendRedirect(gotoPage);
		} catch (IOException ioException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
		}

	}

	/**
	 * Definition of the doPost method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doPost(HttpServletRequest pRequest, HttpServletResponse pReply) {
		this.doGet(pRequest, pReply);
	}

}
