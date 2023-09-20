package smos.application.registerManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

public class ServletViewRegistration extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4054623648928396283L;

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
		User loggedUser = (User) session.getAttribute("loggedUser");
		ManagerClass managerClassroom = ManagerClass.getInstance();
		ManagerRegister managerRegister = ManagerRegister.getInstance();
		Collection<RegisterLine> register = null;
		int year;
		int month;
		int day;

		String date = pRequest.getParameter("date");
		int idClass = Integer.valueOf(pRequest.getParameter("idClass"));

		String[] datevalues;
		datevalues = date.split("/");
		year = Integer.valueOf(datevalues[2]);
		month = Integer.valueOf(datevalues[1]);
		day = Integer.valueOf(datevalues[0]);

		try {
			register = managerRegister.getRegistrationPerClassIDEData(idClass, Utility.String2Date(date));
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}
			if ((!managerUser.eAdministrator(loggedUser)) && (!managerUser.eDirettore(loggedUser))) {
				errorMessage = "L'User collegato non ha accesso alla " + "funzionalita'!";
				gotoPage = "./error.jsp";
			}
			// set le cose da passare alla session, usare session.setAttribute(String,
			// attribute)

			Class classroom = managerClassroom.getClassPerID(idClass);

			session.setAttribute("register", register);
			session.setAttribute("year", year);
			session.setAttribute("month", month);
			session.setAttribute("day", day);

			session.setAttribute("classroom", classroom);

			// prendere l'academic year dalla session
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
