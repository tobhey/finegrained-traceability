package smos.exception;

import java.lang.Exception;

/**
 * This class represents the exception thrown when a user attempts to perform an
 * operation for which he does not have permission.
 */
public class AuthorizationException extends Exception {

	private static final long serialVersionUID = 1881009447251825664L;

	/**
	 * Throws the exception without an associated error message.
	 */
	public AuthorizationException() {
		super("Permission Denied!");
	}

	/**
	 * Throws the exception with an associated error message.
	 *
	 * @param pMessage The error message to be associated with to the exception.
	 */
	public AuthorizationException(String pMessage) {
		super(pMessage);
	}

}