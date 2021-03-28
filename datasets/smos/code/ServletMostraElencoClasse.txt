package smos.application.classroomManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import smos.Ambiente;
import smos.bean.Classe;
import smos.bean.Utente;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.storage.ManagerClasse;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;

public class ServletMostraElencoClasse extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8468821050771730936L;

	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doGet(HttpServletRequest pRichiesta, 
			HttpServletResponse pRisposta) {
		String gotoPage = "./persistentDataManagement/classroomManagement/showClassroomList.jsp";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		Collection<Classe> classroomList = null;
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		ManagerClasse managerClassroom= ManagerClasse.ottenereIstanza();
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
			
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
			
			//prendere l'academic year dalla session
			
			classroomList = managerClassroom.ottenereClassePerAnnoAccademico(Integer.valueOf(pRichiesta.getParameter("academicYear")));
			
					
			session.setAttribute("classroomList", classroomList);
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
