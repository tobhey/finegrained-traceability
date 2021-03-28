package smos.exception;

import java.lang.Exception;

/**
  * Questa classe rappresenta l'eccezione generata quando non e possibile
  * ottenere una connessione al database
  */
public class ConnessioneEccezione extends Exception {
	
	private static final long serialVersionUID = -6593436034986073011L;

	/**
	 * Genera l'eccezione senza un messagio di errore associato.
	 */
	public ConnessioneEccezione() {
		super("Unable to Connect to the DataBase!");
	}
	
	/**
	  * Genera l'eccezione con un messagio di errore associato.
	  *
	  * @param pMessaggio 	Il messaggio di errore che deve essere associato
	  *						all'eccezione.
	  */
	public ConnessioneEccezione(String pMessaggio) {
		super(pMessaggio);
	}
	
	
}