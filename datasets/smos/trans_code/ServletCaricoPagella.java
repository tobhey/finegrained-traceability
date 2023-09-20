package smos.application.reportManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Servlet used to load a student's School Report.
 * 
 * @author Giulio D'Amora.
 * @version 1.0
 * 
 * 
 */
public class ServletLoadSchoolReport extends HttpServlet {

	private static final long serialVersionUID = -1045906657573424217L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "./reportsManagement/updateReport.jsp";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();
		Collection<Grade> votesList = null;
		Collection<Teaching> teachingList = null;
		ManagerGrade managerVotes = ManagerGrade.getInstance();
		ManagerUser managerUser = ManagerUser.getInstance();
		ManagerTeaching managerTeaching = ManagerTeaching.getInstance();
		User loggedUser = (User) session.getAttribute("loggedUser");

		try {
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}
			if (!managerUser.eAdministrator(loggedUser)) {
				errorMessage = "L'User collegato non ha accesso alla funzionalita'!";
				gotoPage = "./error.jsp";
			}
			Class classroom = (Class) session.getAttribute("classroom");
			// Lista teaching
			teachingList = managerTeaching.getTeachingsPerClassId(classroom.getIdStreetAddress());
			Integer year = (Integer) session.getAttribute("selectedYear");
			// Quadrimestre
			int turn = (Integer) session.getAttribute("q");

			ItemListUsers student = (ItemListUsers) session.getAttribute("student");
			// Lista Voti
			votesList = managerVotes.getGradePerUserIdYearRotation(student.getId(), year, turn);
			session.setAttribute("teachingList", teachingList);
			session.setAttribute("votesList", votesList);
			// provare i dati
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
