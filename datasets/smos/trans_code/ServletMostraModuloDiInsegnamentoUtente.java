package smos.application.userManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

public class ServletShowModuleTeachingUser extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2305151029867525356L;

	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "./persistentDataManagement/userManagement/showTeacherDetails.jsp";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();

		User user = null;
		ManagerUser managerUser = ManagerUser.getInstance();
		ManagerClass managerClassroom = ManagerClass.getInstance();

		User loggedUser = (User) session.getAttribute("loggedUser");

		try {
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}

			if ((!managerUser.eAdministrator(loggedUser))) {
				errorMessage = "L'User collegato non ha accesso alla " + "funzionalita'!";
				gotoPage = "./error.jsp";
			}
			user = (User) session.getAttribute("user");

			if (!managerUser.eTeacher(user)) {
				errorMessage = "L'User non e un docente";
				gotoPage = "./error.jsp";

			}
			// int idTeacher= user.getId();

			Collection<Class> classList = managerClassroom.getClassPerTeacher(user);

			/*
			 * Iterator<Classroom> iteClass = classList.iterator(); Classroom tmp = null;
			 * while(iteClass.hasNext()){ tmp=iteClass.next(); if(tmp.getAcademicYear()!=
			 * an){ classList.remove(tmp); } }
			 */
			// @SuppressWarnings("unused")
			// Collection<Teaching> teachingListByClassroom=null;
			// Collection<Classroom,Teaching> list= new Vector <Classroom,Teaching>();

			session.setAttribute("classroomList", classList);

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
		} catch (FieldRequiredException mandatoryFieldException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + mandatoryFieldException.getMessage();
			gotoPage = "./error.jsp";
			mandatoryFieldException.printStackTrace();
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
