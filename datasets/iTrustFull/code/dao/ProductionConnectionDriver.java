package edu.ncsu.csc.itrust.dao;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Produces the JDBC connection from Tomcat's JDBC connection pool (defined in context.xml). Produces and
 * exception when running the unit tests because they're not being run through Tomcat.
 * 
 * @author Andy
 * 
 */
public class ProductionConnectionDriver implements IConnectionDriver {
	public Connection getConnection() throws SQLException {
		try {
			return ((DataSource) (((Context) new InitialContext().lookup("java:comp/env")))
					.lookup("jdbc/itrust")).getConnection();
		} catch (NamingException e) {
			throw new SQLException(("Context Lookup Naming Exception: " + e.getMessage()));
		}
	}
}
