package smos.storage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

/**
	* Class which manages the Digital Register
	*
	* @author Nicola Pisanti
	* @version 1.0
	*/
public class ManagerRegister {

	private static smos.storage.ManagerRegister instance;

	public final static String TABLE_ABSENCE = "absence";
	public final static String DELAY_TABLE = "delay";
	public final static String JUSTIFY_TABLE = "excuse";
	public final static String TABLE_NOTE = "note";

	private ManagerRegister() {
		super();
	}

/**
    * Returns the only instance of the existsnte class.
      *
      * @return Returns the instance of the class.
      */
	public static synchronized smos.storage.ManagerRegister getInstance() {
		if (instance == null) {
			instance = new smos.storage.ManagerRegister();
		}
		return instance;
	}

/**
    * Check if the class given in input is in the database
*
    * @param pAbsence The class whose existence needs to be checked
* @return true if the class is in the database, false otherwise
* @throws ConnectionException
* @throws SQLException
*/
	public synchronized boolean exists(Absence pAbsence) throws ConnectionException, SQLException {

		boolean result = false;
		Connection connect = null;

		try {
			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			String sql = "SELECT * FROM " + smos.storage.ManagerRegister.TABLE_ABSENCE + " WHERE id_absence = "
					+ Utility.eNull(pAbsence.getIdAbsence());

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, sql);

			if (tRs.next()) {
				result = true;
			}

			return result;

		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Check if the class given in input is in the database
	 *
	 * @param pDelay The class whose existence is to be checked
	 * @return true if the class is in the database, false otherwise
	 * @throws ConnectionException
	 * @throws SQLException
	 */
	public synchronized boolean exists(Delay pDelay) throws ConnectionException, SQLException {

		boolean result = false;
		Connection connect = null;

		try {
			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			String sql = "SELECT * FROM " + smos.storage.ManagerRegister.TABLE_ABSENCE + " WHERE id_delay = "
					+ Utility.eNull(pDelay.getIdDelay());
			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, sql);

			if (tRs.next())
				result = true;

			return result;

		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Check if the class given in input is in the database
	 *
	 * @param pDelay The class whose existence is to be checked
	 * @return true if the class is in the database, false otherwise
	 * @throws ConnectionException
	 * @throws SQLException
	 */
	public synchronized boolean exists(Excuse pExcuse) throws ConnectionException, SQLException {

		boolean result = false;
		Connection connect = null;

		try {
			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			String sql = "SELECT * FROM " + smos.storage.ManagerRegister.JUSTIFY_TABLE + " WHERE  id_excuse = "
					+ Utility.eNull(pExcuse.getIdExcuse());

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, sql);

			if (tRs.next())
				result = true;

			return result;

		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Insert an Absence into the database
	 *
	 * @param pAbsence an Absence type object to insert into the database
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws ValueInvalidException
	 */
	public synchronized void insertAbsence(Absence pAbsence)
			throws ConnectionException, SQLException, EntityNotFoundException, ValueInvalidException {

		Connection connect = null;
		try {

			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();
			// We prepare the Sql string
			String sql = "INSERT INTO " + smos.storage.ManagerRegister.TABLE_ABSENCE
					+ " (id_user, date_absence, id_excuse, accademic_year) " + "VALUES ("
					+ Utility.eNull(pAbsence.getIdUser()) + "," + Utility.eNull(pAbsence.getDateOfAbsence()) + ","
					+ Utility.eNull(pAbsence.getIdExcuse()) + "," + Utility.eNull(pAbsence.getAcademicYear()) + ")";

			Utility.performOperation(connect, sql);

			pAbsence.setIdAbsence((Utility.getMaximumValue("id_absence", smos.storage.ManagerRegister.TABLE_ABSENCE)));

		} finally {
			// release the resources

			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Insert a Delay into the database
	 *
	 * @param pDelay a Delay type object to insert into the database
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws ValueInvalidException
	 */
	public synchronized void insertDelay(Delay pDelay)
			throws ConnectionException, SQLException, EntityNotFoundException, ValueInvalidException {

		Connection connect = null;
		try {

			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();
			// We prepare the Sql string
			String sql = "INSERT INTO " + smos.storage.ManagerRegister.DELAY_TABLE
					+ " (id_user, date_delay, time_delay, accademic_year) " + "VALUES ("
					+ Utility.eNull(pDelay.getIdUser()) + "," + Utility.eNull(pDelay.getDateDelay()) + ","
					+ Utility.eNull(pDelay.getTimeDelay()) + "," + Utility.eNull(pDelay.getAcademicYear()) + ")";

			Utility.performOperation(connect, sql);

			pDelay.setIdDelay((Utility.getMaximumValue("id_delay", smos.storage.ManagerRegister.DELAY_TABLE)));

		} finally {
			// release the resources

			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Insert a note in the database
	 *
	 * @param pNotes an object of type Notes to insert into the database
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws ValueInvalidException
	 */
	public synchronized void insertNote(Note pNote) throws FieldRequiredException, ConnectionException, SQLException,
			EntityNotFoundException, ValueInvalidException {

		Connection connect = null;
		try {
			if (pNote.getDescription() == null || pNote.getDescription().equals(""))
				throw new FieldRequiredException("Enter the text of the Note");

			if (pNote.getTeacher() == null || pNote.getTeacher().equals(""))
				throw new FieldRequiredException("Enter the teacher");

			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();
			// We prepare the Sql string
			String sql = "INSERT INTO " + smos.storage.ManagerRegister.TABLE_NOTE
					+ " (id_user, date_note, description, teacher, accademic_year) " + "VALUES ("
					+ Utility.eNull(pNote.getIdUser()) + "," + Utility.eNull(pNote.getDataNote()) + ","
					+ Utility.eNull(pNote.getDescription()) + "," + Utility.eNull(pNote.getTeacher()) + ","
					+ Utility.eNull(pNote.getAcademicYear()) + ")";

			Utility.performOperation(connect, sql);

			pNote.setIdNote((Utility.getMaximumValue("id_note", smos.storage.ManagerRegister.TABLE_NOTE)));

		} finally {
			// release the resources

			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Insert a justification in the database
	 *
	 * @param pExcuse a Justify type object to insert into the database
	 * @param pAbsence an object of type Absence representing Absence
	 * justified
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws ValueInvalidException
	 */
	public synchronized void insertExcuse(Excuse pExcuse, Absence pAbsence)
			throws ConnectionException, SQLException, EntityNotFoundException, ValueInvalidException {

		Connection connect = null;
		try {

			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();
			// We prepare the Sql string
			String sql = "INSERT INTO " + smos.storage.ManagerRegister.JUSTIFY_TABLE
					+ " (id_user, date_excuse, accademic_year) " + "VALUES (" + Utility.eNull(pExcuse.getIdUser())
					+ "," + Utility.eNull(pExcuse.getDataExcuse()) + "," + Utility.eNull(pExcuse.getAcademicYear())
					+ ")";

			Utility.performOperation(connect, sql);

			pExcuse.setIdExcuse((Utility.getMaximumValue("id_excuse", smos.storage.ManagerRegister.JUSTIFY_TABLE)));

			pAbsence.setIdExcuse(pExcuse.getIdExcuse());
			this.updateAbsence(pAbsence);

		} finally {
			// release the resources

			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Delete an Absence from the database
	 *
	 * @param pAbsence the Absence to be deleted
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws ValueInvalidException
	 */
	public synchronized void removeAbsence(Absence pAbsence) throws ConnectionException, SQLException,
			EntityNotFoundException, FieldRequiredException, ValueInvalidException {
		Connection connect = null;

		try {
			connect = DBConnection.getConnection();
			// We prepare the SQL string
			String sql = "DELETE FROM " + smos.storage.ManagerRegister.TABLE_ABSENCE + " WHERE id_absence = "
					+ Utility.eNull(pAbsence.getIdAbsence());

			Utility.performOperation(connect, sql);

			if (!(pAbsence.getIdExcuse() == null)) {
				removeExcuse(pAbsence.getIdExcuse());
			}
		} finally {
			// release the resources
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Delete a Delay from the database
	 *
	 * @param pDelay the Delay to be canceled
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws ValueInvalidException
	 */
	public synchronized void removeDelay(Delay pDelay) throws ConnectionException, SQLException,
			EntityNotFoundException, FieldRequiredException, ValueInvalidException {
		Connection connect = null;

		try {
			connect = DBConnection.getConnection();
			// We prepare the SQL string
			String sql = "DELETE FROM " + smos.storage.ManagerRegister.DELAY_TABLE + " WHERE id_delay = "
					+ Utility.eNull(pDelay.getIdDelay());

			Utility.performOperation(connect, sql);
		} finally {
			// release the resources
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Delete a Note from the database
	 *
	 * @param pNote the Note to be deleted
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws ValueInvalidException
	 */
	public synchronized void removeNote(Note pNote)
			throws ConnectionException, SQLException, EntityNotFoundException, ValueInvalidException {
		Connection connect = null;

		try {
			connect = DBConnection.getConnection();
			// We prepare the SQL string
			String sql = "DELETE FROM " + smos.storage.ManagerRegister.TABLE_NOTE + " WHERE id_note = "
					+ Utility.eNull(pNote.getIdNote());

			Utility.performOperation(connect, sql);
		} finally {
			// release the resources
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Delete a Excuse from the database
	 *
	 * @param pIDExcuse the ID of the Excuse to be deleted
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws ValueInvalidException
	 */
	public synchronized void removeExcuse(int pIDExcuse) throws ConnectionException, SQLException,
			EntityNotFoundException, FieldRequiredException, ValueInvalidException {
		Connection connect = null;

		try {
			connect = DBConnection.getConnection();
			// We prepare the SQL string
			String sql = "DELETE FROM " + smos.storage.ManagerRegister.JUSTIFY_TABLE + " WHERE id_excuse = "
					+ Utility.eNull(pIDExcuse);

			Utility.performOperation(connect, sql);

			try {
				Absence temp = getAbsencePerIdExcuse(pIDExcuse);
				temp.setIdExcuse(0);
				updateAbsence(temp);
			} catch (Exception e) {
				// and normal if an exception is thrown
				// since it may be that we are deleting a excuse
				// whose Absence we just deleted
			}

		} finally {
			// release the resources
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Update the statistics of an Absence
	 *
	 * @param pAbsence L'Absence with updated statistics (but identical ID)
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws FieldRequiredException
	 */
	public synchronized void updateAbsence(Absence pAbsence)
			throws ConnectionException, SQLException, EntityNotFoundException {
		Connection connect = null;

		try {

			// We prepare the SQL string
			String sql = "UPDATE " + smos.storage.ManagerRegister.TABLE_ABSENCE + " SET" + " id_user = "
					+ Utility.eNull(pAbsence.getIdUser()) + ", date_absence = "
					+ Utility.eNull(pAbsence.getDateOfAbsence()) + ", id_excuse = "
					+ Utility.eNull(pAbsence.getIdExcuse()) + ", accademic_year = "
					+ Utility.eNull(pAbsence.getAcademicYear()) + " WHERE id_absence = "
					+ Utility.eNull(pAbsence.getIdAbsence());

			// make a new connection and send the query
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			Utility.performOperation(connect, sql);
		} finally {
			// release the resources
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Update the statistics of a delay
	 *
	 * @param pDelay Delay with updated statistics (but identical ID)
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws FieldRequiredException
	 */
	public synchronized void updateDelay(Delay pDelay)
			throws ConnectionException, SQLException, EntityNotFoundException, FieldRequiredException {
		Connection connect = null;

		try {

			// We prepare the SQL string
			String sql = "UPDATE " + smos.storage.ManagerRegister.DELAY_TABLE + " SET" + " id_user = "
					+ Utility.eNull(pDelay.getIdUser()) + ", date_delay = " + Utility.eNull(pDelay.getDateDelay())
					+ ", time_delay = " + Utility.eNull(pDelay.getTimeDelay()) + ", accademic_year = "
					+ Utility.eNull(pDelay.getAcademicYear()) + " WHERE id_delay = "
					+ Utility.eNull(pDelay.getIdDelay());

			// make a new connection and send the query
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			Utility.performOperation(connect, sql);
		} finally {
			// release the resources
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Method that updates the statistics of a Note
	 *
	 * @param pNote an object of type Notes with updated statistics but id
	 * identical
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws FieldRequiredException
	 */
	public synchronized void updateNote(Note pNote)
			throws ConnectionException, SQLException, EntityNotFoundException, FieldRequiredException {
		Connection connect = null;

		try {
			if (pNote.getDescription() == null || pNote.getDescription().equals(""))
				throw new FieldRequiredException("Enter the text of the Note");

			if (pNote.getTeacher() == null || pNote.getTeacher().equals(""))
				throw new FieldRequiredException("Enter the teacher");
			// We prepare the SQL string
			String sql = "UPDATE " + smos.storage.ManagerRegister.TABLE_NOTE + " SET" + " id_user = "
					+ Utility.eNull(pNote.getIdUser()) + ", date_note = " + Utility.eNull(pNote.getDataNote())
					+ ", description = " + Utility.eNull(pNote.getDescription()) + ", teacher = "
					+ Utility.eNull(pNote.getTeacher()) + ", accademic_year = "
					+ Utility.eNull(pNote.getAcademicYear()) + " WHERE id_note = " + Utility.eNull(pNote.getIdNote());

			// make a new connection and send the query
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			Utility.performOperation(connect, sql);
		} finally {
			// release the resources
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Updates the statistics to a excuse
	 *
	 * @param pExcuse excuse it with updated statistics (but identical ID)
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws FieldRequiredException
	 */
	public synchronized void updateExcuse(Excuse pExcuse)
			throws ConnectionException, SQLException, EntityNotFoundException, FieldRequiredException {
		Connection connect = null;

		try {

			// We prepare the SQL string
			String sql = "UPDATE " + smos.storage.ManagerRegister.JUSTIFY_TABLE + " SET" + " id_user = "
					+ Utility.eNull(pExcuse.getIdUser()) + ", date_excuse = " + Utility.eNull(pExcuse.getDataExcuse())
					+ ", accademic_year = " + Utility.eNull(pExcuse.getAcademicYear()) + " WHERE id_excuse = "
					+ Utility.eNull(pExcuse.getIdExcuse());

			// make a new connection and send the query
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			Utility.performOperation(connect, sql);
		} finally {
			// release the resources
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Method that returns a Note given the id of the note itself
	 *
	 * @param pIDExcuse an integer representing the Note id
	 * @return an object of type Note that represents the Note
	 * @throws ValueInvalidException
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 */
	public synchronized Note getNotePerId(int pIDNote)
			throws ValueInvalidException, EntityNotFoundException, ConnectionException, SQLException {
		Note result = null;
		Connection connect = null;
		try {
			// If the ID was not provided, we return an error code
			if (pIDNote <= 0)
				throw new EntityNotFoundException("Impossibile trovare la Note");

			/*
			 * We prepare the SQL string to retrieve the information corresponding to the
			 * class of the passed id id
			 */
			String tSql = "SELECT * FROM " + smos.storage.ManagerRegister.TABLE_NOTE + " WHERE id_note = "
					+ Utility.eNull(pIDNote);

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			if (tRs.next())
				result = this.loadNoteFromRs(tRs);
			else
				throw new EntityNotFoundException("Impossibile trovare la Note!");

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Method that returns an Absence given the id of the justification associated with it
	 * Absence
	 *
	 * @param pIDExcuse an integer representing the id of the excuse
	 * @return an object of type Absence representing justified Absence
	 * @throws ValueInvalidException
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 */
	public synchronized Absence getAbsencePerIdExcuse(int pIDExcuse)
			throws ValueInvalidException, EntityNotFoundException, ConnectionException, SQLException {
		Absence result = null;
		Connection connect = null;
		try {
			// If the ID was not provided, we return an error code
			if (pIDExcuse <= 0)
				throw new EntityNotFoundException("Impossibile trovare la l'Absence");

			/*
			 * We prepare the SQL string to retrieve the information corresponding to the
			 * class of the passed id id
			 */
			String tSql = "SELECT * FROM " + smos.storage.ManagerRegister.TABLE_ABSENCE + " WHERE id_excuse = "
					+ Utility.eNull(pIDExcuse);

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			if (tRs.next())
				result = this.loadAbsenceFromRs(tRs);
			else
				throw new EntityNotFoundException("Cannot find Absence!");

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Method that returns an Absence given the id of this one
	 *
	 * @param pIdAbsence an integer representing the Absence id
	 * @return an object of type Absence representing Absence
	 * @throws ValueInvalidException
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 */
	public synchronized Absence getAbsencePerIdAbsence(int pIdAbsence)
			throws ValueInvalidException, EntityNotFoundException, ConnectionException, SQLException {
		Absence result = null;
		Connection connect = null;
		try {
			// If the ID was not provided, we return an error code
			if (pIdAbsence <= 0)
				throw new EntityNotFoundException("Impossibile trovare l' Absence");

			/*
			 * We prepare the SQL string to retrieve the information corresponding to the
			 * class of the passed id id
			 */
			String tSql = "SELECT * FROM " + smos.storage.ManagerRegister.TABLE_ABSENCE + " WHERE id_absence = "
					+ Utility.eNull(pIdAbsence);

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			if (tRs.next())
				result = this.loadAbsenceFromRs(tRs);
			else
				throw new EntityNotFoundException("Cannot find Absence!");

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Method that returns a Delay given the id of this
	 *
	 * @param pIDDelay an integer representing the Delay id
	 * @return an object of type Delay representing the Delay
	 * @throws ValueInvalidException
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 */
	public synchronized Delay getDelayPerId(int pIDDelay)
			throws ValueInvalidException, EntityNotFoundException, ConnectionException, SQLException {
		Delay result = null;
		Connection connect = null;
		try {
			// If the ID was not provided, we return an error code
			if (pIDDelay <= 0)
				throw new EntityNotFoundException("Cannot find the delay");

			/*
			 * We prepare the SQL string to retrieve the information corresponding to the
			 * class of the passed id id
			 */
			String tSql = "SELECT * FROM " + smos.storage.ManagerRegister.DELAY_TABLE + " WHERE id_delay = "
					+ Utility.eNull(pIDDelay);

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			if (tRs.next())
				result = this.loadDelayFromRs(tRs);
			else
				throw new EntityNotFoundException("Cannot find Absence!");

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Method that returns an Absence given the id of this one
	 *
	 * @param pIdAbsence an integer representing the Absence id
	 * @return an object of type Absence representing Absence
	 * @throws ValueInvalidException
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 */
	public synchronized Excuse getExcusePerIdExcuse(int pIDExcuse)
			throws ValueInvalidException, EntityNotFoundException, ConnectionException, SQLException {
		Excuse result = null;
		Connection connect = null;
		try {
			// If the ID was not provided, we return an error code
			if (pIDExcuse <= 0)
				throw new EntityNotFoundException("Could not find the justification");

			/*
			 * We prepare the SQL string to retrieve the information corresponding to the
			 * class of the passed id id
			 */
			String tSql = "SELECT * FROM " + smos.storage.ManagerRegister.JUSTIFY_TABLE + " WHERE id_excuse = "
					+ Utility.eNull(pIDExcuse);

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			if (tRs.next())

				result = this.loadJustifyFromRs(tRs);
			else
				throw new EntityNotFoundException("Could not find the justification!");

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Method that returns true if the Absence data in input has a excuse
	 * assigned
	 *
	 * @param pAbsence an object of Absence value that you need to check if it has
	 * excuse
	 * @return true if Absence is justified, false otherwise
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 */
	public synchronized boolean haveExcuse(Absence pAbsence)
			throws EntityNotFoundException, ConnectionException, SQLException {
		if (!exists(pAbsence))
			throw new EntityNotFoundException("Absence not present in database");
		if (pAbsence.getIdExcuse() == null)
			return false;
		return true;
	}

	/**
	 * Method that returns the justification linked to a given Absence
	 *
	 * @param pAbsence an object of type Absence representing Absence
	 * @return an object of type Justify, or null if the Absence does not have
	 * excuse
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	public synchronized Excuse getExcusePerAbsence(Absence pAbsence)
			throws EntityNotFoundException, ConnectionException, SQLException, ValueInvalidException {
		if (!exists(pAbsence))
			throw new EntityNotFoundException("Absence not present in database");
		if (pAbsence.getIdExcuse() == null)
			return null;

		Excuse result = null;
		Connection connect = null;
		try {
			/*
			 * We prepare the SQL string to retrieve the information corresponding to the
			 * class of the passed id id
			 */
			String tSql = "SELECT * FROM " + smos.storage.ManagerRegister.JUSTIFY_TABLE + " WHERE id_excuse = "
					+ Utility.eNull(pAbsence.getIdExcuse());

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			if (tRs.next())
				result = this.loadJustifyFromRs(tRs);
			else
				throw new EntityNotFoundException("Could not find the justification!");

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}

	}

	/**
	 * Method that returns absences taken in a given school year and user in
	 * input
	 *
	 * @param pIdUser an integer representing the user's id
	 * @param pAcademicYear an integer representing the academic year
	 * @return a collection of absences (empty if the user did not have any absences)
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	public synchronized Collection<Absence> getAbsencePerIdUserAndAcademicYear(int pIdUser, int pAcademicYear)
			throws EntityNotFoundException, ConnectionException, SQLException, ValueInvalidException {
		Collection<Absence> result = new Vector<Absence>();
		Connection connect = null;
		try {
			// If the ID was not provided, we return an error code
			if (pAcademicYear <= 1970)
				throw new EntityNotFoundException("Too old date");

			// ditto for the user id
			if (pIdUser <= 0)
				throw new EntityNotFoundException("User non trovato");

			/*
			 * We prepare the SQL string to retrieve the information corresponding to the
			 * class of the passed id id
			 */
			String tSql = "SELECT * FROM " + smos.storage.ManagerRegister.TABLE_ABSENCE + " WHERE accademic_year = "
					+ Utility.eNull(pAcademicYear) + " AND id_user = " + Utility.eNull(pIdUser);

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			while (tRs.next()) {
				result.add(loadAbsenceFromRs(tRs));
			}

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Method that returns a collection of notes for a given user and a given
	 * school year
	 *
	 * @param pIdUser an integer representing the user's id
	 * @param pAcademicYear an integer representing the academic year
	 * @return a collection of notes, empty if the user hasn't received any
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	public synchronized Collection<Note> getNotePerIdUserAndAcademicYear(int pIdUser, int pAcademicYear)
			throws EntityNotFoundException, ConnectionException, SQLException, ValueInvalidException {
		Collection<Note> result = new Vector<Note>();
		Connection connect = null;
		try {
			// If the ID was not provided, we return an error code
			if (pAcademicYear <= 1970)
				throw new EntityNotFoundException("Too old date");

			// ditto for the user id
			if (pIdUser <= 0)
				throw new EntityNotFoundException("User non trovato");

			/*
			 * We prepare the SQL string to retrieve the information corresponding to the
			 * class of the passed id id
			 */
			String tSql = "SELECT * FROM " + smos.storage.ManagerRegister.TABLE_NOTE + " WHERE accademic_year = "
					+ Utility.eNull(pAcademicYear) + " AND id_user = " + Utility.eNull(pIdUser);

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			while (tRs.next()) {
				result.add(loadNoteFromRs(tRs));
			}

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Method that returns the Absence of a given student on a given day
	 *
	 * @param pIdUser an integer representing the student's id
	 * @param pData a string representing the formatted date for the database
	 * @return an object of type Absence, or null if the student was present
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	public synchronized Absence getAbsencePerIdUserAndData(int pIdUser, Date pData)
			throws EntityNotFoundException, ConnectionException, SQLException, ValueInvalidException {
		Absence result = new Absence();
		Connection connect = null;
		try {
			// TODO controls the formatting of the string

			// ditto for the user id
			if (pIdUser <= 0)
				throw new EntityNotFoundException("User non trovato");

			/*
			 * We prepare the SQL string to retrieve the information corresponding to the
			 * class of the passed id id
			 */
			String tSql = "SELECT * FROM " + smos.storage.ManagerRegister.TABLE_ABSENCE + " WHERE date_absence = "
					+ Utility.eNull(pData) + " AND id_user = " + Utility.eNull(pIdUser);

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			if (tRs.next()) {
				result = loadAbsenceFromRs(tRs);
			} else {
				result = null;
			}

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Method that returns the Delay of a given student on a given day
	 *
	 * @param pIdUser an integer representing the student's id
	 * @param pData a string representing the formatted date for the database
	 * @return an object of type Delay, or null if the student was on time or
	 *         absent
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	public synchronized Delay getDelayPerIdUserAndData(int pIdUser, Date pData)
			throws EntityNotFoundException, ConnectionException, SQLException, ValueInvalidException {
		Delay result = new Delay();
		Connection connect = null;
		try {
			// TODO controls the formatting of the string

			// ditto for the user id
			if (pIdUser <= 0)
				throw new EntityNotFoundException("User non trovato");

			/*
			 * We prepare the SQL string to retrieve the information corresponding to the
			 * class of the passed id id
			 */
			String tSql = "SELECT * FROM " + smos.storage.ManagerRegister.DELAY_TABLE + " WHERE date_delay = "
					+ Utility.eNull(pData) + " AND id_user = " + Utility.eNull(pIdUser);

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			if (tRs.next()) {
				result = loadDelayFromRs(tRs);
			} else {
				result = null;
			}

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	public synchronized Collection<RegisterLine> getRegistrationonsPerClassIDEData(int pClassID, Date pData)
			throws EntityNotFoundException, ConnectionException, SQLException, ValueInvalidException {

		Collection<RegisterLine> result = new Vector<RegisterLine>();
		ManagerUser mg = ManagerUser.getInstance();

		Collection<ItemListUsers> students = mg.getStudentPerClassId(pClassID);

		for (ItemListUsers x : students) {
			RegisterLine temp = new RegisterLine();
			temp.setStudent(x);
			temp.setAbsence(this.getAbsencePerIdUserAndData(x.getId(), pData));
			temp.setDelay(this.getDelayPerIdUserAndData(x.getId(), pData));
			result.add(temp);
		}

		return result;
	}

	/**
	 * Method that checks if there is an Absence in a log line
	 *
	 * @param pRegisterLine an object of type RegisterLine
	 * @return true if there is an Absence in the passed log line, otherwise
	 * false
	 */
	public boolean haveAbsence(RegisterLine pRegisterLine) {
		if (pRegisterLine.getAbsence() == null)
			return false;
		return true;
	}

	/**
	 * Method that checks if there is a Delay in a register line
	 *
	 * @param pRegisterLine an object of type RegisterLine
	 * @return true if there is a Delay in the passed register line, otherwise
	 * false
	 */
	public boolean haveDelay(RegisterLine pRegisterLine) {
		if (pRegisterLine.getDelay() == null)
			return false;
		return true;
	}

	/**
	 * Allows reading of only one record from the Result Set
	 *
	 * @param pRs The result set from which to extract the Absence object
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	private Absence loadAbsenceFromRs(ResultSet pRs) throws SQLException, ValueInvalidException {
		Absence absence = new Absence();

		absence.setIdAbsence(pRs.getInt("id_absence"));
		absence.setIdUser(pRs.getInt("id_user"));
		absence.setDateOfAbsence((Date) pRs.getDate("date_absence"));
		absence.setIdExcuse(pRs.getInt("id_excuse"));
		absence.setAcademicYear(pRs.getInt("accademic_year"));

		return absence;
	}

	/**
	 * Allows reading of only one record from the Result Set
	 *
	 * @param pRs The result set from which to extract the Justify object
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	private Excuse loadJustifyFromRs(ResultSet pRs) throws SQLException, ValueInvalidException {
		Excuse excuse = new Excuse();

		excuse.setIdExcuse(pRs.getInt("id_excuse"));
		excuse.setIdUser(pRs.getInt("id_user"));
		excuse.setDataExcuse(pRs.getDate("date_excuse"));
		excuse.setAcademicYear(pRs.getInt("accademic_year"));

		return excuse;
	}

	/**
	 * Allows reading of only one record from the Result Set
	 *
	 * @param pRs The result set from which to extract the Notes object
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */

	private Note loadNoteFromRs(ResultSet pRs) throws SQLException, ValueInvalidException {
		Note note = new Note();

		note.setIdNote(pRs.getInt("id_note"));
		note.setIdUser(pRs.getInt("id_user"));
		note.setDataNote(pRs.getDate("date_note"));
		note.setDescription(pRs.getString("description"));
		note.setTeacher(pRs.getString("teacher"));
		note.setAcademicYear(pRs.getInt("accademic_year"));

		return note;
	}

	/**
	 * Allows reading of only one record from the Result Set
	 *
	 * @param pRs The result set from which to extract the Delay object
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	private Delay loadDelayFromRs(ResultSet pRs) throws SQLException, ValueInvalidException {
		Delay delay = new Delay();

		delay.setIdDelay(pRs.getInt("id_delay"));
		delay.setIdUser(pRs.getInt("id_user"));
		delay.setDateDelay(pRs.getDate("date_delay"));
		delay.setTimeDelay(pRs.getString("time_delay"));
		delay.setAcademicYear(pRs.getInt("accademic_year"));

		return delay;
	}
}
