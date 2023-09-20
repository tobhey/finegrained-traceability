package smos.application.registerManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

public class ServletShowListJustify extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6666791203700528449L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {

		String gotoPage = "./registerManagement/showJustifyList.jsp";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();

		ManagerUser managerUser = ManagerUser.getInstance();
		ManagerRegister managerRegister = ManagerRegister.getInstance();

		User loggedUser = (User) session.getAttribute("loggedUser");

		String student = pRequest.getParameter("student");

		int st = Integer.parseInt(student);
		Class classroom = (Class) session.getAttribute("classroom");

		try {
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}
			if ((!managerUser.eAdministrator(loggedUser)) && (!managerUser.eDirettore(loggedUser))) {
				errorMessage = "L'User collegato non ha accesso alla " + "funzionalita'!";
				gotoPage = "./error.jsp";
			}

			User userStudent = managerUser.getUserPerId(st);

			Collection<Absence> absenceList = (Collection<Absence>) managerRegister.getAbsencePerIdUserEAcademicYear(st,
					classroom.getAcademicYear());
			session.setAttribute("absenceList", absenceList);
			session.setAttribute("user", userStudent);

		} catch (EntityNotFoundException entityNotFoundException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + entityNotFoundException.getMessage();
			gotoPage = "./error.jsp";
			entityNotFoundException.printStackTrace();
		} catch (ConnectionException connectionException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
		} catch (SQLException sqlException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + sqlException.getMessage();
			gotoPage = "./error.jsp";
			sqlException.printStackTrace();
		} catch (ValueInvalidException invalidValueException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
		} catch (IOException ioException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
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
