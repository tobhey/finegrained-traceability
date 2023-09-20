package smos.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;
import java.sql.Connection;

/**
 * 
 * Class manager of grades.
 * 
 * @author Luigi Colangelo
 * @version 1.0
 * 
 * 
 */
public class ManagerGrade {
	private static ManagerGrade instance;

	/**
	 * The name of the grading table.
	 */
	public static final String TABELLA_Grade = "grades";

	/**
	 * The constructor of the class.
	 */
	public ManagerGrade() {
		super();
	}

	/**
	 * Returns the only instance of the existing grade.
	 *
	 * @return Returns the instance of the class.
	 */
	public static synchronized ManagerGrade getInstance() {
		if (instance == null) {
			instance = new ManagerGrade();
		}
		return instance;
	}

	/**
	 * Check the gradeng existence in the database.
	 *
	 * @param pGrade the grade to check.
	 * @return Returns true if the grade passed as a parameter exists, false
	 *         otherwise.
	 *
	 * @throws FieldRequiredException
	 * @throws SQLException
	 * @throws ConnectionException
	 */
	public synchronized boolean exists(Grade pGrade) throws FieldRequiredException, ConnectionException, SQLException {

		boolean result = false;
		Connection connect = null;

		if (pGrade.getId_grade() == 0)
			throw new FieldRequiredException("Specificare l'id.");
		try {
			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			String sql = "SELECT * FROM " + ManagerGrade.TABELLA_Grade + " WHERE id_grades = "
					+ Utility.eNull(pGrade.getId_grade());

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
	 * Insert a new Grade in the grades table.
	 *
	 * @param pGrade the Grade to be inserted.
	 *
	 * @throws SQLException
	 * @throws ConnectionException
	 * @throws FieldRequiredException
	 * @throws EntityNotFoundException
	 * @throws ValueInvalidException
	 */
	public synchronized void insert(Grade pGrade) throws FieldRequiredException, ConnectionException, SQLException,
			EntityNotFoundException, ValueInvalidException {
		Connection connect = null;
		try {

			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();
			// We prepare the Sql string
			String sql = "INSERT INTO " + ManagerGrade.TABELLA_Grade
					+ " (id_user, id_teaching, written, oral, laboratory, AccademicYear, turn) " + "VALUES ("
					+ Utility.eNull(pGrade.getId_user()) + ", " + Utility.eNull(pGrade.getTeaching()) + ", "
					+ Utility.eNull(pGrade.getWriting()) + ", " + Utility.eNull(pGrade.getOral()) + ", "
					+ Utility.eNull(pGrade.getLaboratory()) + ", " + Utility.eNull(pGrade.getAcademicYear()) + ", "
					+ Utility.eNull(pGrade.getrotation()) + " )";

			Utility.performOperation(connect, sql);

			pGrade.setId_grade(Utility.getMaximumValue("id_grades", ManagerGrade.TABELLA_Grade));

		} finally {
			// release resources

			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Update a Grade present in the grades table.
	 *
	 * @param pGrade A Grade to edit
	 *
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws FieldRequiredException
	 */
	public synchronized void update(Grade pGrade)
			throws ConnectionException, SQLException, EntityNotFoundException, FieldRequiredException {
		Connection connect = null;

		try {
			if (pGrade.getId_grade() <= 0)
				throw new EntityNotFoundException("Couldn't find the Grade!");

			if (pGrade.getId_user() <= 0)
				throw new FieldRequiredException("Specify the user of the Grade");
			if (pGrade.getTeaching() <= 0)
				throw new FieldRequiredException("Specify the teaching del Grade");
			if (pGrade.getAcademicYear() <= 0)
				throw new FieldRequiredException("Specify the academic year");
			if (pGrade.getrotation() < 0)
				throw new FieldRequiredException("Specify the semester ");
			// Let's prepare the SQL string
			String sql = "UPDATE " + ManagerGrade.TABELLA_Grade + " SET" + " id_user = "
					+ Utility.eNull(pGrade.getId_user()) + "," + " id_teaching= " + Utility.eNull(pGrade.getTeaching())
					+ "," + " written= " + Utility.eNull(pGrade.getWriting()) + "," + " oral= "
					+ Utility.eNull(pGrade.getOral()) + "," + " laboratory= " + Utility.eNull(pGrade.getLaboratory())
					+ "," + " accademicYear= " + Utility.eNull(pGrade.getAcademicYear()) + "," + " turn="
					+ Utility.eNull(pGrade.getrotation()) + " WHERE id_grades = " + Utility.eNull(pGrade.getId_grade());

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
	 * Check if a student passed as a parameter has a Grade assigned
	 * in the teaching passed as a parameter in the past year as a parameter and in the
	 * quarterly passed as parameter
	 *
	 *
	 * @param pTeaching The teaching to control.
	 * @param pUserListItem The student to check
	 *
	 * @return Returns the id of Grade -1 otherwise
	 *
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws ValueInvalidException
	 */
	public synchronized int getIdGrade(Teaching pTeaching, int academicYear, int rotation, ItemListUsers pUser)
			throws SQLException, EntityNotFoundException, ConnectionException, ValueInvalidException {
		Connection connect = null;
		int result = -1;
		Grade v = null;
		if (pTeaching.getId() <= 0)
			throw new EntityNotFoundException("Specify the teaching");
		if (pUser.getId() <= 0)
			throw new EntityNotFoundException("Specificare l'user");
		try {
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// Prepare the sql string
			String sql = "SELECT * FROM " + ManagerGrade.TABELLA_Grade + " WHERE id_teaching = "
					+ Utility.eNull(pTeaching.getId()) + " AND " + ManagerGrade.TABELLA_Grade + ".AccademicYear= "
					+ Utility.eNull(academicYear) + " AND " + ManagerGrade.TABELLA_Grade + ".turn= "
					+ Utility.eNull(rotation) + " AND " + ManagerGrade.TABELLA_Grade + ".id_user= "
					+ Utility.eNull(pUser.getId());
			// We send the Query to the database
			ResultSet pRs = Utility.queryOperation(connect, sql);
			if (pRs.next()) {
				v = this.loadRecordFromRs(pRs);
				result = v.getId_grade();

			}

			return result;

		} finally {
			// we release the resources
			DBConnection.releaseConnections(connect);

		}
	}

	/**
	 * Delete a Grade from the grades table.
	 *
	 * @param pGrade The Grade to remove.
	 *
	 * @throws FieldRequiredException
	 * @throws EntityNotFoundException
	 * @throws SQLException
	 * @throws ConnectionException
	 * @throws ValueInvalidException
	 * 
	 */
	public synchronized void remove(Grade pGrade) throws ConnectionException, SQLException, EntityNotFoundException,
			FieldRequiredException, ValueInvalidException {
		Connection connect = null;

		try {
			// ManagerTeaching.getInstance().teachingOnDeleteCascade(pTeaching);
			connect = DBConnection.getConnection();
			// Let's prepare the SQL string
			String sql = "DELETE FROM " + ManagerGrade.TABELLA_Grade + " WHERE id_grades = "
					+ Utility.eNull(pGrade.getId_grade());

			Utility.performOperation(connect, sql);
		} finally {
			// release resources
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
	public synchronized Grade getGradePerId(int pId)
			throws ConnectionException, SQLException, EntityNotFoundException, ValueInvalidException {
		Grade result = null;
		Connection connect = null;
		try {

			if (pId <= 0)
				throw new EntityNotFoundException("Couldn't find the Grade!");

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// Let's prepare the SQL string
			String sql = "SELECT * FROM " + ManagerGrade.TABELLA_Grade + " WHERE id_grades = " + Utility.eNull(pId);

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
	 * Returns the ID of the teaching corresponding to the ID of the Grade passed as
	 * parameter.
	 *
	 * @param pId The ID of the Grade.
	 * @return Returns the teaching id.
	 *
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 */
	public synchronized String getTeachingIdPerGradeId(int pId)
			throws EntityNotFoundException, ConnectionException, SQLException {
		String result;
		Connection connect = null;
		try {
			// If the ID has not been provided, we will return an error code
			if (pId <= 0)
				throw new EntityNotFoundException("Couldn't find the Grade!");

			/*
			 * We prepare the SQL string to retrieve the information corrispondenti all'id
			 * dell'teaching passato come parametro
			 */
			String tSql = "SELECT id_teaching FROM " + ManagerGrade.TABELLA_Grade

					+ " WHERE id_grades = " + Utility.eNull(pId);

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			if (tRs.next())
				result = tRs.getString("id_teaching");
			else
				throw new EntityNotFoundException("Couldn't find the Grade!");

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Returns the set of all grades present in the database.
	 *
	 * @return Returns a collection of grades.
	 *
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws ValueInvalidException
	 * @throws EntityNotFoundException
	 */
	public synchronized Collection<Grade> getGrade()
			throws ConnectionException, SQLException, ValueInvalidException, EntityNotFoundException {
		Collection<Grade> result = null;
		Connection connect = null;

		try {
			// Let's prepare the SQL string
			String sql = "SELECT * FROM " + ManagerGrade.TABELLA_Grade + " ORDER BY id_grades";

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
	 * Returns the set of grades associated with the user corresponding to the past id
	 * as a parameter.
	 *
	 * @param pId The user's id.
	 * @return Returns a collection of grades.
	 *
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	public synchronized Collection<Grade> getGradePerUserId(int pId)
			throws EntityNotFoundException, ConnectionException, SQLException, ValueInvalidException {

		Collection<Grade> result = null;
		Connection connect = null;

		if (pId <= 0)
			throw new EntityNotFoundException("specify the user");

		try {
			// Let's prepare the SQL string
			String sql = "SELECT " + ManagerGrade.TABELLA_Grade + ".* FROM " + ManagerGrade.TABELLA_Grade + " WHERE ("
					+ ManagerGrade.TABELLA_Grade + ".id_user = " + Utility.eNull(pId) + ")" + " ORDER BY id_user";

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
	 * Returns the set of grades associated with the user corresponding to the past id
	 * as a parameter.
	 *
	 * @param pId The user's id.
	 * @return Returns a collection of grades.
	 *
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	public synchronized Collection<Grade> getGradePerUserIdYearRotation(int pId, int pYear, int pRotation)
			throws EntityNotFoundException, ConnectionException, SQLException, ValueInvalidException {

		Collection<Grade> result = null;
		Connection connect = null;

		if (pId <= 0)
			throw new EntityNotFoundException("specify the user");

		try {
			// Let's prepare the SQL string
			String sql = "SELECT " + ManagerGrade.TABELLA_Grade + ".* FROM " + ManagerGrade.TABELLA_Grade + " WHERE ("
					+ ManagerGrade.TABELLA_Grade + ".id_user = " + Utility.eNull(pId) + " AND " + ManagerGrade.TABELLA_Grade
					+ ".accademicYear = " + Utility.eNull(pYear) + " AND " + ManagerGrade.TABELLA_Grade + ".turn = "
					+ Utility.eNull(pRotation) + ")" + " ORDER BY id_user";

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

	public synchronized void removeGradePerUserIdYearRotation(int pId, int pYear, int pRotation)
			throws EntityNotFoundException, ConnectionException, SQLException, ValueInvalidException {

		Connection connect = null;

		if (pId <= 0)
			throw new EntityNotFoundException("specify the user");

		try {
			// Let's prepare the SQL string
			String sql = "DELETE " + ManagerGrade.TABELLA_Grade + " FROM " + ManagerGrade.TABELLA_Grade + " WHERE ("
					+ ManagerGrade.TABELLA_Grade + ".id_user=" + Utility.eNull(pId) + " AND " + ManagerGrade.TABELLA_Grade
					+ ".AccademicYear=" + Utility.eNull(pYear) + " AND " + ManagerGrade.TABELLA_Grade + ".turn="
					+ Utility.eNull(pRotation) + ")";

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			Utility.performOperation(connect, sql);

		} finally {
			// release resources
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Allows reading of a record from the ResultSet.
	 *
	 * @param pRs The result of the query.
	 * @return Returns the Grade read.
	 *
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	private Grade loadRecordFromRs(ResultSet pRs) throws SQLException, ValueInvalidException {
		Grade grades = new Grade();
		grades.setId_grade(pRs.getInt(("id_grades")));
		grades.setId_user(pRs.getInt("id_user"));
		grades.setTeaching(pRs.getInt("id_teaching"));
		grades.setWriting(pRs.getInt("written"));
		grades.setOral(pRs.getInt("oral"));
		grades.setLaboratory(pRs.getInt("laboratory"));
		grades.setAcademicYear(pRs.getInt("AccademicYear"));
		grades.setrotation(pRs.getInt("turn"));

		return grades;
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
	private Collection<Grade> loadRecordsFromRs(ResultSet pRs) throws SQLException, ValueInvalidException {
		Collection<Grade> result = new Vector<Grade>();
		do {
			result.add(loadRecordFromRs(pRs));
		} while (pRs.next());
		return result;
	}

}
