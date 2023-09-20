package smos.application.userManagement;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet used to insert a user into the DataBase.
 * 
 * @author napolitano Vincenzo.
 *
 */
public class ServletInsertUser extends HttpServlet {

	private static final long serialVersionUID = -3860569607870099419L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	public void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "./showUserList";
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

			User user = new User();
			user.setFirstName(pRequest.getParameter("firstName"));
			user.setSurname(pRequest.getParameter("lastName"));
			user.setCell(pRequest.getParameter("cell"));
			user.setEMail(pRequest.getParameter("eMail"));

			/*
			 * verifichiamo che la login sia unica.
			 */
			String login = pRequest.getParameter("login");
			user.setLogin(login);

			if (managerUser.existsLogin(user))
				throw new ValueInvalidException("La login inserita exists gia'. Insert una nuova login.");

			if (pRequest.getParameter("password").equals(pRequest.getParameter("password1"))) {
				user.setPassword((pRequest.getParameter("password")));
			} else {
				throw new LoginException("I valori inseriti per la password non coincidono!");
			}
			// inserimento user
			if (!managerUser.exists(user)) {
				managerUser.insert(user);
				session.setAttribute("user", user);

			} else
				throw new DuplicateEntityException("User gia' existsnte");

		} catch (SQLException SQLException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + SQLException.getMessage();
			gotoPage = "./error.jsp";
			SQLException.printStackTrace();
		} catch (ConnectionException connectionException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
		} catch (FieldRequiredException mandatoryFieldException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + mandatoryFieldException.getMessage();
			gotoPage = "./error.jsp";
			mandatoryFieldException.printStackTrace();
		} catch (EntityNotFoundException entityNotFoundException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + entityNotFoundException.getMessage();
			gotoPage = "./error.jsp";
			entityNotFoundException.printStackTrace();
		} catch (ValueInvalidException invalidValueException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
		} catch (DuplicateEntityException duplicatedEntityException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + duplicatedEntityException.getMessage();
			gotoPage = "./error.jsp";
			duplicatedEntityException.printStackTrace();
		} catch (LoginException loginException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + loginException.getMessage();
			gotoPage = "./error.jsp";
			loginException.printStackTrace();
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
