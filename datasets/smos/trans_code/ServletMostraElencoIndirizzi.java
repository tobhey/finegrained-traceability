package smos.application.addressManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Servlet used to view all addresses.
 * 
 * @author Vecchione Giuseppe
 * 
 */
public class ServletShowListAdresses extends HttpServlet {

	private static final long serialVersionUID = 8797912020763935353L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */

	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String errorMessage = "";
		String gotoPage = "./persistentDataManagement/addressManagement/showAddressList.jsp";
		HttpSession session = pRequest.getSession();
		Collection<StreetAddress> addressList = null;
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
			addressList = managerAddress.getStreetAddressElenco();

			session.setAttribute("addressList", addressList);
			pReply.sendRedirect(gotoPage);
			return;

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
		} catch (ValueInvalidException invalidValueException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
		}
		pRequest.getSession().setAttribute("errorMessage", errorMessage);

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
