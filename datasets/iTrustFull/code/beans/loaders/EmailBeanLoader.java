package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.ncsu.csc.itrust.beans.Email;

/**
 * A loader for Fake Emails.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class EmailBeanLoader implements BeanLoader<Email> {

	public List<Email> loadList(ResultSet rs) throws SQLException {
		List<Email> list = new ArrayList<Email>();
		while (rs.next())
			list.add(loadSingle(rs));
		return list;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, Email email) throws SQLException {
		ps.setString(1, email.getToListStr());
		ps.setString(2, email.getFrom());
		ps.setString(3, email.getSubject());
		ps.setString(4, email.getBody());
		return ps;
	}

	public Email loadSingle(ResultSet rs) throws SQLException {
		Email email = new Email();
		email.setFrom(rs.getString("FromAddr"));
		email.setToList(Arrays.asList(rs.getString("ToAddr").split(",")));
		email.setBody(rs.getString("Body"));
		email.setSubject(rs.getString("Subject"));
		email.setTimeAdded(rs.getTimestamp("AddedDate"));
		return email;
	}

}
