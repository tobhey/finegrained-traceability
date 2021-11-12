package smos.bean;

import smos.exception.ValoreNonValidoEccezione;

import java.io.Serializable;

/**
 *  Classe utilizzata per modellare un utente.
 *
 * 
 */
public class Utente implements Serializable{
	

	private static final long serialVersionUID = 7272532316912745508L;
	
	
	private int id;
	private String login;
	private String password;
	private String nomeDiBattesimo;
	private String cognome;
	private int idGeniotore;
	private String cell;
	private String eMail;
	
	/**
	 * Il costruttore della classe.
	 */
	public Utente(){
		this.id = 0 ;
	}
	
	
	/**
	 * @return Ritorna la login dell'utente.
	 */
	public String ottenereLogin() {
		return this.login;
	}
	
	/**
	 * Setta la login dell'utente.
	 * @param pLogin
	 * 			La login da settare.
	 * 
	 * @throws ValoreNonValidoEccezione 
	 */
	public void settareLogin(String pLogin) throws ValoreNonValidoEccezione {
		if(pLogin.length()<=4)
			throw new ValoreNonValidoEccezione();
		else
			this.login = pLogin;
	}
	
	/**
	 * @return Ritorna il nome dell'utente.
	 */
	public String ottenereNome() {
		return this.cognome + " " + this.nomeDiBattesimo;
	}
	
	/**
	 * @return Ritorna il nome dell'utente.
	 */
	public String ottenereNomeDiBattesimo() {
		return this.nomeDiBattesimo;
	}
	
	/**
	 * Setta il nome dell'utente.
	 * @param pNomeDiBattesimo
	 * 			Il nome da settare.
	 */
	public void settareNomeDiBattesimo(String pNomeDiBattesimo) {
		this.nomeDiBattesimo = pNomeDiBattesimo;
	}
	
	
	/**
	 * @return Ritorna la password dell'utente.
	 */
	public String ottenerePassword() {
		return this.password;
	}
	
	/**
	 * Setta la password dell'utente.
	 * @param pPassword
	 * 			La password da settare.
	 * 
	 * @throws ValoreNonValidoEccezione 
	 */
	public void settarePassword(String pPassword) throws ValoreNonValidoEccezione {
		if(pPassword.length()<=4)
			throw new ValoreNonValidoEccezione();
		else
		this.password = pPassword;
	}
	
	/**
	 * @return Ritorna il cognome dell'utente.
	 */
	public String ottenereCognome() {
		return this.cognome;
	}
	
	/**
	 * Setta il cognome dell'utente.
	 * @param pCognome
	 * 			Il cognome da settare.
	 */
	public void settareCognome(String pCognome) {
		this.cognome = pCognome;
	}
	
	/**
	 * @return Ritorna l'id dell'utente.
	 */
	public int ottenereId() {
		return this.id;
	}
	
	/**
	 * Setta l'id dell'utente.
	 * @param pId
	 * 			L'id da settare.
	 */
	public void settareId(int pId) {
		this.id = pId;
	}
	
	/**
	 * Ritorna una stringa contenente nome e cognome dell'utente.
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.ottenereNomeDiBattesimo() 
		+ " " 
		+ this.ottenereCognome();
	}
	
	/**
	 * @return the eMail
	 */
	public String ottenereEMail() {
		return this.eMail;
	}
	/**
	 * @param pMail the eMail to set
	 */
	public void settareEMail(String pMail) {
		this.eMail = pMail;
	}


	/**
	 * @return the cell
	 */
	public String ottenereCell() {
		return this.cell;
	}


	/**
	 * @param cell the cell to set
	 */
	public void settareCell(String pCell) {
		this.cell = pCell;
	}


	/**
	 * @return the idGenitore
	 */
	public int ottenereIdGenitore() {
		return this.idGeniotore;
	}


	/**
	 * @param idGeniotore the idGenitore to set
	 */
	public void settareIdGenitore(int pIdGenitore) {
		this.idGeniotore = pIdGenitore;
	}
	
}
