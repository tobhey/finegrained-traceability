package smos.storage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * Class that managed the classes of the institution
 * 
 * @author Nicola Pisanti
 * @version 1.0
 */
public class ManagerClass {

	private static ManagerClass instance;

	public static final String CLASS_TABLE = "classroom";
	public static final String ADDRESS_TABLE = "address";
	public static final String TEACHER_HAS_CLASSROOM_TABLE = "teacher_has_classroom";
	public static final String STUDENT_HAS_CLASSROOM_TABLE = "student_has_classroom";

	private ManagerClass() {
		super();
	}

	/**
	 * Returns the only instance of the existing class.
	 *
	 * @return Returns the instance of the class.
	 */
	public static synchronized ManagerClass getInstance() {
		if (instance == null) {
			instance = new ManagerClass();
		}
		return instance;
	}

	/**
	 * Check if the class given in input is in the database
	 * 
	 * @param The class that needs to be checked for existence
	 * @return true if the class is in the database, false otherwise
	 * @throws FieldRequiredException
	 * @throws ConnectionException
	 * @throws SQLException
	 */
	public synchronized boolean exists(Class pClass) throws FieldRequiredException, ConnectionException, SQLException {

		boolean result = false;
		Connection connect = null;

		if (pClass.getName() == null)
			throw new FieldRequiredException("Specify the name of the class.");
		if (pClass.getAcademicYear() <= 1970)
			throw new FieldRequiredException("Specify the academic year");
		if (pClass.getIdStreetAddress() <= 0) {
			throw new FieldRequiredException("Specify the address");
			// the user enters the address, it is converted to idAddress
		}

		try {
			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			String sql = "SELECT * FROM " + ManagerClass.CLASS_TABLE + " WHERE name = "
					+ Utility.eNull(pClass.getName()) + " AND accademic_year = " + Utility.eNull(
							pClass.getAcademicYear() + " AND id_address = " + Utility.eNull(pClass.getIdStreetAddress())

					);

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
	 * Insert the object of type class into the database
	 * 
	 * @param the class to insert into the database
	 * @throws FieldRequiredException
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws ValueInvalidException
	 */
	public synchronized void insert(Class pClass) throws FieldRequiredException, ConnectionException, SQLException,
			EntityNotFoundException, ValueInvalidException {

		Connection connect = null;
		try {
			// check the required fields
			if (pClass.getName() == null)
				throw new FieldRequiredException("Specify the name of the class.");
			if (pClass.getAcademicYear() <= 1970)
				throw new FieldRequiredException("Specify the academic year");
			if (pClass.getIdStreetAddress() <= 0) {
				throw new FieldRequiredException("Specify the address");
				// the user enters the address, it is converted to idAddress
			}

			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();
			// We prepare the Sql string
			String sql = "INSERT INTO " + ManagerClass.CLASS_TABLE + " (id_address, name, accademic_year) " + "VALUES ("
					+ Utility.eNull(pClass.getIdStreetAddress()) + "," + Utility.eNull(pClass.getName()) + ","
					+ Utility.eNull(pClass.getAcademicYear()) + ")";

			Utility.performOperation(connect, sql);

			pClass.setIdClass((Utility.getMaximumValue("id_classroom", ManagerClass.CLASS_TABLE)));

		} finally {
			// release the resources

			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Update the statistics of a class
	 * 
	 * @param The class with the updated statistics (but identical ID)
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws FieldRequiredException
	 */
	public synchronized void update(Class pClass)
			throws ConnectionException, SQLException, EntityNotFoundException, FieldRequiredException {
		Connection connect = null;

		try {
			if (pClass.getIdClass() <= 0)
				throw new EntityNotFoundException("Couldn't find the class!");

			if (pClass.getName() == null)
				throw new FieldRequiredException("Specify the name of the class.");
			if (pClass.getAcademicYear() <= 1970)
				throw new FieldRequiredException("Specify the academic year");
			if (pClass.getIdStreetAddress() <= 0) {
				throw new FieldRequiredException("Specify the address");
				// the user enters the address, it is converted to idAddress
			}
			// We prepare the SQL string
			String sql = "UPDATE " + ManagerClass.CLASS_TABLE + " SET" + " id_address = "
					+ Utility.eNull(pClass.getIdStreetAddress()) + ", name = " + Utility.eNull(pClass.getName())
					+ ", accademic_year = " + Utility.eNull(pClass.getAcademicYear()) + " WHERE id_classroom = "
					+ Utility.eNull(pClass.getIdClass());

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
	 * Delete a class from the database
	 * 
	 * @param The class to be deleted
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws FieldRequiredException
	 * @throws ValueInvalidException
	 */
	public synchronized void remove(Class pClass) throws ConnectionException, SQLException, EntityNotFoundException,
			FieldRequiredException, ValueInvalidException {
		Connection connect = null;

		try {
			// ManagerUser.getInstance().userOnDeleteCascade(pUser);
			connect = DBConnection.getConnection();
			// We prepare the SQL string
			String sql = "DELETE FROM " + ManagerClass.CLASS_TABLE + " WHERE id_classroom = "
					+ Utility.eNull(pClass.getIdClass());

			Utility.performOperation(connect, sql);
		} finally {
			// release the resources
			DBConnection.releaseConnections(connect);
		}
	}

	public synchronized Collection<Class> getClassPerStudent(User pUser) throws EntityNotFoundException,
			ConnectionException, SQLException, ValueInvalidException, FieldRequiredException {
		Collection<Class> result = null;
		Connection connect = null;
		ManagerUser managerUser = ManagerUser.getInstance();
		try {
			// If the user does not exist
			if (!managerUser.exists(pUser))
				throw new EntityNotFoundException("The user does not exist!!!");
			if (!managerUser.eStudent(pUser))
				throw new ValueInvalidException("The user is not a student!");
			/*
			 * We prepare the SQL string to retrieve the information corresponding to the
			 * class of the passed id
			 */
			int iduser = managerUser.getUserId(pUser);
			String tSql =

					"SELECT " + ManagerClass.CLASS_TABLE + ".* FROM " + ManagerClass.STUDENT_HAS_CLASSROOM_TABLE + ", "
							+ ManagerClass.CLASS_TABLE + " WHERE " + ManagerClass.STUDENT_HAS_CLASSROOM_TABLE
							+ ".id_user = " + Utility.eNull(iduser) + " AND " + ManagerClass.CLASS_TABLE
							+ ".id_classroom = " + ManagerClass.STUDENT_HAS_CLASSROOM_TABLE + ".id_classroom";

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			result = this.loadRecordsFromRs(tRs);

			if (result.isEmpty())
				throw new EntityNotFoundException("Could not Find Classes for the entered user");

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Returns the class that has the passed ID
	 * 
	 * @param The ID of the class being searched for
	 * @return a string representing the class with the supplied ID
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	public synchronized Class getClassPerID(int pId)
			throws EntityNotFoundException, ConnectionException, SQLException, ValueInvalidException {
		Class result = null;
		Connection connect = null;
		try {
			// If the ID was not provided, we return an error code
			if (pId <= 0)
				throw new EntityNotFoundException("Couldn't find the class!");

			/*
			 * We prepare the SQL string to retrieve the information corresponding to the
			 * class of the passed id
			 */
			String tSql = "SELECT * FROM " + ManagerClass.CLASS_TABLE + " WHERE id_classroom = " + Utility.eNull(pId);

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			if (tRs.next())
				result = this.loadRecordFromRs(tRs);
			else
				throw new EntityNotFoundException("Unable to find user!");

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Returns a collection of classes from the same academic year
	 */
	public synchronized Collection<Class> getClassPerAcademicYear(int pAcademicYear)
			throws EntityNotFoundException, ConnectionException, SQLException, ValueInvalidException {
		Collection<Class> result = null;
		Connection connect = null;
		try {
			// If the ID was not provided, we return an error code
			if (pAcademicYear <= 1970)
				throw new EntityNotFoundException("Too old date");

			/*
			 * We prepare the SQL string to retrieve the information corresponding to the
			 * class of the passed id
			 */
			String tSql = "SELECT * FROM " + ManagerClass.CLASS_TABLE + " WHERE accademic_year = "
					+ Utility.eNull(pAcademicYear);

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			result = this.loadRecordsFromRs(tRs);

			if (result.isEmpty())
				throw new EntityNotFoundException("Could not Find Classes for the date entered");

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	public synchronized Collection<Integer> getAcademicYearList()
			throws EntityNotFoundException, ConnectionException, SQLException, ValueInvalidException {
		Collection<Integer> result = null;
		Connection connect = null;
		try {

			/*
			 * We prepare the SQL string to retrieve the information corresponding to the
			 * class of the passed id id
			 */
			String tSql = "SELECT DISTINCT accademic_year FROM " + ManagerClass.CLASS_TABLE
					+ " order by accademic_year ";

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			result = this.loadIntegersFromRs(tRs);

			if (result.isEmpty())
				throw new EntityNotFoundException("Could not Find Classes for the date entered");

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	public synchronized Class getClassPerUserAcademicYear(User pUser, int pAcademicYear) throws EntityNotFoundException,
			ConnectionException, SQLException, ValueInvalidException, FieldRequiredException {
		Class result = null;
		Class temp = null;
		ManagerClass managerClassroom = ManagerClass.getInstance();
		Collection<Class> list = null;
		list = managerClassroom.getClassPerStudent(pUser);
		Iterator<Class> it = list.iterator();
		while (it.hasNext()) {
			temp = it.next();
			if (temp.getAcademicYear() == pAcademicYear) {
				result = temp;
				break;
			}
		}
		return result;
	}

	public synchronized Collection<Class> getClassPerTeacherAcademicYear(User pUser, int pAcademicYear)
			throws EntityNotFoundException, ConnectionException, SQLException, ValueInvalidException,
			FieldRequiredException {
		Collection<Class> result = null;
		Connection connect = null;
		int idUser = pUser.getId();
		try {
			// If the ID was not provided, we return an error code
			if (pAcademicYear <= 1970)
				throw new EntityNotFoundException("Too old date");

			/*
			 * We prepare the SQL string to retrieve the information
			 * 
			 */
			String tSql = "SELECT DISTINCT " + ManagerClass.CLASS_TABLE + ".* FROM " + ManagerClass.CLASS_TABLE + ", "
					+ ManagerClass.TEACHER_HAS_CLASSROOM_TABLE + " WHERE  " + ManagerClass.CLASS_TABLE
					+ ".id_classroom = " + ManagerClass.TEACHER_HAS_CLASSROOM_TABLE + ".id_classroom  AND "
					+ ManagerClass.CLASS_TABLE + ".accademic_year = " + Utility.eNull(pAcademicYear) + " AND "
					+ ManagerClass.TEACHER_HAS_CLASSROOM_TABLE + ".id_user = " + Utility.eNull(idUser);

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			result = this.loadRecordsFromRs(tRs);

			if (result.isEmpty())
				throw new EntityNotFoundException("Could not Find Classes for the entered user and year");

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}

	}

	public synchronized Collection<Class> getClassPerTeacher(User pUser) throws EntityNotFoundException,
			ConnectionException, SQLException, ValueInvalidException, FieldRequiredException {
		Collection<Class> result = null;
		Connection connect = null;
		ManagerUser managerUser = ManagerUser.getInstance();
		try {
			// If the user does not exist
			if (!managerUser.exists(pUser))
				throw new EntityNotFoundException("The user does not exist!!!");
			if (!managerUser.eTeacher(pUser))
				throw new ValueInvalidException("The user is not a student!");
			/*
			 * We prepare the SQL string to retrieve the information corresponding to the
			 * class of the passed id id
			 */
			int iduser = managerUser.getUserId(pUser);
			String tSql =

					"SELECT DISTINCT " + ManagerClass.CLASS_TABLE + ".* FROM "
							+ ManagerClass.TEACHER_HAS_CLASSROOM_TABLE + ", " + ManagerClass.CLASS_TABLE + " WHERE "
							+ ManagerClass.TEACHER_HAS_CLASSROOM_TABLE + ".id_user = " + Utility.eNull(iduser) + " AND "
							+ ManagerClass.CLASS_TABLE + ".id_classroom = " + ManagerClass.TEACHER_HAS_CLASSROOM_TABLE
							+ ".id_classroom";

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			result = this.loadRecordsFromRs(tRs);

			if (result.isEmpty()) {

				throw new EntityNotFoundException("Could not Find Classes for the entered user");
			}

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Consent to read an integer from the recod resultSet
	 *
	 * @param pRs resultSet
	 * @return collection <Integer>
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	private Collection<Integer> loadIntegersFromRs(ResultSet pRs) throws SQLException, ValueInvalidException {
		Collection<Integer> result = new Vector<Integer>();
		while (pRs.next()) {
			result.add(pRs.getInt("accademic_year"));
		}
		return result;
	}

	/**
	 * Allows reading of only one record from the Result Set
	 * 
	 * @param The result set from which to extract the Classroom object
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	private Class loadRecordFromRs(ResultSet pRs) throws SQLException, ValueInvalidException {
		Class classroom = new Class();
		classroom.setName(pRs.getString("name"));
		classroom.setAcademicYear(pRs.getInt("accademic_year"));
		classroom.setIdClass(pRs.getInt("id_classroom"));
		classroom.setIdStreetAddress(pRs.getInt("id_address"));
		return classroom;
	}

	/**
	 * Allows reading of multiple records from the Result Set
	 * 
	 * @param The result set from which to extract the Classroom object
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	private Collection<Class> loadRecordsFromRs(ResultSet pRs) throws SQLException, ValueInvalidException {
		Collection<Class> result = new Vector<Class>();
		while (pRs.next()) {
			result.add(loadRecordFromRs(pRs));
		}
		return result;
	}

}
