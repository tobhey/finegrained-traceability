package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.forms.VisitReminderReturnForm;

/**
 * A loader for VisitReminderReturnForms.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class VisitReminderReturnFormLoader implements BeanLoader<VisitReminderReturnForm> {

	public List<VisitReminderReturnForm> loadList(ResultSet rs) throws SQLException {
		ArrayList<VisitReminderReturnForm> list = new ArrayList<VisitReminderReturnForm>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public VisitReminderReturnForm loadSingle(ResultSet rs) throws SQLException {
		int i = 0;
		return new VisitReminderReturnForm(rs.getLong(++i),// hcpid
				rs.getLong(++i),// patient ID
				rs.getString(++i),// last name
				rs.getString(++i),// first
				rs.getString(++i),// phone1
				rs.getString(++i),// phone2
				rs.getString(++i));// ,// phone3
		// "diagnosis: " + rs.getString(++i),
		// "last visit: " + rs.getString(++i));
	}

	public PreparedStatement loadParameters(PreparedStatement ps, VisitReminderReturnForm bean)
			throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}

}
