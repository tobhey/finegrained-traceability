package smos.application.userManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

/**
 * Servlet used to remove Teachings from a teacher
 * 
 * 
 * @author Giulio D'Amora
 * @version 1.0
 * 
 * 
 */
public class ServletRemoveTeachingPerTeacher extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8007609698841510837L;

	/**
	 * Definition of the doGet method
	 * 
	 * @param pRequest
	 * @param pReply
	 * 
	 */
	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "./persistentDataManagement/userManagement/teacherTeachings.jsp";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();

		ManagerUser managerUser = ManagerUser.getInstance();
		ManagerTeaching managerTeaching = ManagerTeaching.getInstance();
		ManagerClass managerClass = ManagerClass.getInstance();
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
			// //Mi servono i 3 Id user class teachings(che non so quanti ne sono)
			int idTeacher = ((User) session.getAttribute("user")).getId();
			User teacher = managerUser.getUserPerId(idTeacher);
			int idClass = Integer.valueOf(pRequest.getParameter("classId"));
			String[] idTeachingList = pRequest.getParameterValues("unselectedTeachings");
			int nTeaching = idTeachingList.length;
			int temp;
			// Collection<Teaching> listSelcected = new Vector<Teaching>();
			if (idTeachingList == null)
				gotoPage = "./error.jsp";
			else {
				for (int i = 0; i < nTeaching; i++) {
					temp = Integer.valueOf(idTeachingList[i]);
					if (managerUser.hasTeaching(teacher, managerTeaching.getTeachingPerId(temp),
							managerClass.getClassPerID(idClass))) {
						managerUser.removeTeacherInClassTeaching(teacher, idClass, temp);
						// listSelcected.add(managerTeaching.getTeachingById(temp));
					}
				}
			}
			// session.setAttribute("teachingListTeacher", listSelcected);
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
		} catch (ValueInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pRequest.getSession().setAttribute("errorMessage", errorMessage);
		try {
			pReply.sendRedirect(gotoPage);
		} catch (IOException ioException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + ioException.getMessage();
			gotoPage = "./error6.jsp";
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
