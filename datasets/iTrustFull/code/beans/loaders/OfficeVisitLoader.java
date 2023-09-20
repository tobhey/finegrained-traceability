package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;

/**
 * A loader for OfficeVisitBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class OfficeVisitLoader implements BeanLoader<OfficeVisitBean> {
	public List<OfficeVisitBean> loadList(ResultSet rs) throws SQLException {
		List<OfficeVisitBean> list = new ArrayList<OfficeVisitBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public OfficeVisitBean loadSingle(ResultSet rs) throws SQLException {
		OfficeVisitBean ov = new OfficeVisitBean(rs.getInt("ID"));
		ov.setHcpID(rs.getLong("HCPID"));
		ov.setNotes(rs.getString("Notes"));
		ov.setPatientID(rs.getLong("PatientID"));
		ov.setHospitalID(rs.getString("HospitalID"));
		ov.setVisitDateStr(new SimpleDateFormat("MM/dd/yyyy").format(new Date(rs.getDate("VisitDate").getTime())));

		return ov;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, OfficeVisitBean p) throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}
}
