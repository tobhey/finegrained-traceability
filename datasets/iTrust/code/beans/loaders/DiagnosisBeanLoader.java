package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.DiagnosisBean;

/**
 * A loader for DiagnosisBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency.
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader} 
 */

public class DiagnosisBeanLoader implements BeanLoader<DiagnosisBean> {
	private boolean loadOVDiagnosisID = false;

	public DiagnosisBeanLoader() {
		loadOVDiagnosisID = false;
	}

	public DiagnosisBeanLoader(boolean loadOVDiagnosisID) {
		this.loadOVDiagnosisID = loadOVDiagnosisID;
	}

	public List<DiagnosisBean> loadList(ResultSet rs) throws SQLException {
		ArrayList<DiagnosisBean> list = new ArrayList<DiagnosisBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public DiagnosisBean loadSingle(ResultSet rs) throws SQLException {
		
		DiagnosisBean diag = new DiagnosisBean(rs.getString("Code"), rs.getString("Description"), rs.getString("Chronic"));
		if (loadOVDiagnosisID) {
			diag.setOvDiagnosisID(rs.getInt("ID"));
			diag.setVisitID(rs.getLong("VisitID"));
		}
		return diag;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, DiagnosisBean bean) throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}
}
