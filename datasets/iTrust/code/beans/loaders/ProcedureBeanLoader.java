package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.ProcedureBean;

/**
 * A loader for ProcedureBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class ProcedureBeanLoader implements BeanLoader<ProcedureBean> {
	private boolean loadOVProcedureID;

	public ProcedureBeanLoader() {
		this.loadOVProcedureID = false;
	}

	public ProcedureBeanLoader(boolean loadOVProcedureID) {
		this.loadOVProcedureID = loadOVProcedureID;
	}

	public List<ProcedureBean> loadList(ResultSet rs) throws SQLException {
		ArrayList<ProcedureBean> list = new ArrayList<ProcedureBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public ProcedureBean loadSingle(ResultSet rs) throws SQLException {
		ProcedureBean procedure = new ProcedureBean(rs.getString("Code"));
		procedure.setDescription(rs.getString("Description"));
		procedure.setAttribute(rs.getString("Attribute"));
		if (loadOVProcedureID) {
			procedure.setOvProcedureID(rs.getLong("ID"));
			procedure.setDate(rs.getDate("visitDate"));
		}
		
		return procedure;

	}

	public PreparedStatement loadParameters(PreparedStatement ps, ProcedureBean bean) throws SQLException {
		ps.setString(1, bean.getDescription());
		return ps;
	}
}
