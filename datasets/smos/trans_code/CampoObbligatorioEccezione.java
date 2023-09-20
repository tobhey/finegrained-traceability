package smos.exception;

import java.lang.Exception;

/**
 * This class represents the exception thrown when trying to enter an entity
 * without specifying a required field
 */
public class FieldRequiredException extends Exception {

	private static final long serialVersionUID = -4818814194670133466L;

	/**
	 * Throws the exception without an associated error message.
	 */
	public FieldRequiredException() {
		super("Mandatory Field Missing!");
	}

	/**
	 * Throws the exception with an associated error message.
	 *
	 * @param pMessage The error message that should be associated to the exception.
	 */
	public FieldRequiredException(String pMessage) {
		super(pMessage);
	}

}