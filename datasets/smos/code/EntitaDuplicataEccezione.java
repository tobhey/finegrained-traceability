package smos.exception;

import java.lang.Exception;

/**
  * Questa classe rappresenta l'eccezione generata quando si 
  * tenta di inserire un'entita gia presente nel database.
  */
public class EntitaDuplicataEccezione extends Exception {
	
	private static final long serialVersionUID = 4858261134352455533L;

	/**
	 * Genera l'eccezione senza un messagio di errore associato.
	 */
	public EntitaDuplicataEccezione() {
		super("Duplicate Key into the Repository!");
	}
	
	/**
	  * Genera l'eccezione con un messagio di errore associato.
	  *
	  * @param pMessaggio 	Il messaggio di errore che deve essere associato
	  *						all'eccezione.
	  */
	public EntitaDuplicataEccezione (String pMessaggio) {
		super(pMessaggio);
	}
	
	
}