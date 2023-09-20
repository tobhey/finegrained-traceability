package smos.application.userManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

/**
 * Servlet used to remove a user.
 * 
 * @author Napolitano Vincenzo.
 * 
 */
public class ServletRemoveUser extends HttpServlet {

	private static final long serialVersionUID = -7693860059069872995L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "showUserList";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();
		Collection<ItemListUsers> manager = null;
		Iterator<ItemListUsers> it = null;
		User loggedUser = (User) session.getAttribute("loggedUser");
		ManagerUser managerUser = ManagerUser.getInstance();

		// Verifica che l'user abbia effettuato il login
		try {
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}
			if ((!managerUser.eAdministrator(loggedUser)) && (!managerUser.eAdministrator(loggedUser))) {
				errorMessage = "L'User collegato non ha accesso alla " + "funzionalita'!";
				gotoPage = "./error.jsp";
			}

			User user = (User) session.getAttribute("user");
			// cancella user se non e amministratore
			if (!managerUser.eAdministrator(user)) {
				managerUser.remove(user);
			}
			// controllo se l'user e amministratore e se ce ne sono degli altri
			else {
				manager = managerUser.getAmministratori();
				it = manager.iterator();
				it.next();
				if (it.hasNext()) {
					managerUser.remove(user);
				} else
					throw new DeleteAdministratorException();
			}

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
		} catch (FieldRequiredException mandatoryFieldException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + mandatoryFieldException.getMessage();
			gotoPage = "./error.jsp";
			mandatoryFieldException.printStackTrace();
		} catch (ValueInvalidException invalidValueException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
		} catch (IOException ioException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
		} catch (DeleteAdministratorException deleteAdministratorException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + deleteAdministratorException.getMessage();
			gotoPage = "./error.jsp";
			deleteAdministratorException.printStackTrace();
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
