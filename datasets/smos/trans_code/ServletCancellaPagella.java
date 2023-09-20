package smos.application.reportManagement;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet used to view the students associated with a Class.
 * 
 * @author Giulio D'Amora
 * 
 */
public class ServletCancelSchoolReport extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2020233250419553067L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "./reportsManagement/showStudentsByClass.jsp";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();
		ManagerGrade managerVotes = ManagerGrade.getInstance();
		ManagerUser managerUser = ManagerUser.getInstance();
		User loggedUser = (User) session.getAttribute("loggedUser");
		int year = 0;
		int turn = 0;
		int studentId = 0;
		try {
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}
			if (!managerUser.eAdministrator(loggedUser)) {
				errorMessage = "L'User collegato non ha accesso alla " + "funzionalita'!";
				gotoPage = "./error2.jsp";
			}
			ItemListUsers student = (ItemListUsers) (session.getAttribute("student"));
			studentId = student.getId();
			year = (Integer) session.getAttribute("selectedYear");
			turn = (Integer) session.getAttribute("q");
			managerVotes.removeGradePerUserIdYearRotation(studentId, year, turn);
			pReply.sendRedirect(gotoPage);

		} catch (NumberFormatException numberFormatException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + numberFormatException.getMessage();
			gotoPage = "./error1.jsp?Year=" + year + "&turn=" + turn + "&idStudent=" + studentId;
			numberFormatException.printStackTrace();
		} catch (EntityNotFoundException entityNotFoundException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + entityNotFoundException.getMessage();
			gotoPage = "./error3.jsp";
			entityNotFoundException.printStackTrace();
		} catch (ConnectionException connectionException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + connectionException.getMessage();
			gotoPage = "./error4.jsp";
			connectionException.printStackTrace();
		} catch (SQLException SQLException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + SQLException.getMessage();
			gotoPage = "./error5.jsp?Year=" + year + "&turn=" + turn + "&idStudent=" + studentId;
			SQLException.printStackTrace();
		} catch (IOException ioException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + ioException.getMessage();
			gotoPage = "./error6.jsp";
			ioException.printStackTrace();
		} catch (ValueInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		session.setAttribute("errorMessage", errorMessage);
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
