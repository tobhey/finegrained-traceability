package edu.ncsu.csc.itrust.exception;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Used by the validators to create a list of error messages.
 * 
 * @author Andy
 * 
 */
public class ErrorList implements Iterable<String> {
	private List<String> errorList;

	public ErrorList() {
		errorList = new ArrayList<String>();
	}

	/**
	 * Adds a message to the list if it's not a Java null or empty string.
	 * 
	 * @param errorMessage
	 */
	public void addIfNotNull(String errorMessage) {
		if (errorMessage != null && !"".equals(errorMessage))
			errorList.add(errorMessage);
	}

	/**
	 * Returns the list of error messages
	 * 
	 * @return
	 */
	public List<String> getMessageList() {
		return errorList;
	}

	/**
	 * Returns true if the list has any errors
	 * 
	 * @return
	 */
	public boolean hasErrors() {
		return errorList.size() != 0;
	}

	@Override
	public String toString() {
		return errorList.toString();
	}

	public Iterator<String> iterator() {
		return errorList.iterator();
	}
}
