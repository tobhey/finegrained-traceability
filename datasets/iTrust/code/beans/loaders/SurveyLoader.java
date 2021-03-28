package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import edu.ncsu.csc.itrust.beans.SurveyBean;


/**
 * A loader for SurveyBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class SurveyLoader implements BeanLoader<SurveyBean>{

	public List<SurveyBean> loadList(ResultSet rs) throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}

	public PreparedStatement loadParameters(PreparedStatement ps, SurveyBean bean) throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}

	public SurveyBean loadSingle(ResultSet rs) throws SQLException {
		SurveyBean survey = new SurveyBean();
		survey.setVisitID(rs.getLong("VisitID"));
		survey.setSurveyDate(rs.getTimestamp("SurveyDate"));
		survey.setExamRoomMinutes(rs.getInt("ExamRoomMinutes"));
		survey.setWaitingRoomMinutes(rs.getInt("WaitingRoomMinutes"));
		survey.setTreatmentSatisfaction(rs.getInt("TreatmentSatisfaction"));
		survey.setVisitSatisfaction(rs.getInt("VisitSatisfaction"));

		return survey;
	}

}
