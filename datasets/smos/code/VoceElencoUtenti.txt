package smos.bean;

import java.io.Serializable;

/**
 * Classe utilizzata per modellare le informazioni principali 
 * di un utente.
 */
public class VoceElencoUtenti implements Serializable{

	private static final long serialVersionUID = 3436931564172045464L;

	private String nome;
	private String eMail;
	private int id;
	
	
	
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
	 * @return Ritorna il nome dell'utente.
	 */
	public String ottenereNome() {
		return this.nome;
	}
	
	/**
	 * Setta il nome dell'utente.
	 * @param pNome
	 * 			Il nome da settare.
	 */
	public void settareNome(String pNome) {
		this.nome = pNome;
	}
	

	/**
	 * @return the eMail
	 */
	public String ottenereEMail() {
		return this.eMail;
	}

	/**
	 * @param mail the eMail to set
	 */
	public void settareEMail(String pMail) {
		this.eMail = pMail;
	}
}
