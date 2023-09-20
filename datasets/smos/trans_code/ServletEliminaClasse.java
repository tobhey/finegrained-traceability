package smos.application.classroomManagement;

import java.io.IOException;
import java.sql.SQLException;

public class ServletRemoveClass extends HttpServlet {

	/**
	 * Servlet per cancellare una classe
	 * 
	 * @author Nicola Pisanti
	 * @version 0.9
	 */
	private static final long serialVersionUID = 5272269413504847511L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {

		int aC = 0;
		String gotoPage = "./showClassroomList?academicYear=" + aC;
		String errorMessage = "";
		HttpSession session = pRequest.getSession();
		User loggedUser = (User) session.getAttribute("loggedUser");
		ManagerUser managerUser = ManagerUser.getInstance();
		ManagerClass managerClassroom = ManagerClass.getInstance();

		// Verifica che l'user abbia effettuato il login
		try {
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}
			if (!managerUser.eAdministrator(loggedUser)) {
				errorMessage = "L'User collegato non ha accesso alla " + "funzionalita'!";
				gotoPage = "./error.jsp";
			}

			Class classroom = (Class) session.getAttribute("classroom");
			aC = Integer.valueOf(classroom.getAcademicYear());
			managerClassroom.remove(classroom);
			gotoPage = "./showClassroomList?academicYear=" + aC;
		} catch (EntityNotFoundException entityNotFoundException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + entityNotFoundException.getMessage();
			gotoPage = "./error.jsp";
			entityNotFoundException.printStackTrace();
		} catch (ConnectionException connectionException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
		} catch (SQLException SQLException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + SQLException.getMessage();
			gotoPage = "./error.jsp";
			SQLException.printStackTrace();
		} catch (FieldRequiredException mandatoryFieldException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + mandatoryFieldException.getMessage();
			gotoPage = "./error.jsp";
			mandatoryFieldException.printStackTrace();
		} catch (ValueInvalidException invalidValueException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
		} /*
			 * catch (DeleteManagerException deleteManagerException) { errorMessage =
			 * Environment.DEFAULT_ERROR_MESSAGE + deleteManagerException.getMessage();
			 * gotoPage = "./error.jsp"; deleteManagerException.printStackTrace(); }
			 */catch (IOException ioException) {
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
