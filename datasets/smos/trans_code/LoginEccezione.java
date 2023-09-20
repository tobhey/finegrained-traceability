package smos.exception;

import java.lang.Exception;

/**
 * This class represents the exception thrown when a user * enters an incorrect
 * password during authentication
 */
public class LoginException extends Exception {

	private static final long serialVersionUID = -1213284697567763493L;

	/**
	 * Throws the exception without an associated error message.
	 */
	public LoginException() {
		super("Login or Password Incorrect or Invalid Session!");
	}

	/**
	 * Throws the exception with an associated error message.
	 *
	 * @param pMessage The error message that should be associated to the exception.
	 */
	public LoginException(String pMessage) {
		super(pMessage);
	}

}