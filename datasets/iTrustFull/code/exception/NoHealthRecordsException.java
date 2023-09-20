package edu.ncsu.csc.itrust.exception;

/**
 * This error message is thrown when checking for health risk indicators and the
 * patient has had no health records entered.  It is a separate case than an iTrustException
 * because the error handling is slightly different. 
 */
public class NoHealthRecordsException extends iTrustException {

	public static final String MESSAGE = "The patient did not have any health records recorded. "
			+ "No risks can be calculated if no records exist";

	private static final long serialVersionUID = 7082694071460355325L;

	/**
	 * The default constructor, which just passes a pre-specified message to an iTrustException.
	 */
	public NoHealthRecordsException() {
		super(MESSAGE);
	}
}
