package smos.application.registerManagement;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import smos.Ambiente;
import smos.bean.Classe;
import smos.bean.Nota;
import smos.bean.Utente;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.exception.CampoObbligatorioEccezione;
import smos.storage.ManagerRegistrati;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;
import smos.utility.Utility;

public class ServletInserisciNuovaNota extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6496360730201101300L;

	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doGet(HttpServletRequest pRichiesta, 
			HttpServletResponse pRisposta) {
		String gotoPage = "";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
		
		//instanziare gli oggetti qua
		
		ManagerRegistrati managerRegister = ManagerRegistrati.ottenereIstanza(); 
		
		
		try {
			if (loggedUser == null) {
				pRisposta.sendRedirect("./index.htm");
				return;
			} 
			if (!managerUser.eAmministratore(loggedUser)) {
				messaggioDiErrore =  "L'Utente collegato non ha accesso alla " +
						"funzionalita'!";
				gotoPage = "./error.jsp";
			} 
			//settare le cose da passare alla session, usare session.setAttribute(String, attribute) 
			
			
			if(pRichiesta.getParameter("insert")==null){
				Utente student =(Utente) session.getAttribute("student"); 
					//managerUser.getUserById(Integer.valueOf((String)pRichiesta.getAttribute("student")));
				session.setAttribute("student", student);
				session.setAttribute("idStudent", student.ottenereId());
				gotoPage="./registerManagement/insertNewNote.jsp";
			}else{
				
				
				Nota nNote= new Nota();
				nNote.settareAnnoAccademico(((Classe) session.getAttribute("classroom")).ottenereAnnoAccademico());
				nNote.settareDataNota(Utility.String2Date(pRichiesta.getParameter("dateNote")));
				nNote.settareIdUtente((Integer) session.getAttribute("idStudent"));
				nNote.settareInsegnante(pRichiesta.getParameter("noteTeacher"));
				nNote.settareDescrizione(pRichiesta.getParameter("noteDescription"));
				
				try{
					managerRegister.inserireNota(nNote);
					gotoPage="./showNoteList?student="+session.getAttribute("idStudent");
				}catch(CampoObbligatorioEccezione e){
					session.setAttribute("error", e.getMessage());
					gotoPage="./registerManagement/insertNewNote.jsp";
				}				
				
			}
			
			
				pRisposta.sendRedirect(gotoPage);
			return; 
			
		} catch (SQLException sqlException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + sqlException.getMessage();
			gotoPage = "./error.jsp";
			sqlException.printStackTrace();
		} catch (EntitaNonTrovataEccezione entityNotFoundException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + entityNotFoundException.getMessage();
			gotoPage = "./error.jsp";
			entityNotFoundException.printStackTrace();
		} catch (ConnessioneEccezione connectionException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
		}catch (IOException ioException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
		}catch (ValoreNonValidoEccezione invalidValueException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
		}
		
		pRichiesta.getSession().setAttribute("messaggioDiErrore", messaggioDiErrore);
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
