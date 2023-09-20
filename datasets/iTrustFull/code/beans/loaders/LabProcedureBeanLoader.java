package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.LabProcedureBean;

/**
 * A loader for LabProcedureBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class LabProcedureBeanLoader implements BeanLoader<LabProcedureBean> {
	
	public List<LabProcedureBean> loadList(ResultSet rs) throws SQLException {
		ArrayList<LabProcedureBean> list = new ArrayList<LabProcedureBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public LabProcedureBean loadSingle(ResultSet rs) throws SQLException {
		LabProcedureBean LabProcedure = new LabProcedureBean();
		LabProcedure.setProcedureID(rs.getLong("LaboratoryProcedureID"));
		LabProcedure.setPid(rs.getLong("PatientMID"));
		LabProcedure.setLoinc(rs.getString("LaboratoryProcedureCode"));
		LabProcedure.setStatus(rs.getString("Status"));
		LabProcedure.setCommentary(rs.getString("Commentary"));
		LabProcedure.setResults(rs.getString("Results"));
		LabProcedure.setOvID(rs.getLong("OfficeVisitID"));
		LabProcedure.setTimestamp(rs.getTimestamp("UpdatedDate"));
		LabProcedure.setRights(rs.getString("Rights"));		
		return LabProcedure;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, LabProcedureBean bean) throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}

}
