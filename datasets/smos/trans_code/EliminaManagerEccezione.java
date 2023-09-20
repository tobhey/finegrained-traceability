package smos.exception;

import java.lang.Exception;

/**
 * This class represents the exception thrown when a user attempts to delete the
 * only Manager user in the database.
 */
public class DeleteManagerException extends Exception {

	private static final long serialVersionUID = -6441256751177339494L;

	/**
	 * Throws the exception without an associated error message.
	 * 
	 */
	public DeleteManagerException() {
		super("Unable to delete the user, the selected user is the only Manager in the database! Create a new Manager and try again!");
	}

	/**
	 * Throws the exception with an associated error message.
	 *
	 * @param pMessage The error message that should be associated to the exception.
	 */
	public DeleteManagerException(String pMessage) {
		super(pMessage);
	}

}
