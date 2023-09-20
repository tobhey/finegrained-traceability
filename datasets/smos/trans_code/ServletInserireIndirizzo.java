package smos.application.addressManagement;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet used to insert an address into the database
 * 
 * @author Vecchione Giuseppe
 */
public class ServletInsertStreetAddress extends HttpServlet {

	private static final long serialVersionUID = 8318905833953187814L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * 
	 * @param pReply
	 * 
	 */

	public void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "./showAddressList";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();
		ManagerUser managerUser = ManagerUser.getInstance();
		ManagerStreetAddress managerAddress = ManagerStreetAddress.getInstance();
		User loggedUser = (User) session.getAttribute("loggedUser");
		try {
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}
			if (!managerUser.eAdministrator(loggedUser)) {
				errorMessage = "L' user collegato non ha accesso alla funzionalita'!";
				gotoPage = "./error.jsp";
			}

			StreetAddress address = new StreetAddress();
			address.setName(pRequest.getParameter("name"));

			/**
			 * Verifichiamo che l' indirizzo non sia presente nel database e lo inseriamo
			 */
			if (!managerAddress.exists(address)) {
				managerAddress.insert(address);
			} else {
				throw new DuplicateEntityException("StreetAddress gia' existsnte");
			}

		} catch (IOException ioException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
		} catch (SQLException sqlException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + sqlException.getMessage();
			gotoPage = "./error.jsp";
			sqlException.printStackTrace();
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
		} catch (DuplicateEntityException duplicatedEntityException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + duplicatedEntityException.getMessage();
			gotoPage = "./error.jsp";
			duplicatedEntityException.printStackTrace();
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
