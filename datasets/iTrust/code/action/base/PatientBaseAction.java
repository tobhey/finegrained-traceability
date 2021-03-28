package edu.ncsu.csc.itrust.action.base;

import edu.ncsu.csc.itrust.HtmlEncoder;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * An abstract class for dealing with actions that require an associated patient. The concrete functionality
 * provided by this class allows for asserting the correctness and existence of patients' MIDs.
 * 
 * Use this class whenever your JSP requires a patient ID when it loads (e.g. editPatient.jsp). The patient
 * string is passed to the constructor of this class and is checked for both format and existence. If the
 * patient ID is wrong in any way, an exception is thrown, resulting in the user getting kicked out to the
 * home page.
 * 
 * Very similar to {@link PersonnelBaseAction} and {@link OfficeVisitBaseAction}
 * 
 * Subclasses need not rewrite this functionality, and they are not held to any strict contract to extend this
 * class.
 */
public class PatientBaseAction {

	/**
	 * The database access object factory to associate this with a runtime context.
	 */
	private DAOFactory factory;

	/**
	 * Stores the MID of the patient associated with this action.
	 */
	protected long pid;

	/**
	 * The default constructor.
	 * 
	 * @param factory
	 *            A factory to create a database access object.
	 * @param pidString
	 *            The patient's ID to associate with this action.
	 * @throws iTrustException
	 *             If the patient's ID is incorrect or there is a DB problem.
	 */
	public PatientBaseAction(DAOFactory factory, String pidString) throws iTrustException {
		this.factory = factory;
		this.pid = checkPatientID(pidString);
	}

	/**
	 * Asserts whether the input is a valid, existing patient's MID.
	 * 
	 * @param input
	 *            The presumed MID
	 * @return The existing patient's ID as a long.
	 * @throws iTrustException
	 *             If the patient does not exist or there is a DB Problem.
	 */
	private long checkPatientID(String input) throws iTrustException {
		try {
			long pid = Long.valueOf(input);
			if (factory.getPatientDAO().checkPatientExists(pid))
				return pid;
			else
				throw new iTrustException("Patient does not exist");
		} catch (NumberFormatException e) {
			throw new iTrustException("Patient ID is not a number: " + HtmlEncoder.encode(input));
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
