package smos.application.registerManagement;

import java.io.IOException;
import java.sql.SQLException;

public class ServletInsertJustify extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1252760418542867296L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	public void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "./registerManagement/showClassroomList.jsp";
		String errorMessage = "";

		HttpSession session = pRequest.getSession();
		ManagerUser managerUser = ManagerUser.getInstance();
		User loggedUser = (User) session.getAttribute("loggedUser");

		try {
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}
			if (!managerUser.eAdministrator(loggedUser)) {
				errorMessage = "L'User collegato non ha accesso alla " + "funzionalita'!";
				gotoPage = "./error.jsp";
			}
			ManagerRegister mR = ManagerRegister.getInstance();
			Excuse excuse = new Excuse();
			excuse.setAcademicYear(Integer.parseInt(pRequest.getParameter("academicYear")));

			excuse.setDataExcuse(Utility.String2Date(pRequest.getParameter("date")));

			excuse.setIdUser(Integer.parseInt(pRequest.getParameter("idUser")));
			String idA = pRequest.getParameter("idAbsence");
			int idAbsence = Integer.parseInt(idA);

			// String idC = pRequest.getParameter("idClass");
			// int idClass= Integer.parseInt(idC);

			// gotoPage+=idClass;
			Absence absence = mR.getAbsencePerIdAbsence(idAbsence);

			if (!mR.exists(absence)) {
				errorMessage = "absence non prensente nel db!";
				gotoPage = "./error.jsp";
			}

			// inserimento giustifica

			if (!mR.exists(excuse)) {
				mR.insertExcuse(excuse, absence);
				session.setAttribute("excuse", excuse);

			} else
				throw new DuplicateEntityException("Justify gia' existsnte");

		} catch (SQLException SQLException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + SQLException.getMessage();
			gotoPage = "./error.jsp";
			SQLException.printStackTrace();
		} catch (ConnectionException connectionException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
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
