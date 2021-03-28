package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.SurveyResultBean;
import edu.ncsu.csc.itrust.beans.loaders.SurveyResultBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

/**
 * A DAO for handling all Survey results.
 * 
 * DAO stands for Database Access Object. All DAOs are intended to be reflections of the database, that is,
 * one DAO per table in the database (most of the time). For more complex sets of queries, extra DAOs are
 * added. DAOs can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than a factory. All DAOs should be
 * accessed by DAOFactory (@see {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 */
public class SurveyResultDAO {
	private DAOFactory factory;
	private SurveyResultBeanLoader loader;
	
	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public SurveyResultDAO(DAOFactory factory) {
		this.factory = factory;
		this.loader = new SurveyResultBeanLoader();
	}
	
	/**
	 * Returns all the survey results in the database that correspond to a particular zip code.
	 * 
	 * @param zip The zipcode we are interested in as a String.
	 * @param specialty The specialty we are interested in as a String.
	 * @return A java.util.List of SurveyResultBeans.
	 * @throws DBException
	 */
	public List<SurveyResultBean> getSurveyResultsForZip(String zip, String specialty) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;

		StringBuffer sql = new StringBuffer();
		sql.append("select p.mid, p.firstname, p.lastname, p.address1, p.address2, p.city, p.state, p.zip, p.specialty, ");
		sql.append("'na' hospitalID, ");
		sql.append("avg(s.WaitingRoomMinutes) AvgWaitingRoomMinutes, ");
		sql.append("avg(s.ExamRoomMinutes) AvgExamRoomMinutes, ");
		sql.append("avg(s.VisitSatisfaction) AvgVisitSatisfaction, ");
		sql.append("avg(s.TreatmentSatisfaction) AvgTreatmentSatisfation, ");
		sql.append("count(*) / ");
		sql.append("	(select count(*) from personnel p1, officevisits v1 ");
		sql.append("	 where v1.hcpid = p1.mid ");
		sql.append("	 and substr(p1.zip,1,3) = ? ");
		sql.append("	 and p1.mid = p.mid) * 100 PercentSatisfactionResults ");
		sql.append("from ovsurvey s, personnel p, officevisits v ");
		sql.append("where s.visitid = v.id ");
		sql.append("and v.hcpid = p.mid ");
		sql.append("and substr(p.zip,1,3) = ? ");
		if (!specialty.equals(SurveyResultBean.ANY_SPECIALTY))
			sql.append("and specialty = ?");
		sql.append("group by p.mid, p.firstname, p.lastname, p.address1, p.address2, p.city, p.state, p.zip, hospitalID ");
		if (!specialty.equals(SurveyResultBean.ANY_SPECIALTY))
			sql.append(", p.specialty ");
		sql.append("order by p.mid ");
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, zip.substring(0, 3));
			ps.setString(2, zip.substring(0, 3));
			if (!specialty.equals(SurveyResultBean.ANY_SPECIALTY))
				ps.setString(3, specialty);
			return loader.loadList(ps.executeQuery());

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Returns all the survey data associated with a given hospital and a certain specialty.
	 * 
	 * @param hospitalID The unique ID of the iTrust hospital we are querying about.
	 * @param specialty A string representing the specialty we are interested in.
	 * @return A java.util.List of SurveyResultBeans.
	 * @throws DBException
	 */
	public List<SurveyResultBean> getSurveyResultsForHospital(String hospitalID, String specialty) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;

		StringBuffer sql = new StringBuffer();
		sql.append("select p.mid, p.firstname, p.lastname, p.address1, p.address2, p.city, p.state, p.zip, p.specialty, ");
		sql.append("h.hosid hospitalID, ");
		sql.append("avg(s.WaitingRoomMinutes) AvgWaitingRoomMinutes, ");
		sql.append("avg(s.ExamRoomMinutes) AvgExamRoomMinutes, ");
		sql.append("avg(s.VisitSatisfaction) AvgVisitSatisfaction, ");
		sql.append("avg(s.TreatmentSatisfaction) AvgTreatmentSatisfation, ");
		sql.append("count(*) / ");
		sql.append("	(select count(*) from personnel p1, officevisits v1, HCPAssignedHos h1 ");
		sql.append("	 where v1.hcpid = p1.mid ");
		sql.append("	 and v1.hcpid = h1.hcpid ");
		sql.append("	 and h1.hosid = ? ");
		sql.append("	 and p1.mid = p.mid) * 100 PercentSatisfactionResults ");
		sql.append("from ovsurvey s, personnel p, officevisits v, HCPAssignedHos h ");
		sql.append("where s.visitid = v.id ");
		sql.append("and v.hcpid = p.mid ");
		sql.append("and v.hcpid = h.hcpid ");
		sql.append("and h.hosid = ? ");
		if (!specialty.equals(SurveyResultBean.ANY_SPECIALTY))
			sql.append("and p.specialty = ?");
		sql.append("group by p.mid, p.firstname, p.lastname, p.address1, p.address2, p.city, p.state, p.zip, hospitalID ");
		if (!specialty.equals(SurveyResultBean.ANY_SPECIALTY))
			sql.append(", p.specialty ");
		sql.append("order by p.mid ");
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, hospitalID);
			ps.setString(2, hospitalID);
			if (!specialty.equals(SurveyResultBean.ANY_SPECIALTY)) {
				ps.setString(3, specialty);
			}
			return loader.loadList(ps.executeQuery());

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
		
	}
}
