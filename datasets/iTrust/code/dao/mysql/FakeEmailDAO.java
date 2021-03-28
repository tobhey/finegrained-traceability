package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.Email;
import edu.ncsu.csc.itrust.beans.loaders.EmailBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

/**
 * 
 * DAO stands for Database Access Object. All DAOs are intended to be reflections of the database, that is,
 * one DAO per table in the database (most of the time). For more complex sets of queries, extra DAOs are
 * added. DAOs can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than a factory. All DAOs should be
 * accessed by DAOFactory (@see {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 * 
 * @author Andy
 * 
 */
public class FakeEmailDAO {
	private DAOFactory factory;
	private EmailBeanLoader emailBeanLoader = new EmailBeanLoader();

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public FakeEmailDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Return all emails that have been "sent" (inserted into the database)
	 * 
	 * @return A java.util.List of Email objects representing fake e-mails.
	 * @throws DBException
	 */
	public List<Email> getAllEmails() throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM FakeEmail ORDER BY AddedDate DESC");
			ResultSet rs = ps.executeQuery();
			return emailBeanLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Return all emails that a person has sent
	 * 
	 * @param email The "From" email address as a string.
	 * @return A java.util.List of fake emails.
	 * @throws DBException
	 */
	public List<Email> getEmailsByPerson(String email) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM FakeEmail WHERE ToAddr LIKE ? ORDER BY AddedDate DESC");
			ps.setString(1, "%" + email + "%");
			ResultSet rs = ps.executeQuery();
			return emailBeanLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * "Send" an email, which just inserts it into the database.
	 * 
	 * @param email The Email object to insert.
	 * @throws DBException
	 */
	public void sendEmailRecord(Email email) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO FakeEmail (ToAddr, FromAddr, Subject, Body) "
					+ "VALUES (?,?,?,?)");
			emailBeanLoader.loadParameters(ps, email);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

	/**
	 * Returns a list of emails that have the given string as a substring of the body
	 * @param string The string to search within the body.
	 * @return A java.util.List of fake emails.
	 * @throws DBException 
	 */
	public List<Email> getEmailWithBody(String bodySubstring) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM FakeEmail WHERE Instr(Body,?)>0 ORDER BY AddedDate DESC");
			ps.setString(1, bodySubstring);
			ResultSet rs = ps.executeQuery();
			return emailBeanLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

}
