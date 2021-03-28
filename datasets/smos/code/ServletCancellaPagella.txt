package smos.application.reportManagement;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import smos.Ambiente;
import smos.bean.Utente;
import smos.bean.VoceElencoUtenti;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.storage.ManagerUtente;
import smos.storage.ManagerVoto;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;

/**
 * Servlet utilizzata per visualizzare gli alunni associati ad una Classe.
 * 
 * @author Giulio D'Amora
 * 
 */
public class ServletCancellaPagella extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2020233250419553067L;

	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doGet(HttpServletRequest pRichiesta,
			HttpServletResponse pRisposta) {
		String gotoPage = "./reportsManagement/showStudentsByClass.jsp";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		ManagerVoto managerVotes = ManagerVoto.ottenereIstanza();
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
		int year= 0;
		int turn=0;
		int studentId=0;
		try {
			if (loggedUser == null) {
				pRisposta.sendRedirect("./index.htm");
				return;
			}
			if (!managerUser.eAmministratore(loggedUser)) {
				messaggioDiErrore = "L'Utente collegato non ha accesso alla "
						+ "funzionalita'!";
				gotoPage = "./error2.jsp";
			}
			VoceElencoUtenti student = (VoceElencoUtenti) (session.getAttribute("student"));
			studentId = student.ottenereId();
			year=(Integer) session.getAttribute("selectedYear");
			turn=(Integer) session.getAttribute("q");
			managerVotes.eliminareVotoPerUtenteIdAnnoTurno(studentId, year, turn);
			pRisposta.sendRedirect(gotoPage);
			

		} catch (NumberFormatException numberFormatException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ numberFormatException.getMessage();
			gotoPage = "./error1.jsp?Year="+year+"&turn="+turn+"&idStudent="+studentId;
			numberFormatException.printStackTrace();
		} catch (EntitaNonTrovataEccezione entityNotFoundException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ entityNotFoundException.getMessage();
			gotoPage = "./error3.jsp";
			entityNotFoundException.printStackTrace();
		} catch (ConnessioneEccezione connectionException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ connectionException.getMessage();
			gotoPage = "./error4.jsp";
			connectionException.printStackTrace();
		} catch (SQLException SQLException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ SQLException.getMessage();
			gotoPage = "./error5.jsp?Year="+year+"&turn="+turn+"&idStudent="+studentId;
			SQLException.printStackTrace();
		} catch (IOException ioException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ ioException.getMessage();
			gotoPage = "./error6.jsp";
			ioException.printStackTrace();
		} catch (ValoreNonValidoEccezione e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		session.setAttribute("messaggioDiErrore", messaggioDiErrore);
	}

	/**
	 * Definizione del metodo doPost
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doPost(HttpServletRequest pRichiesta,
			HttpServletResponse pRisposta) {
		this.doGet(pRichiesta, pRisposta);
	}

}

