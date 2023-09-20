package smos.application.userManagement;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet used to login the user.
 * 
 * @author Napolitano Vincenzo.
 */
public class ServletAccess extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();

		// Ottengo i dati dalla request
		String login = pRequest.getParameter("user");
		String password = pRequest.getParameter("password");

		// Login dell'user
		try {

			ManagerUser managerUser = ManagerUser.getInstance();

			if (managerUser.getUserPerLogin(login) != null) {

				User loggedUser = managerUser.login(login, password);
				if (loggedUser != null)
					session.setAttribute("loggedUser", loggedUser);
				else
					throw new LoginException("Name User e/o Password errati!");

				if (managerUser.eAdministrator(loggedUser)) {
					gotoPage = "./homePage/homeAdmin.jsp";
				} else if (managerUser.eTeacher(loggedUser)) {
					gotoPage = "./homePage/homeProfessor.jsp";
				} else if (managerUser.eStudent(loggedUser)) {
					gotoPage = "./homePage/homeStudent.jsp";
				} else if (managerUser.eParent(loggedUser)) {
					gotoPage = "./homePage/homeParent.jsp";
				} else if (managerUser.eAtaPersone(loggedUser)) {
					gotoPage = "./homePage/homeAtaPeople.jsp";
				} else if (managerUser.eDirettore(loggedUser)) {
					gotoPage = "./homePage/homeDirector.jsp";
				}

			}

		} catch (LoginException loginException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + loginException.getMessage();
			gotoPage = "./error.jsp";
			loginException.printStackTrace();
		} catch (ConnectionException connectionException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
		} catch (SQLException sqlException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + sqlException.getMessage();
			gotoPage = "./error.jsp";
			sqlException.printStackTrace();
		} catch (EntityNotFoundException entityNotFoundException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + entityNotFoundException.getMessage();
			gotoPage = "./error.jsp";
			entityNotFoundException.printStackTrace();
		} catch (ValueInvalidException invalidValueException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
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
