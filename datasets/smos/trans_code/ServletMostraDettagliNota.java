package smos.application.registerManagement;

import java.io.IOException;
import java.sql.SQLException;

public class ServletShowDetailsNote extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7648669052646601677L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();
		ManagerUser managerUser = ManagerUser.getInstance();
		User loggedUser = (User) session.getAttribute("loggedUser");

		// instanziare gli oggetti qua

		ManagerRegister managerRegister = ManagerRegister.getInstance();

		try {
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}
			if (!managerUser.eAdministrator(loggedUser)) {
				errorMessage = "L'User collegato non ha accesso alla " + "funzionalita'!";
				gotoPage = "./error.jsp";
			}
			// set le cose da passare alla session, usare session.setAttribute(String,
			// attribute)

			if (pRequest.getParameter("update") == null) {
				User student = (User) session.getAttribute("student");
				// managerUser.getUserById(Integer.valueOf((String)pRequest.getAttribute("student")));
				session.setAttribute("student", student);
				session.setAttribute("idStudent", student.getId());
				session.setAttribute("note", managerRegister
						.getNotePerId(Integer.valueOf(pRequest.getParameter("noteId"))));
				gotoPage = "./registerManagement/showNoteDetails.jsp";
			} else {

				Note nNote = new Note();
				nNote.setAcademicYear(((Class) session.getAttribute("classroom")).getAcademicYear());
				nNote.setDataNote(Utility.String2Date(pRequest.getParameter("dateNote")));
				nNote.setIdUser((Integer) session.getAttribute("idStudent"));
				nNote.setTeacher(pRequest.getParameter("noteTeacher"));
				nNote.setDescription(pRequest.getParameter("noteDescription"));
				nNote.setIdNote(((Note) session.getAttribute("note")).getIdNote());

				try {
					managerRegister.updateNote(nNote);
					gotoPage = "./showNoteList?student=" + session.getAttribute("idStudent");
				} catch (FieldRequiredException e) {
					session.setAttribute("error", e.getMessage());
					gotoPage = "./registerManagement/showNoteDetails.jsp";
				}

			}

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
