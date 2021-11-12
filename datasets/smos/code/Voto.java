package smos.bean;

import java.io.Serializable;

import smos.exception.ValoreNonValidoEccezione;

/**
 * 
 * Classe utilizzata per modellare una voto.
 * 
 * @author Luigi Colangelo 
 * @version 1.0
 * 
 *          
 */
public class Voto implements Serializable {

	/**
	 * Classe utilizzata per modellare un voto
	 * 
	 */
	private static final long serialVersionUID = 3014235634635608576L;
    private int id_voti;
    private int id_utente;
    private int insegnamento;
    private int scritto;
    private int orale;
    private int laboratorio;
    private int annoAccademico;
    private int turno;
    
    /**
     * Il costruttore della classe
     */
    public Voto(){
    	id_voti=0;
    }
    
    /**
     * Metodo che restituisce l'id del voto.
     * @return l'id del voto
     */
	public int ottenereId_voti() {
		return id_voti;
	}
	
	/**
	 * Metodo che setta l'id del voto
	 * @param pId_voti
     *             l'id da settare
	 */
	public void settareId_voti(int pId_voti) {
		this.id_voti = pId_voti;
	}
	
	/**
	 * Metodo che restituisce l'id dell'utente collegato al voto
	 * @return l'id dell'utente
	 */
	public int ottenereId_utente() {
		return id_utente;
	}
	
	/**
	 * Metodo che setta l'id dell'utente relativo al voto
	 * @param pId_utente
	 *               l'id da settare
	 */
	public void settareId_utente(int pId_utente) {
		this.id_utente = pId_utente;
	}
	
	/**
	 * metodo che restituisce il codice dell'insegnamento del voto
	 * @return il metodo dell'insegnamento
	 */
	public int ottenereInsegnamento() {
		return insegnamento;
	}
	
	/**
	 * Metodo che setta il codice dell'insegnamento relativo al voto
	 * @param pInsegnamento
	 *              il codice dell'insegnamento
	 */
	public void settareInsegnamento(int pInsegnamento) {
		this.insegnamento = pInsegnamento;
	}
	
	/**
	 * Metodo che restituisce il voto dello scritto 
	 * @return il voto nello scritto 
	 */
	public int ottenereScritto() {
		return scritto;
	}
	
	/**
	 * Metodo che setta il voto dello scritto, controllando che esso sia compreso tra 0 e 10
	 * @param pScritto
	 */
	public void settareScritto(int pScritto) throws ValoreNonValidoEccezione {
		if (pScritto < 0 || pScritto > 10)
			throw new ValoreNonValidoEccezione();
		else
		this.scritto = pScritto;
	}
	
	/**
	 *  metodo che restituisce il voto dell' orale
	 * @return il voto dell'orale
	 */
	public int ottenereOrale() {
		return orale;
	}
	
	/**
	 * Metodo che setta il voto dell'orale, controllando che esso sia compreso tra 0 e 10 
	 * @param pOrale
	 *            il voto dell'orale da settare
	 */
	public void settareOrale(int pOrale) throws ValoreNonValidoEccezione{
		if (pOrale < 0 || pOrale > 10)
			throw new ValoreNonValidoEccezione();
		else
		this.orale = pOrale;
	}
	
	/**
	 * Metodo che restituisce il voto del laboratorio
	 * @return il voto del laboratorio
	 */
	public int ottenereLaboratorio() {
		return laboratorio;
	}
	
	/**
	 * metodo che setta il voto del laboratorio, controllando che esso sia compreso tra 0 e 10
	 * @param pLaboratorio
	 *                 il voto del laboratorio da settare
	 */
	public void settareLaboratorio(int pLaboratorio)throws ValoreNonValidoEccezione {
		if (pLaboratorio < 0 || pLaboratorio > 10)
			throw new ValoreNonValidoEccezione();
		else
		this.laboratorio = pLaboratorio;
	}
	
	/**
	 * Metodo che restituisce l'anno accademico del voto
	 * @return l'anno accademico
	 */
	public int ottenereAnnoAccademico() {
		return annoAccademico;
	}
	
	/**
	 * metodo che setta l'anno accademico del voto
	 * @param pAnnoAccademico
	 */
	public void settareAnnoAccademico(int pAnnoAccademico) {
		this.annoAccademico = pAnnoAccademico;
	}
	
	/**
	 * Metodo che restituisce il quadrimestre del voto 
	 * @return il semestre del voto (0 o 1)
	 */
	public int ottenereTurno() {
		return turno;
	}
	
	/**
	 * Metodo che setta il quadrimestre del voto
	 * @param pTurno
	 *            il semestre del voto da settare
	 */
	public void settareTurno(int pTurno) {
		this.turno = pTurno;
	}
	
	public String toString(){
		return("id voto= "+id_voti+" id user= "+id_utente+" id insegnamento= "+insegnamento+" scritto= "+scritto+" orale= "+orale+" laboratorio= "+laboratorio+" anno accademico= "+annoAccademico+" quadrimestre= "+turno);
	}
    
 
}
