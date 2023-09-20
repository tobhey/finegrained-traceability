package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.ApptBean;

public class ApptBeanLoader implements BeanLoader<ApptBean> {

	public List<ApptBean> loadList(ResultSet rs) throws SQLException {
		List<ApptBean> list = new ArrayList<ApptBean>();
		while (rs.next())
			list.add(loadSingle(rs));
		return list;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, ApptBean bean) throws SQLException {
		ps.setString(1, bean.getApptType());
		ps.setLong(2, bean.getPatient());
		ps.setLong(3, bean.getHcp());
		ps.setTimestamp(4, bean.getDate());
		ps.setString(5, bean.getComment());
		return ps;
	}

	public ApptBean loadSingle(ResultSet rs) throws SQLException {
		ApptBean bean = new ApptBean();
		bean.setApptType(rs.getString("appt_type"));
		bean.setPatient(rs.getLong("patient_id"));
		bean.setHcp(rs.getLong("doctor_id"));
		bean.setDate(rs.getTimestamp("sched_date"));
		bean.setComment(rs.getString("comment"));
		return bean;
	}

}
