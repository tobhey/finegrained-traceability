package smos.application.registerManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

public class ServletCalculationStatistics extends HttpServlet {

	private static final long serialVersionUID = 6690162445433486239L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	@SuppressWarnings("deprecation")
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "./statisticsManagement/showStatistictsByAcademicYear.jsp";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();
		Integer academicYear = Integer.valueOf(pRequest.getParameter("academicYear"));
		Integer absenceLimit = Integer.valueOf(pRequest.getParameter("absenceLimit"));
		Integer noteLimit = Integer.valueOf(pRequest.getParameter("noteLimit"));
		ManagerUser managerUser = ManagerUser.getInstance();
		User loggedUser = (User) session.getAttribute("loggedUser");
		Collection<User> alertStudentAbsence = new Vector<User>();

		Date today = new Date();

		int[][] unjustifiedAbsence = null;

		Collection<User> alertStudentNote = new Vector<User>();

		int[][] note = null;

		User tmpUser = null;

		try {

			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}
			if ((!managerUser.eAdministrator(loggedUser)) && (!managerUser.eDirettore(loggedUser))) {
				errorMessage = "L'User collegato non ha accesso alla " + "funzionalita'!";
				gotoPage = "./error.jsp";
			}

			if (academicYear == 0) {
				academicYear = today.getYear() + 1900;
			}

			Collection<Integer> academicYearList = ManagerClass.getInstance().getAcademicYearList();

			unjustifiedAbsence = managerUser.getHighlightsStudentAbsence(academicYear);

			if (unjustifiedAbsence != null) {
				for (int i = 0; i < unjustifiedAbsence.length; i++) {
					if (unjustifiedAbsence[i][0] >= absenceLimit) {
						tmpUser = managerUser.getUserPerId(unjustifiedAbsence[i][1]);
						alertStudentAbsence.add(tmpUser);
					}
				}
			}

			note = managerUser.getHighlightsStudentNote(academicYear);

			if (note != null) {
				for (int i = 0; i < note.length; i++) {
					if (note[i][0] >= noteLimit) {

						tmpUser = managerUser.getUserPerId(note[i][1]);

						alertStudentNote.add(tmpUser);
					}
				}
			}

			session.setAttribute("alertStudentAbsence", alertStudentAbsence);
			session.setAttribute("alertStudentNote", alertStudentNote);
			session.setAttribute("academicYearList", academicYearList);
			session.setAttribute("absenceLimit", absenceLimit);
			session.setAttribute("noteLimit", noteLimit);
			session.setAttribute("yearSelected", academicYear);

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

		try {
			pReply.sendRedirect(gotoPage);
		} catch (IOException ioException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
		}

		session.setAttribute("errorMessage", errorMessage);
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
