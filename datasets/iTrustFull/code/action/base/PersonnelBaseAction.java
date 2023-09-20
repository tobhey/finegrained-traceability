package edu.ncsu.csc.itrust.action.base;

import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * An abstract class for dealing with actions that require an associated personnel. The concrete functionality
 * provided by this class allows for asserting the correctness and existence of personnel' MIDs.
 * 
 * Use this class whenever your JSP requires a personnel ID when it loads (e.g. editPatient.jsp). The patient
 * string is passed to the constructor of this class and is checked for both format and existence. If the
 * patient ID is wrong in any way, an exception is thrown, resulting in the user getting kicked out to the
 * home page.
 * 
 * Subclasses need not rewrite this functionality, and they are not held to any strict contract to extend this
 * class.
 * 
 * Very similar to {@link PatientBaseAction}
 */
public class PersonnelBaseAction {

	/**
	 * The database access object factory to associate this with a runtime context.
	 */
	private DAOFactory factory;

	/**
	 * Stores the MID of the personnel associated with this action.
	 */
	protected long pid;

	/**
	 * The default constructor.
	 * 
	 * @param factory
	 *            A factory to create a database access object.
	 * @param pidString
	 *            The personnel's ID to associate with this action.
	 * @throws iTrustException
	 *             If the personnel's ID is incorrect or there is a DB problem.
	 */
	public PersonnelBaseAction(DAOFactory factory, String pidString) throws iTrustException {
		this.factory = factory;
		this.pid = checkPersonnelID(pidString);
	}

	/**
	 * Asserts whether the input is a valid, existing personnel's MID.
	 * 
	 * @param input
	 *            The presumed MID
	 * @return The existing personnel's ID as a long.
	 * @throws iTrustException
	 *             If the personnel does not exist or there is a DB Problem.
	 */
	private long checkPersonnelID(String input) throws iTrustException {
		try {
			long pid = Long.valueOf(input);
			if (factory.getPersonnelDAO().checkPersonnelExists(pid))
				return pid;
			else
				throw new iTrustException("Personnel does not exist");
		} catch (NumberFormatException e) {
			throw new iTrustException("Personnel ID is not a number: " + e.getMessage());
		}
	}

	/**
	 * Retrieves the identifier of the patient as a long.
	 * 
	 * @return The patient's MID.
	 */
	public long getPid() {
		return pid;
	}
}
