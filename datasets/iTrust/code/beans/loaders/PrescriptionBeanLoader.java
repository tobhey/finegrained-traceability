package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.PrescriptionBean;

/**
 * A loader for PrescriptionBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class PrescriptionBeanLoader implements BeanLoader<PrescriptionBean> {
	private MedicationBeanLoader medLoader;

	public PrescriptionBeanLoader() {
		medLoader = new MedicationBeanLoader();
	}

	public List<PrescriptionBean> loadList(ResultSet rs) throws SQLException {
		ArrayList<PrescriptionBean> list = new ArrayList<PrescriptionBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public PrescriptionBean loadSingle(ResultSet rs) throws SQLException {
		PrescriptionBean pres = new PrescriptionBean();
		pres.setId(rs.getLong("ID"));
		pres.setVisitID((long) rs.getInt("VisitID"));
		pres.setStartDateStr(new SimpleDateFormat("MM/dd/yyyy").format(new java.util.Date(rs.getDate(
				"StartDate").getTime())));
		pres.setEndDateStr(new SimpleDateFormat("MM/dd/yyyy").format(new java.util.Date(rs.getDate("EndDate")
				.getTime())));
		pres.setDosage(rs.getInt("Dosage"));
		pres.setInstructions(rs.getString("Instructions"));
		pres.setMedication(medLoader.loadSingle(rs));
		return pres;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, PrescriptionBean pres) throws SQLException {
		ps.setLong(1, pres.getVisitID());
		ps.setString(2, pres.getMedication().getNDCode());
		ps.setDate(3, new java.sql.Date(pres.getStartDate().getTime()));
		ps.setDate(4, new java.sql.Date(pres.getEndDate().getTime()));
		ps.setInt(5, pres.getDosage());
		ps.setString(6, pres.getInstructions());
		return ps;
	}
}
