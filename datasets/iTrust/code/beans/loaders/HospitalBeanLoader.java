package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.HospitalBean;

/**
 * A loader for HospitalBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */

public class HospitalBeanLoader implements BeanLoader<HospitalBean> {
	public List<HospitalBean> loadList(ResultSet rs) throws SQLException {
		ArrayList<HospitalBean> list = new ArrayList<HospitalBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public HospitalBean loadSingle(ResultSet rs) throws SQLException {
		// HospitalBean hosp = new HospitalBean();
		HospitalBean hosp = new HospitalBean(rs.getString("HospitalID"), rs.getString("HospitalName"));
		return hosp;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, HospitalBean bean) throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}
}
