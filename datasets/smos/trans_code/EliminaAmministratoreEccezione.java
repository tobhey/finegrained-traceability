package smos.exception;

/**
 * This class represents the exception thrown when a user try to delete the only
 * Admin user in the database.
 */
public class DeleteAdministratorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2081143475624381775L;

	/**
	 * Throws the exception without an associated error message.
	 * 
	 */
	public DeleteAdministratorException() {
		super("Unable to delete the user, the selected user is the only Admin in the database! Create a new Manager and try again!");
	}

	/**
	 * Throws the exception with an associated error message.
	 *
	 * @param pMessage The error message that should be associated to the exception.
	 */
	public DeleteAdministratorException(String pMessage) {
		super(pMessage);
	}
}
