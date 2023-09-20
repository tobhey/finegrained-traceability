package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.SurveyBean;
import edu.ncsu.csc.itrust.beans.loaders.SurveyLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

/**
 * A DAO for handling all Survey data.
 * 
 * DAO stands for Database Access Object. All DAOs are intended to be reflections of the database, that is,
 * one DAO per table in the database (most of the time). For more complex sets of queries, extra DAOs are
 * added. DAOs can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than a factory. All DAOs should be
 * accessed by DAOFactory (@see {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 */
public class SurveyDAO {
	private DAOFactory factory;
	private SurveyLoader surveyLoader;

	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public SurveyDAO(DAOFactory factory) {
		this.factory = factory;
		this.surveyLoader = new SurveyLoader();
	}
	
	/**
	 * Insert survey data into database.
	 * @param surveyBean The Bean representing the user's responses which will be inserted.
	 * @param date The date the survey was completed.
	 */
	public void addCompletedSurvey(SurveyBean surveyBean, Date date) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO OVSurvey (VisitID, SurveyDate) VALUES (?,?)");
			ps.setLong(1, surveyBean.getVisitID());
			ps.setTimestamp(2, new java.sql.Timestamp(date.getTime()));
			ps.executeUpdate();
			if (surveyBean.getWaitingRoomMinutes() > 0) {
				ps = conn.prepareStatement("update OVSurvey set WaitingRoomMinutes = ? where VisitID = ?");
				ps.setFloat(1, surveyBean.getWaitingRoomMinutes());
				ps.setLong(2, surveyBean.getVisitID());
				ps.executeUpdate();
			}
			if (surveyBean.getExamRoomMinutes() > 0) {
				ps = conn.prepareStatement("update OVSurvey set ExamRoomMinutes = ? where VisitID = ?");
				ps.setFloat(1, surveyBean.getExamRoomMinutes());
				ps.setLong(2, surveyBean.getVisitID());
				ps.executeUpdate();
			}
			if (surveyBean.getVisitSatisfaction() > 0) {
				ps = conn.prepareStatement("update OVSurvey set VisitSatisfaction = ? where VisitID = ?");
				ps.setFloat(1, surveyBean.getVisitSatisfaction());
				ps.setLong(2, surveyBean.getVisitID());
				ps.executeUpdate();
			}
			if (surveyBean.getTreatmentSatisfaction() > 0) {
				ps = conn.prepareStatement("update OVSurvey set TreatmentSatisfaction = ? where VisitID = ?");
				ps.setFloat(1, surveyBean.getTreatmentSatisfaction());
				ps.setLong(2, surveyBean.getVisitID());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Gets survey data from the database by the office visit id. Creates an instance of an
	 * SurveyBean with data and returns it.
	 * 
	 * @param id The unique ID of the survey in question.
	 * @return A bean containing the SurveyBean.
	 * @throws DBException
	 */
	public SurveyBean getSurveyData(long id) throws DBException {

		Connection conn = null;
		PreparedStatement ps = null;

		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM OVSurvey WHERE VisitID = ?");
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return surveyLoader.loadSingle(rs);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Has this survey been completed?
	 * 
	 * @param visitID The unique ID of the office visit we are wondering about.
	 * @return boolean indicating whether this survey is completed.
	 * @throws DBException
	 */
	public boolean isSurveyCompleted(long visitID) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT count(*) FROM OVSurvey WHERE VisitID = ?");
			ps.setLong(1, visitID);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return (rs.getInt(1) == 0) ? false : true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

}
