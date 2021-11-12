/*
 * AdaptorException
 *
 */

package smos.storage.connectionManagement.exception;

import java.rmi.RemoteException;

/**
 * Generato quando si verifica un problema durante l'esecuzione del codice in ensj.
 */
public class AdattatoreEccezione extends RemoteException {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public AdattatoreEccezione() {
        super();
    }

    /**
     * @param pMessaggio
     */
    public AdattatoreEccezione(String pMessaggio) {
        super(pMessaggio + buildLabel());
    }

    /**
     * @param pMessaggio
     * @param pEccezioneGenitore
     */
    public AdattatoreEccezione(String pMessaggio, Exception pEccezioneGenitore) {
        super(pMessaggio + buildLabel(), pEccezioneGenitore);
    }

    /**
     * @param pEccezioneGenitore
     */
    public AdattatoreEccezione(Exception pEccezioneGenitore) {
        super(buildLabel(), pEccezioneGenitore);
    }

    private static String buildLabel() {
        return " [1]";
    }
}
