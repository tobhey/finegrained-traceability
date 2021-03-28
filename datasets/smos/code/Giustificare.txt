package smos.bean;

import java.io.Serializable;

import java.util.Date;
/**
 * Classe che modella una giustifica per un assenza
 * @author Nicola Pisanti
 * @version 1.0
 * 
 */
public class Giustificare implements Serializable {

	private static final long serialVersionUID = -4726381877687167661L;

	private int idGiustificare;
	private int idUtente;
	private Date dataGiustificare;
	private int annoAccademico;
	
	/**
	 * Metodo che restituisce l'id della giustifica
	 * @return un intero che rappresenta l'id della giustifica
	 */
	public int ottenereIdGiustificare() {
		return idGiustificare;
	}
	/**
	 * Metodo che setta l'id della giustifica
	 * @param un intero che rappresenta l'id da settare
	 */
	public void settareIdGiustificare(int pIdGiustificare) {
		this.idGiustificare = pIdGiustificare;
	}
	/**
	 * Metodo restituisce l'id dello studente relativo alla giustifica
	 * @return un intero che rappresenta l'id dello studente 
	 */
	public int ottenereIdUtente() {
		return idUtente;
	}
	/**
	 * Metodo che setta l'id dello studente relativo alla giustifica
	 * @param un intero che rappresenta l'id da settare
	 */
	public void settareIdUtente(int pIdUtente) {
		this.idUtente = pIdUtente;
	}
	/**
	 * Metodo che restituisce la data alla quale e stata giustificata l'assenza
	 * @return una stringa che rappresenta la data giustificata
	 */
	public Date ottenereDataGiustificare() {
		return dataGiustificare;
	}
	/**
	 * Metodo che setta la data alla quale e stata giustificata l'assenza
	 * @param una stringa che rappresenta la data da settare
	 */
	public void settareDataGiustificare(Date pDataGiustificare) {
		this.dataGiustificare = pDataGiustificare;
	}
	/**
	 * Metodo che restituisce l'anno accademico relativo alla giustifica
	 * @return un intero che rappresenta l'anno in cui e iniziato l'anno accademico 
	 */
	public int ottenereAnnoAccademico() {
		return annoAccademico;
	}
	/**
	 * Metodo che setta l'anno accademico relativo alla giustifica
	 * @param un intero che rappresenta l'anno accademico da settare
	 */
	public void settareAnnoAccademico(int pAnnoAccademico) {
		this.annoAccademico = pAnnoAccademico;
	}
	
	
	
}
