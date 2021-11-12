package smos.application.registerManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import smos.Ambiente;
import smos.bean.Assenza;
import smos.bean.Classe;
import smos.bean.Ritardo;
import smos.bean.RegistratiLinea;
import smos.bean.Utente;
import smos.bean.VoceElencoUtenti;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.exception.CampoObbligatorioEccezione;
import smos.storage.ManagerRegistrati;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;
import smos.utility.Utility;

public class ServletAggiornareIlRegistro extends HttpServlet {

	private static final long serialVersionUID = 5966298318913522686L;
	
	protected void doGet(HttpServletRequest pRichiesta,
			HttpServletResponse pRisposta) {
		String gotoPage = "./registerManagement/showClassroomList.jsp";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		
		//Variabile booleana utilizzata per verificare se lo studente ha o meno un'assenza
		boolean flag = false;
		
		//Collection utilizzata per la memorizzazione del registro di una particolare data
		Collection<RegistratiLinea> register = null;
		//Iteratore necessario a scorrere la collection
		Iterator itRegister = null;
		//Variabile temporanea necessaria a leggere le informazioni dalla collection
		RegistratiLinea tmpRegisterLine = null;
		//Variabile temporanea necessaria a leggere le informazioni dalla collection
		VoceElencoUtenti student = null;
		
		//Variabile temporanea necessaria all'inserimento delle nuove assenze
		Assenza tmpAbsence = null;
		
		//Variabile temporanea necessaria all'inserimento di nuovi ritardi
		Ritardo tmpDelay = null;
		
		//Classi manager necessarie all'elaborazione
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		ManagerRegistrati managerRegister = ManagerRegistrati.ottenereIstanza();
		
		//Variabili necessarie per la memorizzazione dei dati provenienti dalla request
		String[] absences = null; //Memorizza gli alunni assenti
		String[] delays = null; //Memorizza gli alunni ritardatari
		
		//Recupero l'utente loggato dalla sessione
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
		//Verifico che l'utente loggato abbia i permessi necessari
		try {
			if (loggedUser == null) {
				pRisposta.sendRedirect("./index.htm");
				return;
			}

			if (!managerUser.eAmministratore(loggedUser)) {
				messaggioDiErrore = "L'Utente collegato non ha accesso alla "
						+ "funzionalita'!";
				gotoPage = "./error.jsp";
				return;
			}
		
		//Recupero i parametri dalla pRichiesta
		Date date = Utility.String2Date(pRichiesta.getParameter("date"));
		absences = pRichiesta.getParameterValues("absences");
		delays = pRichiesta.getParameterValues("delays");
		
		//Recupero l'oggetto classroom dalla session
		Classe classroom = ((Classe) session.getAttribute("classroom"));
		
		/*Invoco il metodo della managerRegister per recuperare dal db le informazioni
		 * inerenti il registro di una classe ad una particolare data (Assenze, Ritardi)
		 */
		register = managerRegister.ottenereRegistratiPerClasseIDEData(classroom.ottenereIdClasse(),date);
			
		if (register != null){
			itRegister = register.iterator();
		}
		
		if (itRegister != null){
			while(itRegister.hasNext()){
				tmpRegisterLine = (RegistratiLinea) itRegister.next();
				//Recupero lo studente cui la register line si riferisce
				student = tmpRegisterLine.ottenereStudente();
				
				//Verifico se per lo studente e' stata inserita o meno un'assenza
				if (absences != null){
					for (int i=0; i<absences.length; i++){
						if (Integer.valueOf(absences[i]) == student.ottenereId()){
							flag = true;
							if (!managerRegister.avereAssenza(tmpRegisterLine)){
								tmpAbsence = new Assenza();
								tmpAbsence.settareAnnoAccademico(classroom.ottenereAnnoAccademico());
								tmpAbsence.settareDataAssenza(date);
								tmpAbsence.settareIdGiustificare(0);
								tmpAbsence.settareIdUtente(student.ottenereId());
								managerRegister.inserireAssenza(tmpAbsence);
							}
						}
						
					}
					if (!flag){
						if (managerRegister.avereAssenza(tmpRegisterLine)){
							managerRegister.eliminareAssenza(tmpRegisterLine.ottenereAssenza());
						}
					}
				} else {
					if (managerRegister.avereAssenza(tmpRegisterLine)){
						managerRegister.eliminareAssenza(tmpRegisterLine.ottenereAssenza());
					}
				}
				flag = false;
				
				//Verifico se per lo studente e' stata inserito o meno un ritardo
				if (delays != null){
					for (int i=0; i<delays.length; i++){
						if (Integer.valueOf(delays[i]) == student.ottenereId()){
							flag = true;
							if (!managerRegister.avereRitardo(tmpRegisterLine)){
								tmpDelay = new Ritardo();
								tmpDelay.settareAnnoAccademico(classroom.ottenereAnnoAccademico());
								tmpDelay.settareDataRitardo(date);
								tmpDelay.settareIdUtente(student.ottenereId());
								tmpDelay.settareTempoRitardo(pRichiesta.getParameter("hour_" + student.ottenereId()));
								managerRegister.inserireRitardo(tmpDelay);
							} else {
								tmpDelay = tmpRegisterLine.ottenereRitardo();
								tmpDelay.settareTempoRitardo(pRichiesta.getParameter("hour_" + student.ottenereId()));
								managerRegister.aggiornareRitardo(tmpDelay);
							}
							
						}
						
					}
					if (!flag){
						if (managerRegister.avereRitardo(tmpRegisterLine)){
							managerRegister.eliminareRitardo(tmpRegisterLine.ottenereRitardo());
						}
					}
				} else {
					if (managerRegister.avereRitardo(tmpRegisterLine)){
						managerRegister.eliminareRitardo(tmpRegisterLine.ottenereRitardo());
					}
				}
				flag = false;
			}
		}
			
			

		} catch (IOException ioException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
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
		} catch (ValoreNonValidoEccezione e) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE + e.getMessage();
			gotoPage = "./error.jsp";
			e.printStackTrace();
		} catch (CampoObbligatorioEccezione e) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE + e.getMessage();
			gotoPage = "./error.jsp";
			e.printStackTrace();
		}
		
		
		session.setAttribute("messaggioDiErrore", messaggioDiErrore);
		try {
			pRisposta.sendRedirect(gotoPage);
		} catch (IOException ioException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE + ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest pRichiesta,
			HttpServletResponse pRisposta) {
		this.doGet(pRichiesta, pRisposta);
	}

}
