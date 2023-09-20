package smos.application.reportManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Servlet utilizzata per visualizzare tutti gli insegnamenti.
 * 
 * @author Giulio D'Amora.
 * @version 1.0
 * 
 * 
 */
public class ServletShowSchoolReport extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1361713427864776624L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "./reportsManagement/showReports.jsp";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();
		Collection<Grade> votesList = null;
		ManagerGrade managerVotes = ManagerGrade.getInstance();
		ManagerUser managerUser = ManagerUser.getInstance();
		User loggedUser = (User) session.getAttribute("loggedUser");

		try {
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}
			if ((!managerUser.eAdministrator(loggedUser)) && (!managerUser.eDirettore(loggedUser))) {
				errorMessage = "L'User collegato non ha accesso alla funzionalita'!";
				gotoPage = "./error.jsp";
			}
			int studentId = Integer.valueOf(pRequest.getParameter("student"));
			Integer year = (Integer) session.getAttribute("selectedYear");
			Integer turn = Integer.valueOf(pRequest.getParameter("q"));
			session.setAttribute("q", turn);
			votesList = managerVotes.getGradePerUserIdYearRotation(studentId, year, turn);
			User u = (User) managerUser.getUserPerId(studentId);
			ItemListUsers st = new ItemListUsers();
			st.setName(u.getName());
			st.setEMail(u.getEMail());
			st.setId(u.getId());
			session.setAttribute("std", st);
			session.setAttribute("votesList", votesList);
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
