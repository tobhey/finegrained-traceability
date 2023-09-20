package smos.bean;

import java.io.Serializable;

import java.util.Date;

/**
 * Class that models one excuse for an absence
 * 
 * @author Nicola Pisanti
 * @version 1.0
 * 
 */
public class Excuse implements Serializable {

	private static final long serialVersionUID = -4726381877687167661L;

	private int idExcuse;
	private int IdUser;
	private Date dataExcuse;
	private int academicYear;

	/**
	 * Method that returns the id of the excuse
	 * 
	 * @return an integer representing the id of the excuse
	 */
	public int getIdExcuse() {
		return idExcuse;
	}

	/**
	 * Method that sets the id of the excuse
	 * 
	 * @param an integer representing the id from set
	 */
	public void setIdExcuse(int pIdExcuse) {
		this.idExcuse = pIdExcuse;
	}

	/**
	 * Method returns the student ID related to the excuse
	 * 
	 * @return an integer representing the student's id
	 */
	public int getIdUser() {
		return IdUser;
	}

	/**
	 * Method that sets the student's id relative to the excuse
	 * 
	 * @param an integer representing the id from set
	 */
	public void setIdUser(int pIdUser) {
		this.idUser = pIdUser;
	}

	/**
	 * Method that returns the date on which the absence was justified
	 * 
	 * @return a string representing the justified date
	 */
	public Date getDataExcuse() {
		return dataExcuse;
	}

	/**
	 * Method that sets the date on which the absence was justified
	 * 
	 * @param a string representing the date to be set
	 */
	public void setDataExcuse(Date pDataExcuse) {
		this.dataExcuse = pDataExcuse;
	}

	/**
	 * Method that returns the academic year related to the excuse
	 * 
	 * @return an integer representing the year the academic year started
	 */
	public int getAcademicYear() {
		return academicYear;
	}

	/**
	 * Method that sets the academic year relative to the excuse
	 * 
	 * @param an integer representing the academic year by itself
	 */
	public void setAcademicYear(int pAcademicYear) {
		this.academicYear = pAcademicYear;
	}

}
