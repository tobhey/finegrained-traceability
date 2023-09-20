package edu.ncsu.csc.itrust.beans;

import java.sql.Timestamp;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * A bean for storing data about a transaction that occurred within iTrust.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class TransactionBean {
	private long transactionID;
	private long loggedInMID;
	private long secondaryMID;
	private TransactionType transactionType;
	private Timestamp timeLogged;
	private String addedInfo;
	private String role;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAddedInfo() {
		return addedInfo;
	}

	public void setAddedInfo(String addedInfo) {
		this.addedInfo = addedInfo;
	}

	public long getLoggedInMID() {
		return loggedInMID;
	}

	public void setLoggedInMID(long loggedInMID) {
		this.loggedInMID = loggedInMID;
	}

	public long getSecondaryMID() {
		return secondaryMID;
	}

	public void setSecondaryMID(long secondaryMID) {
		this.secondaryMID = secondaryMID;
	}

	public Timestamp getTimeLogged() {
		return timeLogged;
	}

	public void setTimeLogged(Timestamp timeLogged) {
		this.timeLogged = timeLogged;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType tranactionType) {
		this.transactionType = tranactionType;
	}

	public long getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(long transactionID) {
		this.transactionID = transactionID;
	}
}
