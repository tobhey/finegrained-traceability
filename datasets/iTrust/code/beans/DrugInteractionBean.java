package edu.ncsu.csc.itrust.beans;

/**
 * A bean for storing data about a message from one user to another.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class DrugInteractionBean {
	String firstDrug;
	String secondDrug;
	String description;
	/**
	 * @return the firstDrug
	 */
	public String getFirstDrug() {
		return firstDrug;
	}
	/**
	 * @param firstDrug the firstDrug to set
	 */
	public void setFirstDrug(String firstDrug) {
		this.firstDrug = firstDrug;
	}
	/**
	 * @return the secondDrug
	 */
	public String getSecondDrug() {
		return secondDrug;
	}
	/**
	 * @param secondDrug the secondDrug to set
	 */
	public void setSecondDrug(String secondDrug) {
		this.secondDrug = secondDrug;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	

}
