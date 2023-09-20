package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.PrescriptionReportBean;

/**
 * A loader for PrescriptionReportBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class PrescriptionReportBeanLoader implements BeanLoader<PrescriptionReportBean> {
	private PrescriptionBeanLoader presLoader;
	private OfficeVisitLoader ovLoader;

	public PrescriptionReportBeanLoader() {
		ovLoader = new OfficeVisitLoader();
		presLoader = new PrescriptionBeanLoader();
	}

	public List<PrescriptionReportBean> loadList(ResultSet rs) throws SQLException {
		ArrayList<PrescriptionReportBean> list = new ArrayList<PrescriptionReportBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public PrescriptionReportBean loadSingle(ResultSet rs) throws SQLException {
		PrescriptionReportBean pres = new PrescriptionReportBean();
		pres.setOfficeVisit(ovLoader.loadSingle(rs));
		pres.setPrescription(presLoader.loadSingle(rs));
		return pres;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, PrescriptionReportBean pres)
			throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}
}
