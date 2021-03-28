package smos.bean;

import java.io.Serializable;
import java.util.Date;
/**
 * Classe che modella l'assenza di uno studente
 * @author Nicola Pisanti
 * @version 1.0 
 */
public class Assenza implements Serializable {
	
	private static final long serialVersionUID = -8396513309450121449L;
	
	private int idAssenza;
	private int idUtente;
	private Date dataAssenza;
	private Integer idGiustificare;
	private int annoAccademico;
	
	public Assenza (){
		
	}
	
	/**
	 * Metodo che restituisce l'id dell'assenza
	 * @return un intero che rappresenta l'id dell'assenza
	 */
	public int ottenereIdAssenza() {
		return idAssenza;
	}
	/**
	 * Metodo che setta l'id dell'assenza
	 * @param un intero che rappresenta l'id da settare
	 */
	public void settareIdAssenza(int pIdAssenza) {
		this.idAssenza = pIdAssenza;
	}
	/**
	 * Metodo che restituisce l'id dello studente relativo all'assenza
	 * @return un intero che rappresenta l'id dello studente assente
	 */
	public int ottenereIdUtente() {
		return idUtente;
	}
	/**
	 * Metodo che setta l'id dello studente relativo all'assenza
	 * @param un intero che rappresenta l'id da settare
	 */
	public void settareIdUtente(int pIdUtente) {
		this.idUtente = pIdUtente;
	}
	/**
	 * Metodo che restituisce la data dell'assenza
	 * @return una stringa che rappresenta la data dell'assenza
	 */
	public Date ottenereDataAssenza() {
		return dataAssenza;
	}
	/**
	 * Metodo che setta la data dell'assenza
	 * @param una stringa con la data da settare
	 */
	public void settareDataAssenza(Date pDataAssenza) {
		this.dataAssenza = pDataAssenza;
	}
	/**
	 * Metodo che ritorna l'id della giustifica relativa all'assenza
	 * @return un intero che rappresenta l'id della giustifica relativa all'assenza, oppure null se l'assenza non e stata giustificata
	 */
	public Integer ottenereIdGiustificare() {
		
		return idGiustificare;
		
	}
	/**
	 * Metodo che setta l'id della giustifica relativa all'assenza
	 * @param un intero che rappresenta l'id della giustifica da settare
	 */
	public void settareIdGiustificare(Integer pIdGiustificare) {
		this.idGiustificare = pIdGiustificare;
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
