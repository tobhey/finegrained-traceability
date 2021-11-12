/*
 * NotYetImplementedException
 *
 */

package smos.storage.connectionManagement.exception;

/**
 * Questa eccezione viene lanciata come avvertimento da una parte del codice che non è stata ancora implementata, ma lo sarà in futuro.
 */
public class NonAncoraImplementatoEccezione extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public NonAncoraImplementatoEccezione() {
        super();
    }

    /**
     * @param pMessaggio
     */
    public NonAncoraImplementatoEccezione(String pMessaggio) {
        super(pMessaggio);
    }

}
