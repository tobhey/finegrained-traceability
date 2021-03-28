package smos.bean;

import java.io.Serializable;
import java.util.Date;
/**
 * Classe che modella un entrata in ritardo di uno studente
 * @author Nicola Pisanti
 * @version 1.0 
 */
public class Ritardo implements Serializable {

	
	private static final long serialVersionUID = 78680884161960621L;

	private int idRitardo;
	private int idUtente;
	private Date dataRitardo;
	private String TempoRitardo;
	private int annoAccademico;
	
	
	/**
	 * Metodo che restituisce l'id del Ritardo 
	 * @return un intero che rappresenta l'id del ritardo 
	 */
	public int ottenereIdRitardo() {
		return idRitardo;
	}
	/**
	 * Metodo che setta l'id del Ritardo 
	 * @param intero che rappresenta l'id da settare
	 */
	public void settareIdRitardo(int pIdRitardo) {
		this.idRitardo = pIdRitardo;
	}
	/**
	 * Metodo che restituisce l'id dello studente ritardatario 
	 * @return un intero che rappresenta l'id dello studente
	 */
	public int ottenereIdUtente() {
		return idUtente;
	}
	/**
	 * Metodo che setta l'id dello studente relativo al ritardo 
	 * @param un intero che rappresenta l'id da settare
	 */
	public void settareIdUtente(int pIdUtente) {
		this.idUtente = pIdUtente;
	}
	/**
	 * Metodo che restituisce la data del ritardo 
	 * @return una stringa che rappresenta la data del ritardo 
	 */
	public Date ottenereDataRitardo() {
		return dataRitardo;
	}
	/**
	 * Metodo che setta la data del ritardo
	 * @param una stringa che rappresenta la data del ritardo
	 */
	public void settareDataRitardo(Date pDataRitardo) {
		this.dataRitardo = pDataRitardo;
	}
	/**
	 * Metodo che restituisce l'ora d'entrata dello studente
	 * @return una stringa che rappresenta l'ora di entrata dello studente ritardatario
	 */
	public String ottenereTempoRitardo() {
		if (this.TempoRitardo.length() > 0){
			return TempoRitardo.substring(0, 5);
		} else {
			return this.TempoRitardo;
		}
	}
	/**
	 * Metodo che setta l'ora di entrata dello studente 
	 * @param una stringa che rappresenta l'ora di entrata da settare
	 */
	public void settareTempoRitardo(String pTempoRitardo) {
		this.TempoRitardo = pTempoRitardo;
	}
	/**
	 * Metodo che restituisce l'anno accademico relativo all'assenza
	 * @return un intero che rappresenta l'anno in cui e iniziato l'anno accademico 
	 */
	public int ottenereAnnoAccademico() {
		return annoAccademico;
	}
	/**
	 * Metodo che setta l'anno accademico relativo all'assenza
	 * @param un intero che rappresenta l'anno accademico da settare
	 */
	public void settareAnnoAccademico(int pAnnoAccademico) {
		this.annoAccademico = pAnnoAccademico;
	}
}
