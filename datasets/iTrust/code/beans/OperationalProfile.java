package edu.ncsu.csc.itrust.beans;

import java.util.HashMap;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * A bean for storing operational profile data.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class OperationalProfile {
	private int numTotalTransactions = 0;
	private int numPatientTransactions = 0;
	private int numPersonnelTransactions = 0;
	private HashMap<TransactionType, Integer> totalCount;
	private HashMap<TransactionType, Integer> personnelCount;
	private HashMap<TransactionType, Integer> patientCount;

	public OperationalProfile() {
		totalCount = createEmptyMap();
		personnelCount = createEmptyMap();
		patientCount = createEmptyMap();
	}

	private HashMap<TransactionType, Integer> createEmptyMap() {
		HashMap<TransactionType, Integer> map = new HashMap<TransactionType, Integer>(TransactionType
				.values().length);
		for (TransactionType type : TransactionType.values()) {
			map.put(type, 0);
		}
		return map;
	}

	public HashMap<TransactionType, Integer> getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(HashMap<TransactionType, Integer> totalCount) {
		this.totalCount = totalCount;
	}

	public HashMap<TransactionType, Integer> getPersonnelCount() {
		return personnelCount;
	}

	public void setPersonnelCount(HashMap<TransactionType, Integer> personnelCount) {
		this.personnelCount = personnelCount;
	}

	public HashMap<TransactionType, Integer> getPatientCount() {
		return patientCount;
	}

	public void setPatientCount(HashMap<TransactionType, Integer> patientCount) {
		this.patientCount = patientCount;
	}

	public void setNumTotalTransactions(int numTransactions) {
		this.numTotalTransactions = numTransactions;
	}

	public int getNumTotalTransactions() {
		return numTotalTransactions;
	}

	public int getNumPatientTransactions() {
		return numPatientTransactions;
	}

	public void setNumPatientTransactions(int numPatientTransactions) {
		this.numPatientTransactions = numPatientTransactions;
	}

	public int getNumPersonnelTransactions() {
		return numPersonnelTransactions;
	}

	public void setNumPersonnelTransactions(int numPersonnelTransactions) {
		this.numPersonnelTransactions = numPersonnelTransactions;
	}
}
