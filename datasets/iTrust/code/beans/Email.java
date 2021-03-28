package edu.ncsu.csc.itrust.beans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * A bean for storing data about Fake Emails.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class Email {
	private List<String> toList = new ArrayList<String>();
	private String from = "";
	private String subject = "";
	private String body = "";
	private Timestamp timeAdded;

	public List<String> getToList() {
		return toList;
	}

	public void setToList(List<String> toList) {
		this.toList = toList;
	}

	public String getToListStr() {
		String str = "";
		for (String addr : toList) {
			str += addr + ",";
		}
		return str.substring(0, str.length() - 1);
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Timestamp getTimeAdded() {
		return timeAdded;
	}

	public void setTimeAdded(Timestamp timeAdded) {
		this.timeAdded = timeAdded;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj.getClass().equals(this.getClass()) && this.equals((Email) obj);
	}

	@Override
	public int hashCode() {
		assert false : "hashCode not designed";
		return 42; // any arbitrary constant will do
	}

	private boolean equals(Email other) {
		return from.equals(other.from) && subject.equals(other.subject) && body.equals(other.body)
				&& listEquals(toList, other.toList);
	}

	private boolean listEquals(List<String> toList, List<String> otherToList) {
		if (toList.size() != otherToList.size())
			return false;
		for (int i = 0; i < toList.size(); i++) {
			if (!toList.get(i).equals(otherToList.get(i)))
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "FROM: " + from + " TO: " + toList.toString() + " SUBJECT: " + subject + " BODY: " + body;
	}

}
