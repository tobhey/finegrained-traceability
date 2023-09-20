package smos.application.userManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public class ServletRemoveTheStudentClassroom extends HttpServlet {

	/**
	 * 
	 */

	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();
		ManagerUser managerUser = ManagerUser.getInstance();
		ManagerClass managerClassroom = ManagerClass.getInstance();
		User loggedUser = (User) session.getAttribute("loggedUser");

		// Verifica che l'user abbia effettuato il login
		try {
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}
			if ((!managerUser.eAdministrator(loggedUser))) {
				errorMessage = "L'User collegato non ha accesso alla " + "funzionalita'!";
				gotoPage = "./error.jsp";
			}

			User user = (User) session.getAttribute("user");
			Class classroom = (Class) session.getAttribute("classroom");
			int idClass = classroom.getIdClass();

			Collection<ItemListUsers> studentClass = (Collection<ItemListUsers>) managerUser
					.getStudentPerClassId(idClass);
			Iterator<ItemListUsers> itStudent = null;
			if (studentClass != null) {
				itStudent = studentClass.iterator();
			}

			while ((itStudent != null) && (itStudent.hasNext())) {
				ItemListUsers stud = itStudent.next();
				if (stud.getId() == user.getId())
					managerUser.removeStudentClass(user, idClass);
			}

		} catch (NumberFormatException numberFormatException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + numberFormatException.getMessage();
			gotoPage = "./error.jsp";
			numberFormatException.printStackTrace();
		} catch (EntityNotFoundException entityNotFoundException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + entityNotFoundException.getMessage();
			gotoPage = "./error.jsp";
			entityNotFoundException.printStackTrace();
		} catch (SQLException SQLException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + SQLException.getMessage();
			gotoPage = "./error.jsp";
			SQLException.printStackTrace();
		} catch (ConnectionException connectionException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
		} catch (ValueInvalidException invalidValueException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
		} catch (IOException ioException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
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
