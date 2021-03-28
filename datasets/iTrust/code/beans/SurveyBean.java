package edu.ncsu.csc.itrust.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A bean for storing survey data associated with an office visit.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class SurveyBean {
	private long visitID;
	private Date surveyDate;
	private int waitingRoomMinutes;
	private int examRoomMinutes;
	private int visitSatisfaction;
	private int treatmentSatisfaction;

	public final static String dateFormat = "MM/dd/yyyy HH:mm";
	
	public void setVisitID(long iD) {
		visitID = iD;
	}
	public long getVisitID() {
		return visitID;
	}

	public void setSurveyDate(Date surveyDate) {
		this.surveyDate = (surveyDate == null ? null : (Date)surveyDate.clone());
	}
	public void setSurveyDateString(String s) {
		try {
			setSurveyDate(new SimpleDateFormat(dateFormat).parse(s));
		}
		catch (ParseException ex) {
			System.out.println(ex.getMessage());
		}
	}
	public Date getSurveyDate() {
		return (surveyDate == null ? null : (Date)surveyDate.clone());
	}
	public String getSurveyDateString() {
		if (surveyDate == null) return "";
		return new SimpleDateFormat(dateFormat).format(getSurveyDate());
	}
	
	public void setWaitingRoomMinutes(int waitingRoomMinutes) {
		this.waitingRoomMinutes = waitingRoomMinutes;
	}
	public int getWaitingRoomMinutes() {
		return waitingRoomMinutes;
	}
	
	public void setExamRoomMinutes(int examRoomMinutes) {
		this.examRoomMinutes = examRoomMinutes;
	}
	public int getExamRoomMinutes() {
		return examRoomMinutes;
	}
	
	public void setVisitSatisfaction(int visitSatisfaction) {
		this.visitSatisfaction = visitSatisfaction;
	}
	public int getVisitSatisfaction() {
		return visitSatisfaction;
	}
	
	public void setTreatmentSatisfaction(int treatmentSatisfaction) {
		this.treatmentSatisfaction = treatmentSatisfaction;
	}
	public int getTreatmentSatisfaction() {
		return treatmentSatisfaction;
	}

	
}
