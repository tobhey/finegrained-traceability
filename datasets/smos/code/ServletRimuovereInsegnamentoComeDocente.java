package smos.application.userManagement;

import smos.Ambiente;
import smos.bean.Insegnamento;
import smos.bean.Utente;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.storage.ManagerClasse;
import smos.storage.ManagerInsegnamento;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet utilizzata per rimuovere Insegnamenti ad un docente
 * 
 * 
 * @author Giulio D'Amora
 * @version 1.0
 * 
 *          
 */
public class ServletRimuovereInsegnamentoComeDocente extends HttpServlet {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8007609698841510837L;

	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doGet(HttpServletRequest pRichiesta,
			HttpServletResponse pRisposta) {
		String gotoPage = "./persistentDataManagement/userManagement/teacherTeachings.jsp";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();

		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		ManagerInsegnamento managerTeaching = ManagerInsegnamento.ottenereIstanza();
		ManagerClasse managerClass = ManagerClasse.ottenereIstanza();
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
		try {
			if (loggedUser == null) {
				pRisposta.sendRedirect("./index.htm");
				return;
			}
			if (!managerUser.eAmministratore(loggedUser)) {
				messaggioDiErrore = "L'Utente collegato non ha accesso alla "
					+ "funzionalita'!";
				gotoPage = "./error.jsp";
			}
			// //Mi servono i 3 Id user class teachings(che non so quanti ne sono)
			int idTeacher = (int) ((Utente) session.getAttribute("user")).ottenereId();
			Utente teacher = managerUser.ottenereUtentePerId(idTeacher); 
			int idClass = Integer.valueOf(pRichiesta.getParameter("classId"));
			String[] idTeachingList = pRichiesta.getParameterValues("unselectedTeachings");
			int nTeaching =idTeachingList.length;
			int temp;
			//Collection<Teaching> listSelcected = new Vector<Teaching>();
			if(idTeachingList==null)
				gotoPage = "./error.jsp";
			else{
				for(int i=0;i<nTeaching;i++){
					temp = Integer.valueOf(idTeachingList[i]);
					if(managerUser.avereInsegnamento(teacher,managerTeaching.ottenereInsegnamentoPerId(temp),managerClass.ottenereClassePerID(idClass))){
						managerUser.eliminareInsegnanteInClasseInsegnamento(teacher,idClass,temp);
						//listSelcected.add(managerTeaching.getTeachingById(temp));
					}
				}
			}
			//session.setAttribute("teachingListTeacher", listSelcected);
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
		} catch (ValoreNonValidoEccezione e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pRichiesta.getSession().setAttribute("messaggioDiErrore", messaggioDiErrore);
		try {
			pRisposta.sendRedirect(gotoPage);
		} catch (IOException ioException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
			+ ioException.getMessage();
			gotoPage = "./error6.jsp";
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
