package smos.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe che modella una nota sul registro 
 * @author Nicola Pisanti
 * @version 1.0
 */
public class Nota implements Serializable{

	private static final long serialVersionUID = 5953926210895315436L;
	
	private int idNota;
	private int idUtente;
	private Date dataNota;
	private String descrizione;
	private String insegnante;
	private int annoAccademico;
	
	
	public Nota(){
		
	}
	
	
	
	/**
	 * Metodo che restituisce l'id della nota
	 * @return un intero che rappresenta l'id della nota
	 */
	public int ottenereIdNota() {
		return idNota;
	}
	/**
	 * Metodo per settare l'id della nota
	 * @param un intero che rappresenta il nuovo valore dell'id
	 */
	public void settareIdNota(int pIdNota) {
		this.idNota = pIdNota;
	}
	/**
	 * Metodo che restituisce l'id dello studente che ha ricevuto la nota
	 * @return l'id dell'utente che ha ricevuto la nota
	 */
	public int ottenereIdUtente() {
		return idUtente;
	}
	/**
	 * Metodo per settare l'id dello studente che ha ricevuto la nota
	 * @param un intero che rappresenta il nuovo valore dell'id
	 */
	public void settareIdUtente(int pIdUtente) {
		this.idUtente = pIdUtente;
	}
	/**
	 * Metodo che restituisce una stringa che rappresenta la data in cui e stata data la nota
	 * @return una stringa che rappresenta la data della nota
	 */
	public Date ottenereDataNota() {
		return dataNota;
	}
	/**
	 * Metodo che setta una stringa che rappresenta la data in cui e stata data la nota
	 * @param la stringa che rappresenta la nuova data
	 */
	public void settareDataNota(Date pDataNota) {
		this.dataNota = pDataNota;
	}
	/**
	 * Metodo che restituisce il testo della nota 
	 * @return una stringa che rappresenta il testo della nota
	 */
	public String ottenereDescrizione() {
		return descrizione;
	}
	/**
	 * Metodo che setta la descrizione della nota
	 * @param una stringa che contiene la descrizione della nota
	 */
	public void settareDescrizione(String pDescrizione) {
		this.descrizione = pDescrizione;
	}
	/**
	 * Metodo che restituisce l'id dell'insegnante che ha dato la nota 
	 * @return un intero che rappresenta l'id dell'insegnante
	 */
	public String ottenereInsegnante() {
		return insegnante;
	}
	/**
	 * Metodo che setta l'id dell'insegnante che ha dato la nota 
	 * @param insegnante the teacher to set
	 */
	public void settareInsegnante(String pInsegnante) {
		this.insegnante = pInsegnante;
	}
	/**
	 * Metodo che restituisce l'anno accademico in corso
	 * @return un intero che indica l'anno di inizio delle lezioni 
	 */
	public int ottenereAnnoAccademico() {
		return annoAccademico;
	}
	/**
	 * Medoto che setta l'anno accademico in corso durante l'assegnazione della nota
	 * @param un intero che indica l'anno di inizio delle lezioni da inserire
	 */
	public void settareAnnoAccademico(int annoAccademico) {
		this.annoAccademico = annoAccademico;
	}

	
	
	
	
	
}
