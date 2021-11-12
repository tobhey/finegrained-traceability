package smos.application.userManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import smos.Ambiente;
import smos.bean.Classe;
import smos.bean.Insegnamento;
import smos.bean.Utente;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.exception.CampoObbligatorioEccezione;
import smos.storage.ManagerClasse;
import smos.storage.ManagerInsegnamento;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;
import smos.utility.Utility;

public class ServletMostraModuloDiInsegnamentoUtente extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2305151029867525356L;
	
	
	protected void doGet(HttpServletRequest pRichiesta, 
			HttpServletResponse pRisposta) {
		String gotoPage = "./persistentDataManagement/userManagement/showTeacherDetails.jsp";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		
		Utente user = null;
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		ManagerClasse managerClassroom = ManagerClasse.ottenereIstanza();
		
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
		
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
			user = (Utente) session.getAttribute("user");
			
			if(!managerUser.eInsegnante(user)){
				messaggioDiErrore =  "L'Utente non e un docente";
				gotoPage = "./error.jsp";
				
			}
			//int idTeacher= user.getId();
			
			Collection<Classe> classList = managerClassroom.ottenereClassePerInsegnante(user);
			
			/*
			Iterator<Classroom> iteClass = classList.iterator();
			Classroom tmp = null;
			while(iteClass.hasNext()){
				tmp=iteClass.next();
				if(tmp.ottenereAnnoAccademico()!= an){
					classList.remove(tmp);
				}
			}*/
			//@SuppressWarnings("unused")
			//Collection<Teaching> teachingListByClassroom=null;
			//Collection<Classroom,Teaching> list= new Vector <Classroom,Teaching>();
			
			session.setAttribute("classroomList", classList);
			
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
		} catch (ValoreNonValidoEccezione invalidValueException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
		} catch (CampoObbligatorioEccezione mandatoryFieldException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + mandatoryFieldException.getMessage();
			gotoPage = "./error.jsp";
			mandatoryFieldException.printStackTrace();
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
