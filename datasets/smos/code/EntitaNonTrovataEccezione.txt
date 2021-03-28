package smos.exception;

import java.lang.Exception;

/**
  * Questa classe rappresenta l'eccezione generata quando un'entita'
  * non viene trovata nel database.
  */
public class EntitaNonTrovataEccezione extends Exception {
	
	private static final long serialVersionUID = -1236105333523133721L;

	/**
	 * Genera l'eccezione senza un messagio di errore associato.
	 */
	public EntitaNonTrovataEccezione() {
		super("Object Not Found in Repository!");
	}
	
	/**
	  * Genera l'eccezione con un messagio di errore associato.
	  *
	  * @param pMessaggio 	Il messaggio di errore che deve essere associato
	  *						all'eccezione.
	  */
	public EntitaNonTrovataEccezione(String pMessaggio) {
		super(pMessaggio);
	}
	
	
}