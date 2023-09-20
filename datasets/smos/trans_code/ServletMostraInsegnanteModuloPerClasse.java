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
public class ServletShowTeacherModulePerClass extends HttpServlet {

	private static final long serialVersionUID = -3988115259356084996L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "./persistentDataManagement/userManagement/teacherTeachings.jsp";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();
		Collection<Teaching> teachingList = null;
		ManagerUser managerUser = ManagerUser.getInstance();
		User loggedUser = (User) session.getAttribute("loggedUser");
		// User teacher = (User) session.getAttribute("user");
		try {
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}
			if (!managerUser.eAdministrator(loggedUser)) {
				errorMessage = "L'User collegato non ha accesso alla " + "funzionalita'!";
				gotoPage = "./error.jsp";
			}
			// recuperiamo l'id della classe selezionata
			int selectedClassId = Integer.valueOf(pRequest.getParameter("classId"));
			ManagerTeaching managerTeaching = ManagerTeaching.getInstance();
			// Calcoliamo l'elenco degli insegnamenti associati alla class eselezionata
			teachingList = managerTeaching.getTeachingsPerClassId(selectedClassId);
			session.setAttribute("teachingList", teachingList);
			Class selectedClass = ManagerClass.getInstance().getClassPerID(selectedClassId);
			session.setAttribute("selectedClass", selectedClass);
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
