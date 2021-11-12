package smos.bean;

import java.io.Serializable;

/**
 * Classe che modella una linea del registro 
 * @author Nicola Pisanti
 * @version 1.0 
 */
public class RegistratiLinea implements Serializable {

	
	private static final long serialVersionUID = -6010085925185873838L;
	
	private VoceElencoUtenti studente;
	private Assenza assenza;
	private Ritardo ritardo;
	
	public RegistratiLinea(){
		assenza=null;
		ritardo=null;
		
	}
	
	/**
	 * Metodo che restituisce lo studente di questa riga del registro
	 * @return un oggetto di tipo User che rappresenta lo studente
	 */
	public VoceElencoUtenti ottenereStudente() {
		return studente;
	}
	/**
	 * Metodo che setta lo studente di questa riga del registro
	 * @param un oggetto di tipo User che rappresenta lo studente da inserire
	 */
	public void settareStudente(VoceElencoUtenti studente) {
		this.studente = studente;
	}
	/**
	 * Metodo che restituisce l'assenza dello studente di questa riga del registro
	 * @return un oggetto di tipo Absence che rappresenta l'assenza, oppure null se lo studente era presente
	 */
	public Assenza ottenereAssenza() {
		return assenza;
	}
	/**
	 * Metodo che setta l'assenza dello studente di questa riga del registro 
	 * @param un oggetto di tipo Absence da settare
	 */
	public void settareAssenza(Assenza assenza) {
		this.assenza = assenza;
	}
	/**
	 * Metodo che restituisce il ritardo dello studente di questa riga del registro 
	 * @return un oggetto di tipo Delay che rappresenta il ritardo, oppure null se lo studente era arrivato in orario o era assente
	 */
	public Ritardo ottenereRitardo() {
		return ritardo;
	}
	/**
	 * Metodo che setta il ritardo dello studente di questa riga del registro 
	 * @param un oggetto di tipo Delay da settare
	 */
	public void settareRitardo(Ritardo ritardo) {
		this.ritardo = ritardo;
	}

}
