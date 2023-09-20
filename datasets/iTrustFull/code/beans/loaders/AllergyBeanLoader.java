package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.AllergyBean;

/**
 * A loader for AllergyBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class AllergyBeanLoader implements BeanLoader<AllergyBean> {

	public List<AllergyBean> loadList(ResultSet rs) throws SQLException {
		ArrayList<AllergyBean> list = new ArrayList<AllergyBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public AllergyBean loadSingle(ResultSet rs) throws SQLException {
		AllergyBean allergy = new AllergyBean();
		allergy.setId(rs.getLong("ID"));
		allergy.setPatientID(rs.getLong("PatientID"));
		allergy.setDescription(rs.getString("Description"));
		allergy.setFirstFound(rs.getTimestamp("FirstFound"));
		return allergy;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, AllergyBean bean) throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}

}
