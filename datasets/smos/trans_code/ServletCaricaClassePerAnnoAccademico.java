package smos.application.userManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Servlet used to view all academic years present in the
 * db.
 * 
 * @author Giulio D'Amora
 * @version 1.0
 * 
 * 
 */
public class ServletLoadClassPerAcademicYear extends HttpServlet {

	private static final long serialVersionUID = -3988115259356084996L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "./persistentDataManagement";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();
		Collection<Class> classroomList = null;
		ManagerUser managerUser = ManagerUser.getInstance();
		User loggedUser = (User) session.getAttribute("loggedUser");

		try {
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}
			if ((!managerUser.eAdministrator(loggedUser)) && (!managerUser.eDirettore(loggedUser))) {
				errorMessage = "L'User collegato non ha accesso alla " + "funzionalita'!";
				gotoPage = "./error.jsp";
			}
			// Date today = new Date();
			// recuperiamo l'anno accademico selezionato
			int selectedAccademicYear = Integer.valueOf(pRequest.getParameter("accademicYear"));

			ManagerClass managerClassroom = ManagerClass.getInstance();
			// Calcoliamo l'elenco delle classi dell'anno accademico selezionato
			classroomList = managerClassroom.getClassPerAcademicYear(selectedAccademicYear);
			session.setAttribute("classroomList", classroomList);
			session.setAttribute("selectedYear", selectedAccademicYear);
			// session.removeAttribute("selectedClass");
			gotoPage += (String) session.getAttribute("goTo");

			pReply.sendRedirect(gotoPage);
			return;

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
