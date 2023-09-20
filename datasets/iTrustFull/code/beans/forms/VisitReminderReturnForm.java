package edu.ncsu.csc.itrust.beans.forms;

import java.util.ArrayList;
import edu.ncsu.csc.itrust.beans.VisitFlag;

/**
 * A form to contain data coming from reminding a user about an upcoming office visit.
 * 
 * A form is a bean, kinda. You could say that it's a "form" of a bean :) 
 * Think of a form as a real-life administrative form that you would fill out to get 
 * something done, not necessarily making sense by itself.
 */
public class VisitReminderReturnForm {

	private long hcpID;
	private long patientID;
	private String lastName;
	private String firstName;
	private String phoneNumber;
	private ArrayList<VisitFlag> visitFlags;

	public VisitReminderReturnForm(long hcpID, long patientID, String lastName, String firstName,
			String phone1, String phone2, String phone3) {
		this.hcpID = hcpID;
		this.patientID = patientID;
		this.lastName = lastName;
		this.firstName = firstName;
		setPhoneNumber(phone1, phone2, phone3);
	}

	public long getHcpID() {
		return hcpID;
	}

	public void setHcpID(long hcpID) {
		this.hcpID = hcpID;
	}

	public long getPatientID() {
		return patientID;
	}

	public void setPatientID(long patientID) {
		this.patientID = patientID;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phone1, String phone2, String phone3) {
		this.phoneNumber = phone1 + "-" + phone2 + "-" + phone3;
	}

	public VisitFlag[] getVisitFlags() {
		return visitFlags.toArray(new VisitFlag[visitFlags.size()]);
	}

	public void setVisitFlags(VisitFlag[] visitFlags) {
		this.visitFlags = new ArrayList<VisitFlag>();
		for (VisitFlag flag : visitFlags) {
			this.visitFlags.add(flag);
		}
	}

	public void addVisitFlag(VisitFlag flag) {
		if (null == visitFlags)
			visitFlags = new ArrayList<VisitFlag>();
		visitFlags.add(flag);
	}
}
