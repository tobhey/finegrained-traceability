package smos.application.teachingManagement;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet used to modify a teaching.
 * 
 * @author Giulio D'Amora.
 * @version 1.0
 * 
 * 
 */
public class ServletUpgradTeaching extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 943677173076169934L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "./teachingList";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();
		ManagerUser managerUser = ManagerUser.getInstance();
		Teaching teaching = (Teaching) session.getAttribute("teaching");
		ManagerTeaching managerTeaching = ManagerTeaching.getInstance();
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

			teaching.setName((pRequest.getParameter("name")));
			teaching.setId(teaching.getId());

			if (!managerTeaching.exists(teaching)) {
				managerTeaching.update(teaching);
			} else if (teaching.getId() == managerTeaching.getTeachingId(teaching))
				managerTeaching.update(teaching);
			else {

				throw new DuplicateEntityException("Teaching gia existsnte");
			}

		} catch (SQLException SQLException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + SQLException.getMessage();
			gotoPage = "./error.jsp";
			SQLException.printStackTrace();
		} catch (ConnectionException connectionException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
		} catch (EntityNotFoundException entityNotFoundException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + entityNotFoundException.getMessage();
			gotoPage = "./error.jsp";
			entityNotFoundException.printStackTrace();
		} catch (FieldRequiredException mandatoryFieldException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + mandatoryFieldException.getMessage();
			gotoPage = "./error.jsp";
			mandatoryFieldException.printStackTrace();
		} catch (ValueInvalidException invalidValueException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
		} catch (DuplicateEntityException duplicatedEntityException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + duplicatedEntityException.getMessage();
			gotoPage = "./error.jsp";
			duplicatedEntityException.printStackTrace();
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
