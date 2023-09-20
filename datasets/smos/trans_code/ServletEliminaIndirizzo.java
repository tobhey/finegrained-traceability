package smos.application.addressManagement;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet used to delete an address from the database
 * 
 * @author Vecchione Giuseppe
 */
public class ServletRemoveStreetAddress extends HttpServlet {

	private static final long serialVersionUID = -7383336226678925533L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String errorMessage = "";
		String gotoPage = "./showAddressList";
		ManagerUser managerUser = ManagerUser.getInstance();
		ManagerStreetAddress managerAddress = ManagerStreetAddress.getInstance();
		HttpSession session = pRequest.getSession();
		User loggedUser = (User) session.getAttribute("loggedUser");
		StreetAddress address = null;
		try {
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}
			if (!managerUser.eAdministrator(loggedUser)) {
				errorMessage = "L' user collegato non ha accesso alla funzionalita'!";
				gotoPage = "./error.jsp";
			}

			address = (StreetAddress) session.getAttribute("address");
			managerAddress.delete(address);

		} catch (IOException ioException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
		} catch (SQLException SQLException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + SQLException.getMessage();
			gotoPage = "./error.jsp";
			SQLException.printStackTrace();
		} catch (EntityNotFoundException entityNotFoundException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + entityNotFoundException.getMessage();
			gotoPage = "./error.jsp";
			entityNotFoundException.printStackTrace();
		} catch (ConnectionException connectionException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
		} catch (FieldRequiredException mandatoryFieldException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + mandatoryFieldException.getMessage();
			gotoPage = "./error.jsp";
			mandatoryFieldException.printStackTrace();
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
