package edu.ncsu.csc.itrust.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Used by DAOFactory to abstract away different ways of getting our JDBC connection
 * 
 * @author Andy
 * 
 */
public interface IConnectionDriver {
	public Connection getConnection() throws SQLException;
}
