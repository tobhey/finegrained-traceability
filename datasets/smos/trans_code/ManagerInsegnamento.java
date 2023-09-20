package smos.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;
import java.sql.Connection;

/**
 * 
 * Teaching class manager.
 * 
 * @author Giulio D'Amora
 * @version 1.0
 * 
 */
public class ManagerTeaching {
	private static ManagerTeaching instance;

	/**
	 * The name of the teaching table.
	 */
	public static final String TEACHING_TABLE = "teaching";

	/**
	 * The name of the table that models the many-to-many association addresses and
	 * teachings.
	 */
	public static final String ADDRESS_HAS_TEACHING_TABLE = "address_has_teaching";

	/**
	 * The name of the table that models the many-to-many association between users
	 * and teachings.
	 */
	public static final String TEACHER_HAS_CLASSROOM_TABLE = "teacher_has_classroom";

	/**
	 * The constructor of the class.
	 */
	private ManagerTeaching() {
		super();
	}

	/**
	 * Returns the only instance of the teaching existsnte.
	 *
	 * @return Returns the instance of the class.
	 */
	public static synchronized ManagerTeaching getInstance() {
		if (instance == null) {
			instance = new ManagerTeaching();
		}
		return instance;
	}

	/**
	 * Check the existence of a teaching in the database.
	 *
	 * @param pTeaching The teaching to check.
	 * @return Returns true if the teaching passed as parameter exists, false
	 *         otherwise.
	 *
	 * @throws FieldRequiredException
	 * @throws SQLException
	 * @throws ConnectionException
	 */
	public synchronized boolean exists(Teaching pTeaching)
			throws FieldRequiredException, ConnectionException, SQLException {

		boolean result = false;
		Connection connect = null;

		if (pTeaching.getName() == null)
			throw new FieldRequiredException("Specify the name.");
		try {
			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			String sql = "SELECT * FROM " + ManagerTeaching.TEACHING_TABLE + " WHERE name = "
					+ Utility.eNull(pTeaching.getName());

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
	 * Insert a new teaching in the teaching table.
	 *
	 * @param pTeaching The teaching from insert.
	 *
	 * @throws SQLException
	 * @throws ConnectionException
	 * @throws FieldRequiredException
	 * @throws EntityNotFoundException
	 * @throws ValueInvalidException
	 */
	public synchronized void insert(Teaching pTeaching) throws FieldRequiredException, ConnectionException,
			SQLException, EntityNotFoundException, ValueInvalidException {
		Connection connect = null;
		try {
			// check the required fields
			if (pTeaching.getName() == null)
				throw new FieldRequiredException("Specify the name field");

			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();
			// We prepare the Sql string
			String sql = "INSERT INTO " + ManagerTeaching.TEACHING_TABLE + " (name) " + "VALUES ("
					+ Utility.eNull(pTeaching.getName()) + ")";

			Utility.performOperation(connect, sql);

			pTeaching.setId(Utility.getMaximumValue("id_teaching", ManagerTeaching.TEACHING_TABLE));

		} finally {
			// release resources

			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Update a teaching present in the teaching table.
	 *
	 * @param pTeaching The teaching to be changed
	 *
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws FieldRequiredException
	 */
	public synchronized void update(Teaching pTeaching)
			throws ConnectionException, SQLException, EntityNotFoundException, FieldRequiredException {
		Connection connect = null;

		try {
			if (pTeaching.getId() <= 0)
				throw new EntityNotFoundException("Could not find teaching!");

			if (pTeaching.getName() == null)
				throw new FieldRequiredException("Specify the name field");

			// Let's prepare the SQL string
			String sql = "UPDATE " + ManagerTeaching.TEACHING_TABLE + " SET" + " name = "
					+ Utility.eNull(pTeaching.getName()) + " WHERE id_teaching = " + Utility.eNull(pTeaching.getId());

			// make a new connection and send the query
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			Utility.performOperation(connect, sql);
		} finally {
			// release resources
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Delete a teaching from the teaching table.
	 *
	 * @param pTeaching The teaching from remove.
	 * 
	 * @throws FieldRequiredException
	 * @throws EntityNotFoundException
	 * @throws SQLException
	 * @throws ConnectionException
	 * @throws ValueInvalidException
	 * 
	 */
	public synchronized void remove(Teaching pTeaching) throws ConnectionException, SQLException,
			EntityNotFoundException, FieldRequiredException, ValueInvalidException {
		Connection connect = null;

		try {
			// ManagerTeaching.getInstance().teachingOnDeleteCascade(pTeaching);
			connect = DBConnection.getConnection();
			// Let's prepare the SQL string
			String sql = "DELETE FROM " + ManagerTeaching.TEACHING_TABLE + " WHERE id_teaching = "
					+ Utility.eNull(pTeaching.getId());

			Utility.performOperation(connect, sql);
		} finally {
			// release resources
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Returns the teaching id passed as a parameter.
	 *
	 * @param pTeaching The teaching for which the id is requested.
	 * @return Returns the teaching id passed as a parameter.
	 * 
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 */
	public synchronized int getTeachingId(Teaching pTeaching)
			throws EntityNotFoundException, ConnectionException, SQLException {
		int result = 0;
		Connection connect = null;
		try {
			if (pTeaching == null)
				throw new EntityNotFoundException("Could not find teaching!");

			/*
			 * We prepare the SQL string to retrieve the information corresponding to the
			 * teaching id passed as a parameter.
			 */
			String tSql = "SELECT id_teaching FROM " + ManagerTeaching.TEACHING_TABLE + " WHERE name = "
					+ Utility.eNull(pTeaching.getName());

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			if (tRs.next())
				result = tRs.getInt("id_teaching");

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Return the name of the teaching corresponding to the id passed as parameter.
	 *
	 * @param pId The id of the teaching.
	 * @return Returns a string containing the name of the teaching.
	 * 
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 */
	public synchronized String getTeachingNamePerId(int pId)
			throws EntityNotFoundException, ConnectionException, SQLException {
		String result;
		Connection connect = null;
		try {
			// If the ID has not been provided, we will return an error code
			if (pId <= 0)
				throw new EntityNotFoundException("Could not find teaching!");

			/*
			 * We prepare the SQL string to retrieve the information corrispondenti all'id
			 * dell'teaching passato come parametro
			 */
			String tSql = "SELECT name FROM " + ManagerTeaching.TEACHING_TABLE + " WHERE id_teaching = "
					+ Utility.eNull(pId);

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			if (tRs.next())
				result = tRs.getString("name");
			else
				throw new EntityNotFoundException("Could not find teaching!");

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Returns the teaching corresponding to the id passed as a parameter.
	 *
	 * @param pId The id of the teaching.
	 * @return Returns the teaching associated with the id passed as a parameter.
	 * 
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws ValueInvalidException
	 */
	public synchronized Teaching getTeachingPerId(int pId)
			throws ConnectionException, SQLException, EntityNotFoundException, ValueInvalidException {
		Teaching result = null;
		Connection connect = null;
		try {

			if (pId <= 0)
				throw new EntityNotFoundException("Could not find teaching!");

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// Let's prepare the SQL string
			String sql = "SELECT * FROM " + ManagerTeaching.TEACHING_TABLE + " WHERE id_teaching = "
					+ Utility.eNull(pId);

			// We send the Query to the DataBase
			ResultSet pRs = Utility.queryOperation(connect, sql);

			if (pRs.next())
				result = this.loadRecordFromRs(pRs);
			else
				throw new EntityNotFoundException("Could not find teaching!");

			return result;
		} finally {
			// release resources
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Returns the set of all the teachings present in the database.
	 *
	 * @return A collection of teachings returns.
	 * 
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws ValueInvalidException
	 * @throws EntityNotFoundException
	 */
	public synchronized Collection<Teaching> getTeachings()
			throws ConnectionException, SQLException, ValueInvalidException, EntityNotFoundException {
		Collection<Teaching> result = null;
		Connection connect = null;

		try {
			// Let's prepare the SQL string
			String sql = "SELECT * FROM " + ManagerTeaching.TEACHING_TABLE + " ORDER BY name";

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet pRs = Utility.queryOperation(connect, sql);

			if (pRs.next())
				result = this.loadRecordsFromRs(pRs);

			return result;
		} finally {
			// release resources
			DBConnection.releaseConnections(connect);
		}

	}

	/**
	 * Returns the set of teachings associated with the corresponding user to the id
	 * passed as a parameter.
	 *
	 * @param pId The user's id.
	 * @return A collection of teachings returns.
	 * 
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	public synchronized Collection<Teaching> getTeachingsPerUserId(int pId)
			throws EntityNotFoundException, ConnectionException, SQLException, ValueInvalidException {

		Collection<Teaching> result = null;
		Connection connect = null;

		if (pId <= 0)
			throw new EntityNotFoundException("specify the user");

		try {
			// Let's prepare the SQL string
			String sql = "SELECT " + ManagerTeaching.TEACHING_TABLE + ".* FROM "
					+ ManagerTeaching.TEACHER_HAS_CLASSROOM_TABLE + ", " + ManagerTeaching.TEACHING_TABLE + " WHERE ("
					+ ManagerTeaching.TEACHER_HAS_CLASSROOM_TABLE + ".id_teaching = " + ManagerTeaching.TEACHING_TABLE
					+ ".id_teaching AND " + ManagerTeaching.TEACHER_HAS_CLASSROOM_TABLE + ".id_user = "
					+ Utility.eNull(pId) + ")" + " ORDER BY name";

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet pRs = Utility.queryOperation(connect, sql);

			if (pRs.next())
				result = this.loadRecordsFromRs(pRs);

			return result;
		} finally {
			// release resources
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Returns the set of teachings that the teacher teaches in the class
	 *
	 * @param pId      Teacher The user's id.
	 * @param pIdClass the id of the class
	 * @return A collection of teachings returns.
	 *
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	public synchronized Collection<Teaching> getTeachingsPerUserClassID(int pIdTeacher, int pIdClass)
			throws EntityNotFoundException, ConnectionException, SQLException, ValueInvalidException {

		Collection<Teaching> result = null;
		Connection connect = null;

		if (pIdTeacher <= 0)
			throw new EntityNotFoundException("specify the user");
		if (pIdClass <= 0)
			throw new EntityNotFoundException("specify the class");

		try {
			// Let's prepare the SQL string

			String sql = "SELECT DISTINCT " + ManagerTeaching.TEACHING_TABLE + ".* FROM "
					+ ManagerTeaching.TEACHER_HAS_CLASSROOM_TABLE + ", " + ManagerTeaching.TEACHING_TABLE + " WHERE ("
					+ ManagerTeaching.TEACHER_HAS_CLASSROOM_TABLE + ".id_user = " + Utility.eNull(pIdTeacher)
					+ " AND " + ManagerTeaching.TEACHER_HAS_CLASSROOM_TABLE + ".id_teaching= " + Utility.eNull(pIdClass)
					+ " AND " + ManagerTeaching.TEACHER_HAS_CLASSROOM_TABLE + ".id_teaching = "
					+ ManagerTeaching.TEACHING_TABLE + ".id_teaching " + ") ORDER BY name";

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet pRs = Utility.queryOperation(connect, sql);

			if (pRs.next())
				result = this.loadRecordsFromRs(pRs);

			return result;
		} finally {
			// release resources
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Check if a teaching has an assigned professor.
	 *
	 * @param pTeaching The teaching to check.
	 * @return Returns true if the teaching has a professor assigned, false
	 *         otherwise.
	 * 
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws ValueInvalidException
	 */
	public synchronized boolean hasTeacher(Teaching pTeaching)
			throws SQLException, EntityNotFoundException, ConnectionException, ValueInvalidException {
		Connection connect = null;
		boolean result = false;
		if (pTeaching.getId() <= 0)
			throw new EntityNotFoundException("Specify the teaching");

		try {
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// Prepare the sql string
			String sql = "SELECT * FROM " + ManagerTeaching.TEACHER_HAS_CLASSROOM_TABLE + " WHERE id_teaching = "
					+ Utility.eNull(pTeaching.getId());
			// We send the Query to the database
			ResultSet pRs = Utility.queryOperation(connect, sql);
			if (pRs.next())
				result = true;

			return result;

		} finally {
			// we release the resources
			DBConnection.releaseConnections(connect);

		}
	}

	/**
	 * Returns the set of courses associated with the specified class
	 *
	 * @param pId The id of the class.
	 * @return A collection of teachings returns.
	 * 
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	public synchronized Collection<Teaching> getTeachingsPerClassId(int pId)
			throws EntityNotFoundException, ConnectionException, SQLException, ValueInvalidException {

		Collection<Teaching> result = null;
		Connection connect = null;

		if (pId < 0)
			throw new EntityNotFoundException("specify the id of the Class!");

		try {
			// Let's prepare the SQL string
			String sql = "SELECT " + ManagerTeaching.TEACHING_TABLE + ".* FROM " + ManagerClass.CLASS_TABLE + ", "
					+ ManagerTeaching.ADDRESS_HAS_TEACHING_TABLE + ", " + ManagerTeaching.TEACHING_TABLE + " WHERE "
					+ ManagerClass.CLASS_TABLE + ".id_classroom = " + Utility.eNull(pId) + " AND "
					+ ManagerClass.CLASS_TABLE + ".id_address = " + ManagerTeaching.ADDRESS_HAS_TEACHING_TABLE
					+ ".id_address AND " + ManagerTeaching.TEACHING_TABLE + ".id_teaching= "
					+ ManagerTeaching.ADDRESS_HAS_TEACHING_TABLE + ".id_teaching ";

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet pRs = Utility.queryOperation(connect, sql);

			if (pRs.next())
				result = this.loadRecordsFromRs(pRs);

			return result;
		} finally {
			// release resources
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Returns the set of courses associated with the specified class
	 *
	 * @param name The name of the class.
	 * @return A collection of teachings returns.
	 * 
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	public synchronized Collection<Teaching> getTeachingsPerClassName(String name)
			throws EntityNotFoundException, ConnectionException, SQLException, ValueInvalidException {

		Collection<Teaching> result = null;
		Connection connect = null;

		if ((name == null) || (name == ""))
			throw new EntityNotFoundException("specify the name of the Class!");

		try {
			// Let's prepare the SQL string
			String sql = "SELECT " + ManagerTeaching.TEACHING_TABLE + ".* FROM " + ManagerClass.CLASS_TABLE + ", "
					+ ManagerTeaching.ADDRESS_HAS_TEACHING_TABLE + ", " + ManagerTeaching.TEACHING_TABLE + " WHERE "
					+ ManagerClass.CLASS_TABLE + ".name = " + Utility.eNull(name) + " AND " + ManagerClass.CLASS_TABLE
					+ ".id_address = " + ManagerTeaching.ADDRESS_HAS_TEACHING_TABLE + ".id_address AND "
					+ ManagerTeaching.TEACHING_TABLE + ".id_teaching= " + ManagerTeaching.ADDRESS_HAS_TEACHING_TABLE
					+ ".id_teaching ";

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet pRs = Utility.queryOperation(connect, sql);

			if (pRs.next())
				result = this.loadRecordsFromRs(pRs);

			return result;
		} finally {
			// release resources
			DBConnection.releaseConnections(connect);
		}
	}

	public synchronized Collection<Teaching> getTeachingsPerIdUserIdClass(int pUser, int pClass)
			throws SQLException, EntityNotFoundException, ConnectionException, ValueInvalidException {

		Collection<Teaching> result = null;
		Connection connect = null;
		try {
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// Prepare the sql string
			// select teaching.* from teacher_has_classroom AS THC , teaching where
			// thc.id_user = 54
			// && thc.id_classroom = 64 && thc.id_teaching = teaching.id_teaching

			String sql = "SELECT DISTINCT " + ManagerTeaching.TEACHING_TABLE + ".*" + " FROM "
					+ ManagerUser.TEACHER_HAS_CLASSROOM_TABLE + " , " + ManagerTeaching.TEACHING_TABLE + " WHERE "
					+ ManagerUser.TEACHER_HAS_CLASSROOM_TABLE + ".id_user = " + Utility.eNull(pUser) + " AND "
					+ ManagerUser.TEACHER_HAS_CLASSROOM_TABLE + ".id_classroom= " + Utility.eNull(pClass) + " AND "
					+ ManagerUser.TEACHER_HAS_CLASSROOM_TABLE + ".id_teaching =" + ManagerTeaching.TEACHING_TABLE
					+ ".id_teaching";
			// We send the Query to the database
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet pRs = Utility.queryOperation(connect, sql);

			if (pRs.next())
				result = this.loadRecordsFromRs(pRs);

			return result;
		} finally {
			// release resources
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Allows reading of a record from the ResultSet.
	 *
	 * @param pRs The result of the query.
	 * @return Returns the teaching read.
	 * 
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	private Teaching loadRecordFromRs(ResultSet pRs) throws SQLException, ValueInvalidException {
		Teaching teaching = new Teaching();
		teaching.setName(pRs.getString(("name")));
		teaching.setId(pRs.getInt("id_teaching"));

		return teaching;
	}

	/**
	 * Allows reading of records from the ResultSet.
	 *
	 * @param pRs The result of the query.
	 * @return The collection of lessons read is back.
	 * 
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	private Collection<Teaching> loadRecordsFromRs(ResultSet pRs) throws SQLException, ValueInvalidException {
		Collection<Teaching> result = new Vector<Teaching>();
		do {
			result.add(loadRecordFromRs(pRs));
		} while (pRs.next());
		return result;
	}

}
