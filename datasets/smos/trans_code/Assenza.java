package smos.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Class that models the absence of a student
 * 
 * @author Nicola Pisanti
 * @version 1.0
 */
public class Absence implements Serializable {

	private static final long serialVersionUID = -8396513309450121449L;

	private int idAbsence;
	private int idUser;
	private Date dateOfAbsence;
	private Integer idExcuse;
	private int academicYear;

	public Absence() {

	}

	/**
	 * Method that returns the ID of the absence
	 * 
	 * @return an integer representing the ID of the absence
	 */
	public int getIdAbsence() {

		return idAbsence;
	}

	/**
	 * Method that sets the ID of the absence
	 * 
	 * @param an integer representing the ID to be set
	 */
	public void setIdAbsence(int pIdAbsence) {

		this.idAbsence = pIdAbsence;
	}

	/**
	 * Method that returns the student ID relating to the absence
	 * 
	 * @return a whole that represents the ID of the absent student
	 */
	public int getIdUser() {
		return idUser;
	}

	/**
	 * Method that sets the student ID relating to the absence
	 * 
	 * @param an integer representing the ID to be see
	 */
	public void setIdUser(int pIdUser) {

		this.idUser = pIdUser;
	}

	/**
	 * Method that returns the absence date
	 * 
	 * @return a string that represents the date of absence
	 */
	public Date getDateOfAbsence() {

		return dateOfAbsence;
	}

	/**
	 * Method that sets the absence date
	 * 
	 * @Param a string with the date to be set
	 */
	public void setDateOfAbsence(Date pDateOfAbsence) {

		this.dateOfAbsence = pDateOfAbsence;
	}

	/**
	 * Method that returns the ID of the justification relating to the absence
	 * 
	 * @return an entire that represents the ID of the justification relating to the
	 *         absence, or null if absence has not been justified
	 */
	public Integer getIdExcuse() {

		return idExcuse;

	}

	/**
	 * Method that sets the ID of the justification relating to the absence
	 * 
	 * @param an integer representing the ID of the justifice to be set
	 */
	public void setIdExcuse(Integer pIdExcuse) {
		this.idExcuse = pIdExcuse;
	}

	/**
	 * Method that returns the academic year relating to the absence
	 * 
	 * @return a whole that represents the year in which the academic year began
	 */
	public int getAcademicYear() {

		return academicYear;
	}

	/**
	 * Method that sets the academic year concerning the absence
	 * 
	 * @param an integer representing the academic year to be set
	 */
	public void setAcademicYear(int pAcademicYear) {
		this.academicYear = pAcademicYear;
	}

}
