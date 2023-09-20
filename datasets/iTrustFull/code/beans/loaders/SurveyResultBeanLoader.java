package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.SurveyResultBean;
/**
 * A loader for SurveyResultBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class SurveyResultBeanLoader implements BeanLoader<SurveyResultBean> {

	public List<SurveyResultBean> loadList(ResultSet rs) throws SQLException {
		ArrayList<SurveyResultBean> list = new ArrayList<SurveyResultBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, SurveyResultBean bean) throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}

	public SurveyResultBean loadSingle(ResultSet rs) throws SQLException {
		SurveyResultBean surveyResult = new SurveyResultBean();
		surveyResult.setHCPMID(rs.getLong("mid"));
		surveyResult.setAvgExamRoomMinutes(rs.getFloat("AvgExamRoomMinutes"));
		surveyResult.setAvgTreatmentSatisfaction(rs.getFloat("AvgTreatmentSatisfation"));
		surveyResult.setAvgVisitSatisfaction(rs.getFloat("AvgVisitSatisfaction"));
		surveyResult.setAvgWaitingRoomMinutes(rs.getFloat("AvgWaitingRoomMinutes"));
		surveyResult.setHCPaddress1(rs.getString("address1"));
		surveyResult.setHCPaddress2(rs.getString("address2"));
		surveyResult.setHCPcity(rs.getString("city"));
		surveyResult.setHCPstate(rs.getString("state"));
		surveyResult.setHCPzip(rs.getString("zip"));
		surveyResult.setHCPhospital(rs.getString("hospitalID"));
		surveyResult.setHCPFirstName(rs.getString("firstName"));
		surveyResult.setHCPLastName(rs.getString("lastName"));
		surveyResult.setHCPspecialty(rs.getString("specialty"));
		surveyResult.setPercentSatisfactionResults(rs.getFloat("PercentSatisfactionResults"));
		
		return surveyResult;
	}

}
