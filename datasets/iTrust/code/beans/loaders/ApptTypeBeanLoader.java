package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.ApptTypeBean;

public class ApptTypeBeanLoader implements BeanLoader<ApptTypeBean> {

	public List<ApptTypeBean> loadList(ResultSet rs) throws SQLException {
		List<ApptTypeBean> list = new ArrayList<ApptTypeBean>();
		while (rs.next())
			list.add(loadSingle(rs));
		return list;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, ApptTypeBean apptType) throws SQLException {
		ps.setString(1, apptType.getName());
		ps.setInt(2, apptType.getDuration());
		return ps;
	}

	public ApptTypeBean loadSingle(ResultSet rs) throws SQLException {
		ApptTypeBean apptType = new ApptTypeBean();
		apptType.setName(rs.getString("appt_type"));
		apptType.setDuration(rs.getInt("duration"));
		return apptType;
	}

}
