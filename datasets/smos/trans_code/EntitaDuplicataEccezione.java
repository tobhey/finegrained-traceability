package smos.exception;

import java.lang.Exception;

/**
 * This class represents the exception thrown when you try to insert an entity
 * already present in the database.
 */
public class DuplicateEntityException extends Exception {

	private static final long serialVersionUID = 4858261134352455533L;

	/**
	 * Throws the exception without an associated error message.
	 */
	public DuplicateEntityException() {
		super("Duplicate Key into the Repository!");
	}

	/**
	 * Throws the exception with an associated error message.
	 *
	 * @param pMessage The error message that should be associated to the exception.
	 */
	public DuplicateEntityException(String pMessage) {
		super(pMessage);
	}

}