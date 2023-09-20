package smos.application.reportManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

/**
 * Servlet used to insert a student schoolReport.
 * 
 * @author Giulio D'Amora.
 * @version 1.0
 * 
 * 
 */
public class ServletInsertSchoolReport extends HttpServlet {

	private static final long serialVersionUID = 8121220088758892213L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "./showReports";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();
		Collection<Teaching> teachingList = null;
		ManagerGrade managerVotes = ManagerGrade.getInstance();
		ManagerUser managerUser = ManagerUser.getInstance();
		ManagerTeaching managerTeaching = ManagerTeaching.getInstance();
		User loggedUser = (User) session.getAttribute("loggedUser");

		try {
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}
			if (!managerUser.eAdministrator(loggedUser)) {
				errorMessage = "L'User collegato non ha accesso alla funzionalita'!";
				gotoPage = "./error.jsp";
			}
			Class classroom = (Class) session.getAttribute("classroom");
			// Lista teaching
			teachingList = managerTeaching.getTeachingsPerClassId(classroom.getIdStreetAddress());
			Iterator<Teaching> itTeaching = teachingList.iterator();
			Integer year = (Integer) session.getAttribute("selectedYear");
			ItemListUsers student = (ItemListUsers) session.getAttribute("student");
			// Quadrimestre
			int turn = (Integer) session.getAttribute("q");
			Teaching teachingTemp = null;
			int idTemp;
			String write, oral, lab;
			gotoPage += "?student=" + student.getId() + "&q=" + turn;
			while (itTeaching.hasNext()) {
				teachingTemp = itTeaching.next();
				idTemp = teachingTemp.getId();
				write = "writing_" + idTemp;
				oral = "oral_" + idTemp;
				lab = "laboratory_" + idTemp;
				write = pRequest.getParameter(write);
				oral = pRequest.getParameter(oral);
				lab = pRequest.getParameter(lab);
				Grade newVotes = new Grade();
				int writeInt = 0, oralInt = 0, labInt = 0;
				if (write != "")
					writeInt = Integer.valueOf(write);
				if (oral != "")
					oralInt = Integer.valueOf(oral);
				if (lab != "")
					labInt = Integer.valueOf(lab);
				if (writeInt != 0 || oralInt != 0 || labInt != 0) {
					newVotes.setAcademicYear(year);
					newVotes.setId_user(student.getId());
					newVotes.setLaboratory(labInt);
					newVotes.setOral(oralInt);
					newVotes.setTeaching(idTemp);
					newVotes.setRotation(turn);
					newVotes.setWriting(writeInt);
					managerVotes.insert(newVotes);
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
		} catch (FieldRequiredException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
