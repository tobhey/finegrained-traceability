package smos.application.userManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

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
import smos.storage.connectionManagement.exception.ConnessioneEccezione;
/**
 * Servlet  che modifica il record dello studente con l'id del padre.
 * 
 * @author Napolitano Vincenzo.
 * 
 */
public class ServletAssegnaStudenteGenitore extends HttpServlet {

	
	private static final long serialVersionUID = -4507225018030147979L;
	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 */
	protected void doGet(HttpServletRequest pRichiesta,
			HttpServletResponse pRisposta) {
		String gotoPage = "showUserList";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		

		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();

		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
		
		// Verifica che l'utente abbia effettuato il login
		try {
			if (loggedUser == null) {
				pRisposta.sendRedirect("./index.htm");
				return;
			}
			if ((!managerUser.eAmministratore(loggedUser))) {
				messaggioDiErrore =  "L'Utente collegato non ha accesso alla " +
						"funzionalita'!";
				gotoPage = "./error.jsp";
			}
			
			
			Utente user = (Utente) session.getAttribute("user");
			int idGenitore=user.ottenereId();
			
			Collection<VoceElencoUtenti> students = new Vector<VoceElencoUtenti>();
			Iterator<VoceElencoUtenti> itStu = null;
			
			students = managerUser.ottenereStudenti();
			
			
			String[] selectedStudent = pRichiesta.getParameterValues("selectedStudent");
			String[] unselectedStudent = pRichiesta.getParameterValues("unselectedStudent");
			
			if (selectedStudent != null) {
				int selectedlength = selectedStudent.length;
				VoceElencoUtenti tmp = null;
				Utente stu=null;
				int idStudent=0;
				for (int i = 0; i < selectedlength; i++) {
					itStu = students.iterator();
					String dd = selectedStudent[i];
					idStudent=Integer.parseInt(dd);
					while(itStu.hasNext()){
						 tmp = (VoceElencoUtenti)itStu.next();
						if(tmp.ottenereId()==idStudent){	
							stu=managerUser.ottenereUtentePerId(tmp.ottenereId());//recupero userStudente	
							managerUser.assegnareGenitore(stu, idGenitore);
						}
					}	
				}
				
			} else {
				gotoPage="./error.jsp";
			}
			if (unselectedStudent != null) {
				itStu=null;
				int unselectedlength = unselectedStudent.length;
				VoceElencoUtenti tmp = null;
				Utente stu=null;
				int idStudent=0;
				for (int i = 0; i < unselectedlength; i++) {
					itStu = students.iterator();
					String dd = unselectedStudent[i];
					idStudent=Integer.parseInt(dd);
					while(itStu.hasNext()){
						 tmp = (VoceElencoUtenti)itStu.next();
						if(tmp.ottenereId()==idStudent){	
							stu=managerUser.ottenereUtentePerId(tmp.ottenereId());//recupero userStudente	
							managerUser.eliminareGenitore(stu);
						}
					}
				}
			}
			
			
			session.setAttribute("user", user);
		} catch (NumberFormatException numberFormatException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + numberFormatException.getMessage();
			gotoPage = "./error.jsp";
			numberFormatException.printStackTrace();
		} catch (EntitaNonTrovataEccezione entityNotFoundException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + entityNotFoundException.getMessage();
			gotoPage = "./error.jsp";
			entityNotFoundException.printStackTrace();
		} catch (SQLException SQLException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + SQLException.getMessage();
			gotoPage = "./error.jsp";
			SQLException.printStackTrace();
		} catch (ConnessioneEccezione connectionException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
		} catch (ValoreNonValidoEccezione invalidValueException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
		}  catch (IOException ioException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE + ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
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
