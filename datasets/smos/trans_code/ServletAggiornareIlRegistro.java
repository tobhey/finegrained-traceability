package smos.application.registerManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class ServletUpdateTheRegister extends HttpServlet {

	private static final long serialVersionUID = 5966298318913522686L;

	protected void doGet(HttpServletRequest pRequest, HttpServletResponse pReply) {
		String gotoPage = "./registerManagement/showClassroomList.jsp";
		String errorMessage = "";
		HttpSession session = pRequest.getSession();

		// Variabile booleana utilizzata per verificare se lo student ha o meno
		// un'absence
		boolean flag = false;

		// Collection utilizzata per la memorizzazione del registro di una particolare
		// data
		Collection<RegisterLine> register = null;
		// Iteratore necessario a scorrere la collection
		Iterator itRegister = null;
		// Variabile temporanea necessaria a leggere le informazioni dalla collection
		RegisterLine tmpRegisterLine = null;
		// Variabile temporanea necessaria a leggere le informazioni dalla collection
		ItemListUsers student = null;

		// Variabile temporanea necessaria all'inserimento delle nuove assenze
		Absence tmpAbsence = null;

		// Variabile temporanea necessaria all'inserimento di nuovi ritardi
		Delay tmpDelay = null;

		// Classi manager necessarie all'elaborazione
		ManagerUser managerUser = ManagerUser.getInstance();
		ManagerRegister managerRegister = ManagerRegister.getInstance();

		// Variabili necessarie per la memorizzazione dei dati provenienti dalla request
		String[] absences = null; // Memorizza gli alunni assenti
		String[] delays = null; // Memorizza gli alunni ritardatari

		// Recupero l'user loggato dalla sessione
		User loggedUser = (User) session.getAttribute("loggedUser");
		// Verifico che l'user loggato abbia i permessi necessari
		try {
			if (loggedUser == null) {
				pReply.sendRedirect("./index.htm");
				return;
			}

			if (!managerUser.eAdministrator(loggedUser)) {
				errorMessage = "L'User collegato non ha accesso alla " + "funzionalita'!";
				gotoPage = "./error.jsp";
				return;
			}

			// Recupero i parametri dalla pRequest
			Date date = Utility.String2Date(pRequest.getParameter("date"));
			absences = pRequest.getParameterValues("absences");
			delays = pRequest.getParameterValues("delays");

			// Recupero l'oggetto classroom dalla session
			Class classroom = ((Class) session.getAttribute("classroom"));

			/*
			 * Invoco il metodo della managerRegister per recuperare dal db le informazioni
			 * inerenti il registro di una classe ad una particolare data (Assenze, Ritardi)
			 */
			register = managerRegister.getRegistrationPerClassIDEData(classroom.getIdClass(), date);

			if (register != null) {
				itRegister = register.iterator();
			}

			if (itRegister != null) {
				while (itRegister.hasNext()) {
					tmpRegisterLine = (RegisterLine) itRegister.next();
					// Recupero lo student cui la register line si riferisce
					student = tmpRegisterLine.getStudent();

					// Verifico se per lo student e' stata inserita o meno un'absence
					if (absences != null) {
						for (int i = 0; i < absences.length; i++) {
							if (Integer.valueOf(absences[i]) == student.getId()) {
								flag = true;
								if (!managerRegister.haveAbsence(tmpRegisterLine)) {
									tmpAbsence = new Absence();
									tmpAbsence.setAcademicYear(classroom.getAcademicYear());
									tmpAbsence.setDateOfAbsence(date);
									tmpAbsence.setIdExcuse(0);
									tmpAbsence.setIdUser(student.getId());
									managerRegister.insertAbsence(tmpAbsence);
								}
							}

						}
						if (!flag) {
							if (managerRegister.haveAbsence(tmpRegisterLine)) {
								managerRegister.removeAbsence(tmpRegisterLine.getAbsence());
							}
						}
					} else {
						if (managerRegister.haveAbsence(tmpRegisterLine)) {
							managerRegister.removeAbsence(tmpRegisterLine.getAbsence());
						}
					}
					flag = false;

					// Verifico se per lo student e' stata inserito o meno un delay
					if (delays != null) {
						for (int i = 0; i < delays.length; i++) {
							if (Integer.valueOf(delays[i]) == student.getId()) {
								flag = true;
								if (!managerRegister.haveDelay(tmpRegisterLine)) {
									tmpDelay = new Delay();
									tmpDelay.setAcademicYear(classroom.getAcademicYear());
									tmpDelay.setDateDelay(date);
									tmpDelay.setIdUser(student.getId());
									tmpDelay.setTimeDelay(pRequest.getParameter("hour_" + student.getId()));
									managerRegister.insertDelay(tmpDelay);
								} else {
									tmpDelay = tmpRegisterLine.getDelay();
									tmpDelay.setTimeDelay(pRequest.getParameter("hour_" + student.getId()));
									managerRegister.updateDelay(tmpDelay);
								}

							}

						}
						if (!flag) {
							if (managerRegister.haveDelay(tmpRegisterLine)) {
								managerRegister.removeDelay(tmpRegisterLine.getDelay());
							}
						}
					} else {
						if (managerRegister.haveDelay(tmpRegisterLine)) {
							managerRegister.removeDelay(tmpRegisterLine.getDelay());
						}
					}
					flag = false;
				}
			}

		} catch (IOException ioException) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
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
		} catch (ValueInvalidException e) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + e.getMessage();
			gotoPage = "./error.jsp";
			e.printStackTrace();
		} catch (FieldRequiredException e) {
			errorMessage = Environment.DEFAULT_ERROR_MESSAGE + e.getMessage();
			gotoPage = "./error.jsp";
			e.printStackTrace();
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

	protected void doPost(HttpServletRequest pRequest, HttpServletResponse pReply) {
		this.doGet(pRequest, pReply);
	}

}
