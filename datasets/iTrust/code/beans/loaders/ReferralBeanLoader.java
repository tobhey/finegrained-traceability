package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.ReferralBean;
import edu.ncsu.csc.itrust.beans.ReferralBean.ReferralStatus;

/**
 * A loader for ReferralBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class ReferralBeanLoader implements BeanLoader<ReferralBean> {

	public ReferralBeanLoader() {

	}

	public List<ReferralBean> loadList(ResultSet rs) throws SQLException {
		ArrayList<ReferralBean> list = new ArrayList<ReferralBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public ReferralBean loadSingle(ResultSet rs) throws SQLException {
		ReferralBean ref = new ReferralBean();
		// ERIC: fixed to correct for the mysql database bug plaguing java 1.5
		ref.setId((long) rs.getInt("id"));
		//ref.setId(rs.getLong("id"));
		ref.setSenderID(rs.getLong("SenderID"));
		ref.setReceiverID(rs.getLong("ReceiverID"));
		ref.setPatientID((long) rs.getInt("PatientID"));
		ref.setReferralDetails(rs.getString("ReferralDetails"));
		ref.setConsultationDetails(rs.getString("ConsultationDetails"));
		
		if (rs.getString("Status").equals("Pending")) 
			ref.setStatus(ReferralStatus.Pending);
		else if (rs.getString("Status").equals("Finished")) 
			ref.setStatus(ReferralStatus.Finished);
		else 
			ref.setStatus(ReferralStatus.Declined);
		
		
		return ref;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, ReferralBean ref) throws SQLException {
		ps.setLong(1, ref.getPatientID());
		ps.setLong(2, ref.getSenderID());
		ps.setLong(3, ref.getReceiverID());
		ps.setString(4, ref.getReferralDetails());
		ps.setString(5, ref.getConsultationDetails());
		ps.setString(6, ref.getStatus().toString());
		return ps;
	}
}
