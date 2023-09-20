package smos.application.classroomManagement;

import java.io.IOException;
import java.sql.SQLException;

public class ServletInsertClass extends HttpServlet {

	/**
	 * Servlet to insert a class
	 * 
	 * @author Nicola Pisanti
	 * @version 0.9
	 */
	private static final long serialVersionUID = 1355159545343902216L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	public void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		int aC = Integer.valueOf(pRequest.getParameter("academicYear"));
		String gotoPage = "./showClassroomList?academicYear=" + aC;

		String errorMessage = "";
		HttpSession session = pRequest.getSession();
		ManagerUser managerUser = ManagerUser.getInstance();
		ManagerClass managerClassroom = ManagerClass.getInstance();
		User loggedUser = (User) session.getAttribute("loggedUser");

		String isWizard = "yes";

		try {
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}
			if (!managerUser.eAdministrator(loggedUser)) {
				errorMessage = "L'User collegato non ha accesso alla " + "funzionalita'!";
				gotoPage = "./error.jsp";
			}

			int idAdd = (int) Integer.valueOf(pRequest.getParameter("address"));

			Class classroom = new Class();
			classroom.setName(pRequest.getParameter("name"));
			classroom.setAcademicYear(aC);
			classroom.setIdStreetAddress(idAdd);

			if (classroom.getAcademicYear() < 1970) {
				throw new ValueInvalidException("l'anno inserito e troppo vecchio");

			}

			if (!(managerClassroom.exists(classroom))) {
				managerClassroom.insert(classroom);
				session.setAttribute("isWizard", isWizard);
			} else {
				throw new DuplicateEntityException("la classe gia exists nel database");
			}

		} catch (SQLException SQLException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + SQLException.getMessage();
			gotoPage = "./error.jsp";
			SQLException.printStackTrace();
		} catch (ConnectionException connectionException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
		} catch (FieldRequiredException mandatoryFieldException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + mandatoryFieldException.getMessage();
			gotoPage = "./error.jsp";
			mandatoryFieldException.printStackTrace();
		} catch (EntityNotFoundException entityNotFoundException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + entityNotFoundException.getMessage();
			gotoPage = "./error.jsp";
			entityNotFoundException.printStackTrace();
		} catch (ValueInvalidException invalidValueException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
		} catch (DuplicateEntityException duplicatedEntityException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + duplicatedEntityException.getMessage();
			gotoPage = "./error.jsp";
			duplicatedEntityException.printStackTrace();
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
