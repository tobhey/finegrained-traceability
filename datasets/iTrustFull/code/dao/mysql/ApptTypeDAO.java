package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.ApptTypeBean;
import edu.ncsu.csc.itrust.beans.loaders.ApptTypeBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

public class ApptTypeDAO {
	private DAOFactory factory;
	private ApptTypeBeanLoader atLoader;
	
	public ApptTypeDAO(DAOFactory factory) {
		this.factory = factory;
		this.atLoader = new ApptTypeBeanLoader();
	}
	
	public List<ApptTypeBean> getApptTypes() throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;

		conn = factory.getConnection();
		ps = conn.prepareStatement("SELECT * FROM appointmenttype");
		ResultSet rs = ps.executeQuery();
		
		List<ApptTypeBean> atList = this.atLoader.loadList(rs);
		DBUtil.closeConnection(conn, ps);

		return atList;
	}
	
	public boolean addApptType(ApptTypeBean apptType) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;

		conn = factory.getConnection();
		ps = conn.prepareStatement(
				"INSERT INTO appointmenttype (appt_type, duration) "
			  + "VALUES (?, ?)");
		this.atLoader.loadParameters(ps, apptType);
		int x = ps.executeUpdate();

		DBUtil.closeConnection(conn, ps);
		
		if(x > 0)
			return true;
		else
			return false;
	}
	
	public boolean editApptType(ApptTypeBean apptType) throws SQLException, DBException {
		Connection conn = null;
		PreparedStatement ps = null;

		conn = factory.getConnection();
		ps = conn.prepareStatement("UPDATE appointmenttype SET duration=? WHERE appt_type=?");
		ps.setInt(1, apptType.getDuration());
		ps.setString(2, apptType.getName());
		int x = ps.executeUpdate();

		DBUtil.closeConnection(conn, ps);
		
		if(x > 0)
			return true;
		else
			return false;
	}
}
