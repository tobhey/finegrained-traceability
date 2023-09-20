package smos.application.addressManagement;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet used to assign Teachings to a teacher
 * 
 * 
 * @author Giulio D'Amora
 * @version 1.0
 * 
 * 
 */
public class ServletAddRemoveTeachingsToStreetAddress extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6692711286746163446L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "./showAddressList";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();
		ManagerUser managerUser = ManagerUser.getInstance();
		ManagerTeaching managerTeaching = ManagerTeaching.getInstance();
		ManagerStreetAddress managerAddress = ManagerStreetAddress.getInstance();
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
			// //Mi servono l'id dell'address e le due liste di insegnamenti!
			StreetAddress address = (StreetAddress) session.getAttribute("address");
			String[] idSelectedList = pRequest.getParameterValues("selectedTeachings");
			String[] idUnselectedList = pRequest.getParameterValues("unselectedTeachings");
			int nSelected = idSelectedList.length;
			int nUnselected = idUnselectedList.length;
			int temp;
			// Aggiungiamo gli insegnamenti selezionati!!
			for (int i = 0; i < nSelected; i++) {
				temp = Integer.valueOf(idSelectedList[i]);
				if (!managerAddress.hasTeaching(managerTeaching.getTeachingPerId(temp), address)) {
					managerAddress.assignTeachingAsStreetAddress(address, managerTeaching.getTeachingPerId(temp));
				}
			}
			// Rimuoviamo gli insegnamenti non selezionati
			for (int i = 0; i < nUnselected; i++) {
				temp = Integer.valueOf(idUnselectedList[i]);
				if (managerAddress.hasTeaching(managerTeaching.getTeachingPerId(temp), address)) {
					managerAddress.removeTeachingAsStreetAddress(address, managerTeaching.getTeachingPerId(temp));
				}
			}
			// session.setAttribute("teachingListTeacher", listSelcected);
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
		} catch (IOException ioException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
		} catch (ValueInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldRequiredException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DuplicateEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pRequest.getSession().setAttribute("errorMessage", errorMessage);
		try {
			pReply.sendRedirect(gotoPage);
		} catch (IOException ioException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + ioException.getMessage();
			gotoPage = "./error6.jsp";
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
