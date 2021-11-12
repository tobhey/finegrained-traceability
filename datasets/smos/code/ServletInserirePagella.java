package smos.application.reportManagement;

import smos.Ambiente;
import smos.bean.Classe;
import smos.bean.Insegnamento;
import smos.bean.Utente;
import smos.bean.VoceElencoUtenti;
import smos.bean.Voto;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.exception.CampoObbligatorioEccezione;
import smos.storage.ManagerInsegnamento;
import smos.storage.ManagerUtente;
import smos.storage.ManagerVoto;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet utilizzata per inserire una pagella di uno studente.
 * 
 * @author Giulio D'Amora.
 * @version 1.0
 * 
 *          
 */
public class ServletInserirePagella extends HttpServlet {

	private static final long serialVersionUID = 8121220088758892213L;

	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doGet(HttpServletRequest pRichiesta,
			HttpServletResponse pRisposta) {
		String gotoPage = "./showReports";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		Collection<Insegnamento> teachingList = null;
		ManagerVoto managerVotes = ManagerVoto.ottenereIstanza();
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		ManagerInsegnamento managerTeaching = ManagerInsegnamento.ottenereIstanza();
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");

		try {
			if (loggedUser == null) {
				pRisposta.sendRedirect("./index.htm");
				return;
			}
			if (!managerUser.eAmministratore(loggedUser)) {
				messaggioDiErrore = "L'Utente collegato non ha accesso alla funzionalita'!";
				gotoPage = "./error.jsp";
			}
			Classe classroom = (Classe) session.getAttribute("classroom");
			// Lista teaching
			teachingList = managerTeaching.ottenereInsegnamentiPerClasseId(classroom
					.ottenereIdIndirizzo());
			Iterator<Insegnamento> itTeaching = teachingList.iterator();
			Integer year = (Integer) session.getAttribute("selectedYear");
			VoceElencoUtenti student = (VoceElencoUtenti) session
					.getAttribute("student");
			// Quadrimestre
			int turn = (Integer) session.getAttribute("q");
			Insegnamento teachingTemp = null;
			int idTemp;
			String write, oral, lab;
			gotoPage += "?student=" + student.ottenereId() + "&q=" + turn;
			while (itTeaching.hasNext()) {
				teachingTemp = itTeaching.next();
				idTemp = teachingTemp.ottenereId();
				write = "scritto_" + idTemp;
				oral = "orale_" + idTemp;
				lab = "laboratorio_" + idTemp;
				write = pRichiesta.getParameter(write);
				oral = pRichiesta.getParameter(oral);
				lab = pRichiesta.getParameter(lab);
				Voto newVotes = new Voto();
				int writeInt=0, oralInt=0,labInt=0;
				if(write!="")
					writeInt = Integer.valueOf(write);
				if(oral!="")
					oralInt = Integer.valueOf(oral);
				if(lab!="")
					labInt = Integer.valueOf(lab);
				if (writeInt != 0 || oralInt != 0 || labInt != 0) {
					newVotes.settareAnnoAccademico(year);
					newVotes.settareId_utente(student.ottenereId());
					newVotes.settareLaboratorio(labInt);
					newVotes.settareOrale(oralInt);
					newVotes.settareInsegnamento(idTemp);
					newVotes.settareTurno(turn);
					newVotes.settareScritto(writeInt);
					managerVotes.inserire(newVotes);
				}

			}

			pRisposta.sendRedirect(gotoPage);
			return;

		} catch (SQLException sqlException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ sqlException.getMessage();
			gotoPage = "./error.jsp";
			sqlException.printStackTrace();
		} catch (EntitaNonTrovataEccezione entityNotFoundException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ entityNotFoundException.getMessage();
			gotoPage = "./error.jsp";
			entityNotFoundException.printStackTrace();
		} catch (ConnessioneEccezione connectionException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
		} catch (IOException ioException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
		} catch (ValoreNonValidoEccezione invalidValueException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
		} catch (CampoObbligatorioEccezione e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pRichiesta.getSession().setAttribute("messaggioDiErrore", messaggioDiErrore);
		try {
			pRisposta.sendRedirect(gotoPage);
		} catch (IOException ioException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
		}
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
