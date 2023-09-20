package smos.application.registerManagement;

import java.io.IOException;
import java.sql.SQLException;

public class ServletUpdateJustified extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8405877983387271542L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "./registerManagement/showRegister.jsp";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();
		ManagerUser managerUser = ManagerUser.getInstance();
		ManagerRegister mR = ManagerRegister.getInstance();
		Excuse excuse = (Excuse) session.getAttribute("excuse");
		User loggedUser = (User) session.getAttribute("loggedUser");
		String idClass = (String) session.getAttribute("idClass");
		int id = Integer.parseInt(idClass);
		gotoPage += "?idClass=" + id;

		try {
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}
			if (!managerUser.eAdministrator(loggedUser)) {
				errorMessage = "L'User collegato non ha accesso alla " + "funzionalita'!";
				gotoPage = "./error.jsp";
			}

			excuse.setAcademicYear(Integer.parseInt((pRequest.getParameter("academicYear"))));
			excuse.setDataExcuse(Utility.String2Date(pRequest.getParameter("date")));

			/*
			 * verifichiamo che la giustifica esista.
			 */

			if (!mR.exists(excuse)) {
				errorMessage = "la giustifica non exists!!";
				gotoPage = "./error.jsp";

			} else {// aggiorniamo
				mR.updateExcuse(excuse);
			}

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
		} catch (FieldRequiredException mandatoryFieldException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + mandatoryFieldException.getMessage();
			gotoPage = "./error.jsp";
			mandatoryFieldException.printStackTrace();
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
