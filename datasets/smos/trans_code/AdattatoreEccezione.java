/*
 * AdaptorException
 *
 */

package smos.storage.connectionManagement.exception;

import java.rmi.RemoteException;

/**
 * Raised when a problem occurs while executing code in ensj.
 */
public class AdapterException extends RemoteException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public AdapterException() {
		super();
	}

	/**
	 * @param pMessage
	 */
	public AdapterException(String pMessage) {
		super(pMessage + buildLabel());
	}

	/**
	 * @param pMessage
	 * @param pExceptionParent
	 */
	public AdapterException(String pMessage, Exception pExceptionParent) {
		super(pMessage + buildLabel(), pExceptionParent);
	}

	/**
	 * @param pExceptionParent
	 */
	public AdapterException(Exception pExceptionParent) {
		super(buildLabel(), pExceptionParent);
	}

	private static String buildLabel() {
		return " [1]";
	}
}
