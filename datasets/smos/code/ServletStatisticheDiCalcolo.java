package smos.application.registerManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import smos.Ambiente;
import smos.bean.Utente;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.storage.ManagerClasse;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;

public class ServletStatisticheDiCalcolo extends HttpServlet {

	private static final long serialVersionUID = 6690162445433486239L;
	
	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	@SuppressWarnings("deprecation")
	protected void doGet(HttpServletRequest pRichiesta,
			HttpServletResponse pRisposta) {
		String gotoPage = "./statisticsManagement/showStatistictsByAcademicYear.jsp";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		Integer academicYear = Integer.valueOf(pRichiesta.getParameter("academicYear"));
		Integer absenceLimit = Integer.valueOf(pRichiesta.getParameter("absenceLimit"));
		Integer noteLimit = Integer.valueOf(pRichiesta.getParameter("noteLimit"));
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
		Collection <Utente> alertStudentAbsence = new Vector<Utente>();
		
		Date today = new Date();
		
		
		
		int [][] unjustifiedAbsence = null;
		
		Collection <Utente> alertStudentNote = new Vector<Utente>();
		
		int [][] note = null;
		
		Utente tmpUser = null;
		
		try {
		
		if (loggedUser == null) {
			pRisposta.sendRedirect("./index.htm");
			return;
		}
		if ((!managerUser.eAmministratore(loggedUser)) && (!managerUser.eDirettore(loggedUser))) {
			messaggioDiErrore = "L'Utente collegato non ha accesso alla "
					+ "funzionalita'!";
			gotoPage = "./error.jsp";
		}
		
		
			
		if (academicYear == 0){
			academicYear = today.getYear() + 1900;
		}
		
		Collection <Integer> academicYearList = ManagerClasse.ottenereIstanza().ottenereAnnoAccademicoElenco();
		
		unjustifiedAbsence = managerUser.ottenereHighlightsStudenteAssenza(academicYear);
		
		if (unjustifiedAbsence != null){
			for (int i=0; i< unjustifiedAbsence.length; i++){
					if (unjustifiedAbsence[i][0] >= absenceLimit){
						tmpUser = managerUser.ottenereUtentePerId(unjustifiedAbsence[i][1]);
						alertStudentAbsence.add(tmpUser);
					}
			}
		}
		
		note = managerUser.ottenereHighlightsStudenteNota(academicYear);
		
		if (note != null){
			for (int i=0; i< note.length; i++){
					if (note[i][0] >= noteLimit){
						
							tmpUser = managerUser.ottenereUtentePerId(note[i][1]);
						
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
		}

		try {
			pRisposta.sendRedirect(gotoPage);
		} catch (IOException ioException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
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
