package smos.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;
import java.sql.Connection;

/**
 *
 * Address class manager
 *
 */
public class ManagerStreetAddress {

	private static ManagerStreetAddress instance;

	/**
	 * The name of the address table
	 */
	public static final String ADDRESS_TABLE = "address";
	public static final String ADDRESS_TABLE_AVERE_INSEGNAMENTO = "address_has_teaching";

	private ManagerStreetAddress() {
		super();
	}

	/**
	 * Returns the only instance of the existsnte class.
	 *
	 * @return Returns the instance of the class.
	 */
	public static synchronized ManagerStreetAddress getInstance() {
		if (instance == null) {
			instance = new ManagerStreetAddress();
		}
		return instance;
	}

	/**
	 * Check the existence of an address in the database.
	 *
	 * @param pStreetAddress The address to check.
	 * @return Returns true if the address passed as a parameter already exists,
	 *         false otherwise.
	 *
	 * @throws FieldRequiredException
	 * @throws SQLException
	 * @throws FieldRequiredException
	 * @throws ConnectionException
	 * @throws ConnectionException
	 * @throws SQLException
	 */
	public synchronized boolean hasTeaching(Teaching pTeaching, StreetAddress pStreetAddress)
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
			String sql = "SELECT * FROM " + ManagerTeaching.ADDRESS_HAS_TEACHING_TABLE + " WHERE id_teaching = "
					+ Utility.eNull(pTeaching.getId()) + " AND id_address = "
					+ Utility.eNull(pStreetAddress.getIdStreetAddress());
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

