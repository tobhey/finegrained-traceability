package smos.application.userManagement;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet used to view the role management form of
 * users.
 * 
 * @author Napolitano Vincenzo.
 *
 */
public class ServletShowModuleRoleUser extends HttpServlet {

	private static final long serialVersionUID = -2210761175435137331L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "./persistentDataManagement/userManagement/userRolez.jsp";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();

		boolean isTeacherBoolean = false;
		boolean isAdministratorBoolean = false;
		boolean isParentBoolean = false;
		boolean isStudentBoolean = false;
		boolean isAtaBoolean = false;
		boolean isDirectorBoolean = false;

		int isTeacher = 0;
		int isAdministrator = 0;
		int isDirector = 0;
		int isParent = 0;
		int isStudent = 0;
		int isAta = 0;

		User user = null;
		ManagerUser managerUser = ManagerUser.getInstance();

		User loggedUser = (User) session.getAttribute("loggedUser");

		try {
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}

			if ((!managerUser.eAdministrator(loggedUser))) {
				errorMessage = "L'User collegato non ha accesso alla " + "funzionalita'!";
				gotoPage = "./error.jsp";
			}
			user = (User) session.getAttribute("user");
			// prepariamo i valori da passare alla jsp
			isTeacherBoolean = managerUser.eTeacher(user);
			isAdministratorBoolean = managerUser.eAdministrator(user);
			isAtaBoolean = managerUser.eAtaPersone(user);
			isDirectorBoolean = managerUser.eDirettore(user);
			isStudentBoolean = managerUser.eStudent(user);
			isParentBoolean = managerUser.eParent(user);

			isTeacher = Utility.BooleanToInt(isTeacherBoolean);
			isDirector = Utility.BooleanToInt(isDirectorBoolean);
			isAdministrator = Utility.BooleanToInt(isAdministratorBoolean);
			isAta = Utility.BooleanToInt(isAtaBoolean);
			isStudent = Utility.BooleanToInt(isStudentBoolean);
			isParent = Utility.BooleanToInt(isParentBoolean);

			gotoPage = "./persistentDataManagement/userManagement/userRolez.jsp?isTeacher=" + isTeacher + "&isAdmin="
					+ isAdministrator + "&isAta=" + isAta + "&isStudent=" + isStudent + "&isParent=" + isParent
					+ "&isDirector=" + isDirector;
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
