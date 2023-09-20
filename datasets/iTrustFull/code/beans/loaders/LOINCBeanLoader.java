package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.LOINCbean;

/**
 * A loader for LOINCBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class LOINCBeanLoader implements BeanLoader<LOINCbean> {
	
	public List<LOINCbean> loadList(ResultSet rs) throws SQLException {
		ArrayList<LOINCbean> list = new ArrayList<LOINCbean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public LOINCbean loadSingle(ResultSet rs) throws SQLException {
		LOINCbean LOINC = new LOINCbean();
		LOINC.setLabProcedureCode(rs.getString("LaboratoryProcedureCode"));
		LOINC.setComponent(rs.getString("Component"));
		LOINC.setKindOfProperty(rs.getString("KindOfProperty"));
		LOINC.setTimeAspect(rs.getString("TimeAspect"));
		LOINC.setSystem(rs.getString("System"));
		LOINC.setScaleType(rs.getString("ScaleType"));
		LOINC.setMethodType(rs.getString("MethodType"));
		return LOINC;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, LOINCbean bean) throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}

}