	public synchronized boolean exists(StreetAddress pStreetAddress)
			throws FieldRequiredException, ConnectionException, SQLException {
		boolean result = false;
		Connection connect = null;

		if (pStreetAddress.getName() == null)
			throw new FieldRequiredException("Specify the name.");
		try {
			// We get the connection to the database
			connect = DBConnection.getConnection();

			if (connect == null)
				throw new ConnectionException();

			String sql = " SELECT * FROM " + ManagerStreetAddress.ADDRESS_TABLE + " WHERE name = "
					+ Utility.eNull(pStreetAddress.getName());

			ResultSet tRs = Utility.queryOperation(connect, sql);

			if (tRs.next())
				result = true;

			return result;

		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Insert a new address in the address table.
	 *
	 * @param pStreetAddress The address to insert.
	 *
	 * @throws SQLException
	 * @throws ConnectionException
	 * @throws FieldRequiredException
	 * @throws EntityNotFoundException
	 * @throws ValueInvalidException
	 */
	public synchronized void insert(StreetAddress pStreetAddress) throws FieldRequiredException, ConnectionException,
			SQLException, EntityNotFoundException, ValueInvalidException {
		Connection connect = null;
		try {
			// check the required fields
			if (pStreetAddress.getName() == null)
				throw new FieldRequiredException("Specify the name field");

			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();
			// We prepare the Sql string
			String sql = "INSERT INTO " + ManagerStreetAddress.ADDRESS_TABLE + " (name) " + "VALUES ("
					+ Utility.eNull(pStreetAddress.getName()) + ")";

			Utility.performOperation(connect, sql);

			pStreetAddress
					.setIdStreetAddress(Utility.getMaximumValue("id_address", ManagerStreetAddress.ADDRESS_TABLE));

		} finally {
			// release resources

			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Delete an address from the address table.
	 *
	 * @param pStreetAddress The address to remove.
	 *
	 * @throws FieldRequiredException
	 * @throws EntityNotFoundException
	 * @throws SQLException
	 * @throws ConnectionException
	 * @throws ValueInvalidException
	 * 
	 */
	public synchronized void delete(StreetAddress pStreetAddress) throws ConnectionException, SQLException,
			EntityNotFoundException, FieldRequiredException, ValueInvalidException {
		Connection connect = null;

		try {
			// ManagerAddress.getInstance().AddressOnDeleteCascade(pStreetAddress);
			connect = DBConnection.getConnection();
			// Let's prepare the SQL string
			String sql = "DELETE FROM " + ManagerStreetAddress.ADDRESS_TABLE + " WHERE id_address = "
					+ Utility.eNull(pStreetAddress.getIdStreetAddress());

			Utility.performOperation(connect, sql);
		} finally {
			// release resources
			DBConnection.releaseConnections(connect);
		}
	}

	public synchronized void assignTeachingAsStreetAddress(StreetAddress pStreetAddress, Teaching pTeaching)
			throws ConnectionException, SQLException, EntityNotFoundException, FieldRequiredException,
			ValueInvalidException, DuplicateEntityException {
		Connection connect = null;
		ManagerStreetAddress managerAddress = ManagerStreetAddress.getInstance();
		if (managerAddress.hasTeaching(pTeaching, pStreetAddress))
			throw new DuplicateEntityException("This address already has this teaching associated with it");

		try {
			// ManagerAddress.getInstance().AddressOnDeleteCascade(pStreetAddress);
			connect = DBConnection.getConnection();
			// Let's prepare the SQL string
			String sql = "INSERT INTO " + ManagerStreetAddress.ADDRESS_TABLE_AVERE_INSEGNAMENTO
					+ " (id_address, id_teaching) " + " VALUES( " + Utility.eNull(pStreetAddress.getIdStreetAddress())
					+ " , " + Utility.eNull(pTeaching.getId()) + " )";

			Utility.performOperation(connect, sql);
		} finally {
			// release resources
			DBConnection.releaseConnections(connect);
		}
	}

	public synchronized void removeTeachingAsStreetAddress(StreetAddress pStreetAddress, Teaching pTeaching)
			throws ConnectionException, SQLException, EntityNotFoundException, FieldRequiredException,
			ValueInvalidException {
		Connection connect = null;
		ManagerStreetAddress managerAddress = ManagerStreetAddress.getInstance();
		if (!managerAddress.hasTeaching(pTeaching, pStreetAddress))
			throw new EntityNotFoundException("This address does not contain the teaching to be removed");

		try {
			// ManagerAddress.getInstance().AddressOnDeleteCascade(pStreetAddress);
			connect = DBConnection.getConnection();
			// Let's prepare the SQL string
			String sql = "DELETE FROM " + ManagerStreetAddress.ADDRESS_TABLE_AVERE_INSEGNAMENTO + " WHERE id_address= "
					+ Utility.eNull(pStreetAddress.getIdStreetAddress()) + " AND id_teaching = "
					+ Utility.eNull(pTeaching.getId());

			Utility.performOperation(connect, sql);
		} finally {
			// release resources
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Returns the address id passed as a parameter.
	 *
	 * @param pStreetAddress The address whose id is required.
	 * @return Returns the address id passed as a parameter.
	 *
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 */
	public synchronized int getStreetAddressId(StreetAddress pStreetAddress)
			throws EntityNotFoundException, ConnectionException, SQLException {
		int result = 0;
		Connection connect = null;
		try {
			if (pStreetAddress == null)
				throw new EntityNotFoundException("The address could not be found!");

			/*
			 * We prepare the SQL string to retrieve the corresponding information to the id
			 * of the address passed as a parameter.
			 */
			String tSql = "SELECT id_address FROM " + ManagerStreetAddress.ADDRESS_TABLE + " WHERE name = "
					+ Utility.eNull(pStreetAddress.getName());

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			if (tRs.next())
				result = tRs.getInt("id_address");

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Returns the address corresponding to the id passed as a parameter.
	 *
	 * @param pIdStreetAddress The id of the address.
	 * @return Returns the address associated with the id passed as a parameter.
	 *
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 * @throws ValueInvalidException
	 */
	public synchronized StreetAddress getStreetAddressPerId(int pIdStreetAddress)
			throws ConnectionException, SQLException, EntityNotFoundException, ValueInvalidException {
		StreetAddress result = null;
		Connection connect = null;
		try {

			if (pIdStreetAddress <= 0)
				throw new EntityNotFoundException("The address could not be found!");

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// Let's prepare the SQL string
			String sql = "SELECT * FROM " + ManagerStreetAddress.ADDRESS_TABLE + " WHERE id_address = "
					+ Utility.eNull(pIdStreetAddress);

			// We send the Query to the DataBase
			ResultSet pRs = Utility.queryOperation(connect, sql);

			if (pRs.next())
				result = this.loadRecordFromRs(pRs);
			else
				throw new EntityNotFoundException("Unable to find user!");

			return result;
		} finally {
			// release resources
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Returns the set of all addresses in the database.
	 *
	 * @return Returns a collection of addresses.
	 *
	 * @throws ConnectionException
	 * @throws EntityNotFoundException
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	public synchronized Collection<StreetAddress> getStreetAddressElenco()
			throws ConnectionException, EntityNotFoundException, SQLException, ValueInvalidException {
		Connection connect = null;
		Collection<StreetAddress> result = new Vector<StreetAddress>();
		;

		try {
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// Let's prepare the SQL string
			String sql = "SELECT * " + " FROM " + ManagerStreetAddress.ADDRESS_TABLE + " ORDER BY id_address";

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, sql);

			if (tRs.next())
				result = this.loadRecordsFromRs(tRs);
			return result;
		} finally {
			// release resources
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Returns the name of the address corresponding to the id passed as a
	 * parameter.
	 *
	 * @param pIdStreetAddress The id of the address.
	 * @return Returns a string containing the name of the address.
	 *
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 */
	public synchronized String getNameStreetAddressPerId(int pIdStreetAddress)
			throws EntityNotFoundException, ConnectionException, SQLException {
		String result;
		Connection connect = null;
		try {
			// If the ID has not been provided, we will return an error code
			if (pIdStreetAddress <= 0)
				throw new EntityNotFoundException("The address could not be found!");

			/*
			 * We prepare the SQL string to retrieve the corresponding information to the
			 * user id passed as a parameter
			 */
			String tSql = "SELECT name FROM " + ManagerStreetAddress.ADDRESS_TABLE + " WHERE id_address = "
					+ Utility.eNull(pIdStreetAddress);

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			if (tRs.next())
				result = tRs.getString("name");
			else
				throw new EntityNotFoundException("The address could not be found!");

			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/**
	 * Returns a collection with the ids of the courses associated with the past id
	 * as a parameter.
	 *
	 * @param pIdStreetAddress The id of the address.
	 * @return Returns a collection with di int
	 *
	 * @throws EntityNotFoundException
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws ValueInvalidException
	 */
	public synchronized Collection<Integer> getTeachingsStreetAddress(int pIdStreetAddress)
			throws EntityNotFoundException, ConnectionException, SQLException, ValueInvalidException {
		Collection<Integer> result;
		Connection connect = null;
		try {
			// If the ID has not been provided, we will return an error code
			if (pIdStreetAddress <= 0)
				throw new EntityNotFoundException("The address could not be found!");

			/*
			 * We prepare the SQL string to retrieve the corresponding information to the
			 * user id passed as a parameter
			 */
			String tSql = "SELECT id_teaching FROM " + ManagerStreetAddress.ADDRESS_TABLE_AVERE_INSEGNAMENTO
					+ " WHERE id_address = " + Utility.eNull(pIdStreetAddress);

			// We get a Connections to the DataBase
			connect = DBConnection.getConnection();
			if (connect == null)
				throw new ConnectionException();

			// We send the Query to the DataBase
			ResultSet tRs = Utility.queryOperation(connect, tSql);

			result = this.loadIntegersFromRs(tRs);
			return result;
		} finally {
			DBConnection.releaseConnections(connect);
		}
	}

	/*
	 * Allows reading of a record from the ResultSet.
	 */
	private StreetAddress loadRecordFromRs(ResultSet pRs) throws SQLException, ValueInvalidException {
		StreetAddress address = new StreetAddress();
		address.setName(pRs.getString("name"));
		address.setIdStreetAddress(pRs.getInt("id_address"));
		return address;
	}

	/*
	 * Allows reading of records from the ResultSet.
	 */
	private Collection<StreetAddress> loadRecordsFromRs(ResultSet pRs) throws SQLException, ValueInvalidException {
		Collection<StreetAddress> result = new Vector<StreetAddress>();
		do {
			result.add(loadRecordFromRs(pRs));
		} while (pRs.next());
		return result;
	}

	private Collection<Integer> loadIntegersFromRs(ResultSet pRs) throws SQLException, ValueInvalidException {
		Collection<Integer> result = new Vector<Integer>();
		while (pRs.next()) {
			result.add(pRs.getInt("id_teaching"));
		}
		return result;
	}

}
