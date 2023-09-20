package smos.exception;

import java.lang.Exception;

/**
 * This class represents the exception thrown when it is not possible get a
 * database connection
 */
public class ConnectionException extends Exception {

	private static final long serialVersionUID = -6593436034986073011L;

	/**
	 * Throws the exception without an associated error message.
	 */
	public ConnectionException() {
		super("Unable to Connect to the DataBase!");
	}

	/**
	 * Throws the exception with an associated error message.
	 *
	 * @param pMessage The error message that should be associated to the exception.
	 */
	public ConnectionException(String pMessage) {
		super(pMessage);
	}

}