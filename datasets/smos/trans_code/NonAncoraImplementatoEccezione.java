/*
 * NotYetImplementedException
 *
 */

package smos.storage.connectionManagement.exception;

/**
 * This exception is thrown as a warning by some part of the code that
 * has not yet been implemented, but will be in the future.
 */
public class NotYetImplementedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public NotYetImplementedException() {
		super();
	}

	/**
	 * @param pMessage
	 */
	public NotYetImplementedException(String pMessage) {
		super(pMessage);
	}

}
