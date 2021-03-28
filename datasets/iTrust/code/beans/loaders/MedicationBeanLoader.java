package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.MedicationBean;

/**
 * A loader for MedicationBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class MedicationBeanLoader implements BeanLoader<MedicationBean> {
	public MedicationBeanLoader() {
	}

	public List<MedicationBean> loadList(ResultSet rs) throws SQLException {
		ArrayList<MedicationBean> list = new ArrayList<MedicationBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public MedicationBean loadSingle(ResultSet rs) throws SQLException {
		// MedicationBean med = new MedicationBean();
		MedicationBean med = new MedicationBean(rs.getString("Code"));
		med.setDescription(rs.getString("Description"));
		return med;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, MedicationBean bean) throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}
}
