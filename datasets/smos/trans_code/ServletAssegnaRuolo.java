package smos.application.userManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * Servlet used to assign one or more roles to a user.
 * 
 * @author Napolitano Vincenzo.
 * 
 */
public class ServletAssignRole extends HttpServlet {

	private static final long serialVersionUID = 537330195407987283L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "showUserList";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();

		Collection<ItemListUsers> administrators = new Vector<ItemListUsers>();
		Iterator<ItemListUsers> itAdmin = null;

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

			administrators = managerUser.getAmministratori();
			itAdmin = administrators.iterator();
			itAdmin.next();

			String[] selectedRoles = pRequest.getParameterValues("selectedRoles");
			String[] unselectedRoles = pRequest.getParameterValues("unselectedRoles");

			if (selectedRoles != null) {
				int selectedlength = selectedRoles.length;
				for (int i = 0; i < selectedlength; i++) {
					int role = Integer.valueOf(selectedRoles[i]);
					/*
					 * controlliamo se il role che stiamo assegnando e' quello di docente
					 */

					/*
					 * if ((role == Role.TEACHER) && (!managerUser.isTeacher(user))){
					 * gotoPage="./loadYearForTeachings";
					 * 
					 * }
					 */
					/*
					 * controlliamo se il role che stiamo assegnando e' quello di student
					 */
					/*
					 * if ((role == Role.STUDENT) && (!managerUser.isStudent(user))){
					 * gotoPage="./showUserList";
					 * 
					 * }
					 */
					/*
					 * controlliamo se il role che stiamo assegnando e' quello di genitore
					 */
					/*
					 * if((role==Role.PARENT)&& (!managerUser.isParent(user))){ gotoPage=
					 * "./persistentDataManagement/userManagement/showStudentParentForm.jsp"; }
					 */
					managerUser.assegnareRole(user, role);
				}
			}

			if (unselectedRoles != null) {
				int unselectedlength = unselectedRoles.length;
				for (int i = 0; i < unselectedlength; i++) {
					int role = Integer.valueOf(unselectedRoles[i]);
					if ((managerUser.eAdministrator(user)) && (!itAdmin.hasNext()) && (role == Role.ADMINISTRATOR)) {
						throw new DeleteManagerException(
								"Impossibile modificare il role dell'user, e' l'unico Administrator del sistema! Creare un nuovo Administrator e riprovare!");
					}
					managerUser.removeRole(user, role);
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
		} catch (DeleteManagerException deleteManagerException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + deleteManagerException.getMessage();
			gotoPage = "./error.jsp";
			deleteManagerException.printStackTrace();
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
