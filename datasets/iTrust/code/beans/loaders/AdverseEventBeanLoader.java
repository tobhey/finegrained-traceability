package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.AdverseEventBean;

/**
 * A loader for AdverseEventBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class AdverseEventBeanLoader implements BeanLoader<AdverseEventBean> {

	public List<AdverseEventBean> loadList(ResultSet rs) throws SQLException {
		ArrayList<AdverseEventBean> list = new ArrayList<AdverseEventBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public AdverseEventBean loadSingle(ResultSet rs) throws SQLException {
		AdverseEventBean adverseEvent = new AdverseEventBean();
		adverseEvent.setMID(rs.getString("PatientMID"));
		adverseEvent.setDrug(rs.getString("PresImmu"));
		adverseEvent.setDescription(rs.getString("Comment"));
		adverseEvent.setCode(rs.getString("Code"));
		adverseEvent.setDate(rs.getTimestamp("TimeLogged").toString());
		adverseEvent.setId(rs.getInt("id"));
		adverseEvent.setStatus(rs.getString("Status"));
		return adverseEvent;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, AdverseEventBean bean) throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}

}
