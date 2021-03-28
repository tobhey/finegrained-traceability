package smos.exception;

import java.lang.Exception;

/**
  * Questa classe rappresenta l'eccezione generata quando si tenta
  * di inserire un'entit senza specificare un campo obbligatorio
  */
public class CampoObbligatorioEccezione extends Exception {
	
	private static final long serialVersionUID = -4818814194670133466L;

	/**
	 * Genera l'eccezione senza un messagio di errore associato.
	 */
	public CampoObbligatorioEccezione() {
		super("Mandatory Field Missing!");
	}
	
	/**
	  * Genera l'eccezione con un messagio di errore associato.
	  *
	  * @param pMessaggio 	Il messaggio di errore che deve essere associato
	  *						all'eccezione.
	  */
	public CampoObbligatorioEccezione(String pMessaggio) {
		super(pMessaggio);
	}
	
	
}