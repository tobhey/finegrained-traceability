package smos.application.userManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * Servlet that modifies the student record with the parent's id.
 * 
 * @author Napolitano Vincenzo.
 * 
 */
public class ServletAssignStudentParent extends HttpServlet {

	private static final long serialVersionUID = -4507225018030147979L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "showUserList";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();

		ManagerUser managerUser = ManagerUser.getInstance();

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
			int idParent = user.getId();

			Collection<ItemListUsers> students = new Vector<ItemListUsers>();
			Iterator<ItemListUsers> itStu = null;

			students = managerUser.getStudents();

			String[] selectedStudent = pRequest.getParameterValues("selectedStudent");
			String[] unselectedStudent = pRequest.getParameterValues("unselectedStudent");

			if (selectedStudent != null) {
				int selectedlength = selectedStudent.length;
				ItemListUsers tmp = null;
				User stu = null;
				int idStudent = 0;
				for (int i = 0; i < selectedlength; i++) {
					itStu = students.iterator();
					String dd = selectedStudent[i];
					idStudent = Integer.parseInt(dd);
					while (itStu.hasNext()) {
						tmp = itStu.next();
						if (tmp.getId() == idStudent) {
							stu = managerUser.getUserPerId(tmp.getId());// recupero userStudent
							managerUser.assegnareParent(stu, idParent);
						}
					}
				}

			} else {
				gotoPage = "./error.jsp";
			}
			if (unselectedStudent != null) {
				itStu = null;
				int unselectedlength = unselectedStudent.length;
				ItemListUsers tmp = null;
				User stu = null;
				int idStudent = 0;
				for (int i = 0; i < unselectedlength; i++) {
					itStu = students.iterator();
					String dd = unselectedStudent[i];
					idStudent = Integer.parseInt(dd);
					while (itStu.hasNext()) {
						tmp = itStu.next();
						if (tmp.getId() == idStudent) {
							stu = managerUser.getUserPerId(tmp.getId());// recupero userStudent
							managerUser.removeParent(stu);
						}
					}
				}
			}

			session.setAttribute("user", user);
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
