package smos.exception;

import java.lang.Exception;

/**
 * This class represents the default exception thrown by the system.
 */
public class DefaultException extends Exception {

	private static final long serialVersionUID = -8985617134055655964L;

	/**
	 * Throws the exception without an associated error message.
	 */
	public DefaultException() {
		super();
	}

	/**
	 * Throws the exception with an associated error message.
	 *
	 * @param pMessage The error message that should be associated to the exception.
	 */
	public DefaultException(String pMessage) {
		super(pMessage);
	}

}