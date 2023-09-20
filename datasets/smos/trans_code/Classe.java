package smos.bean;

import java.io.Serializable;

/**
 * Class used to model a class
 * 
 * @author Nicola Pisanti
 * @version 1.0
 */
public class Class implements Serializable {

	private static final long serialVersionUID = -8295647317972301446L;

	private int idClass; // Class id
	private int idStreetAddress; // Street address id
	private String name; // Class name
	private int academicYear; // Academic year of the class, to be entered in the year of the first semester.

	public Class() {
		this.idStreetAddress = 0;
		this.idClass = 0;

	}

	/**
	 * Method that returns the academic year
	 * 
	 * @return An integer representing the school year of the first semester of the
	 *         class.
	 */
	public int getAcademicYear() {
		return academicYear;
	}

	/**
	 * Method that sets the academic year
	 * 
	 * @param The new academic year to set
	 */
	public void setAcademicYear(int pAcademicYear) {
		this.academicYear = pAcademicYear;
	}

	/**
	 * Method to get the class address ID
	 * 
	 * @return An integer representing the class address ID
	 */
	public int getIdStreetAddress() {
		return idStreetAddress;
	}

	/**
	 * Method that sets the class address ID
	 * 
	 * @param The new ID from set
	 */
	public void setIdStreetAddress(int pIdStreetAddress) {
		this.idStreetAddress = pIdStreetAddress;
	}

	/**
	 * Method that returns the ID of the class
	 * 
	 * @return An integer representing the class ID
	 */
	public int getIdClass() {
		return idClass;
	}

	/**
	 * Method that sets the class ID
	 * 
	 * @param The new ID from set
	 */
	public void setIdClass(int pIdClass) {
		this.idClass = pIdClass;
	}

	/**
	 * Method that returns the name of the class
	 * 
	 * @return A string representing the name of the class
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method that sets the name of the class
	 * 
	 * @param The new name from set
	 */
	public void setName(String pName) {
		this.name = pName;
	}

	@Override
	public String toString() {

		return (name + " " + academicYear + " ID: " + idClass);
	}

}
