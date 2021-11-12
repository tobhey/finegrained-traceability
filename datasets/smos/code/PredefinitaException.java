package smos.exception;

import java.lang.Exception;

/**
  * Questa classe rappresenta l'eccezione predefinita generata dal sistema.
  */
public class PredefinitaException extends Exception {
	
	private static final long serialVersionUID = -8985617134055655964L;

	/**
	 * Genera l'eccezione senza un messagio di errore associato.
	 */
	public PredefinitaException() {
		super();
	}
	
	/**
	  * Genera l'eccezione con un messagio di errore associato.
	  *
	  * @param pMessaggio 	Il messaggio di errore che deve essere associato
	  *						all'eccezione.
	  */
	public PredefinitaException(String pMessaggio) {
		super(pMessaggio);
	}
	
	
}