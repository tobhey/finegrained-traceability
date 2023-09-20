package edu.ncsu.csc.itrust.exception;

import java.sql.SQLException;

/**
 * The reasoning behind this wrapper exception is security. When an SQL Exception gets thrown all the way back
 * to the JSP, we begin to reveal details about our database (even knowing that it's MySQL is bad!) So, we
 * make a wrapper exception with a vague description, but we also keep track of the SQL Exception for
 * debugging and testing purposes.
 * 
 * @author Andy
 * 
 */
public class DBException extends iTrustException {
	private static final long serialVersionUID = -6554118510590118376L;
	private SQLException sqlException = null;

	public DBException(SQLException e) {
		super("A database exception has occurred. Please see the log in the console for stacktrace");
		this.sqlException = e;
	}

	/**
	 * @return The SQL Exception that was responsible for this error.
	 */
	public SQLException getSQLException() {
		return sqlException;
	}

	@Override
	public String getExtendedMessage() {
		if (sqlException != null)
			return sqlException.getMessage();
		else
			return super.getExtendedMessage();
	}
}
