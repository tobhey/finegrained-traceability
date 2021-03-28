package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.DrugInteractionBean;

/**
 * A loader for MedicationBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class DrugInteractionBeanLoader implements BeanLoader<DrugInteractionBean> {
	public DrugInteractionBeanLoader() {
	}

	public List<DrugInteractionBean> loadList(ResultSet rs) throws SQLException {
		ArrayList<DrugInteractionBean> list = new ArrayList<DrugInteractionBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public DrugInteractionBean loadSingle(ResultSet rs) throws SQLException {
		// MedicationBean med = new MedicationBean();
		DrugInteractionBean drugIt = new DrugInteractionBean();
		drugIt.setDescription(rs.getString("Description"));
		drugIt.setFirstDrug(rs.getString("FirstDrug"));
		drugIt.setSecondDrug(rs.getString("SecondDrug"));
		return drugIt;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, DrugInteractionBean bean) throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}
}
