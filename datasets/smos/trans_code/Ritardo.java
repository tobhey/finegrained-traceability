package smos.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Class modeling a student delay entry
 * 
 * @author Nicola Pisanti
 * @version 1.0
 */
public class Delay implements Serializable {

	private static final long serialVersionUID = 78680884161960621L;

	private int idDelay;
	private int IdUser;
	private Date dateDelay;
	private String TimeDelay;
	private int academicYear;

	/**
	 * Method that returns the Delay id
	 *
	 * @return an integer representing the delay id
	 */
	public int getIdDelay() {
		return idDelay;
	}

	/**
	 * Method that sets the Delay id
	 *
	 * @param integer representing the id from set
	 */
	public void setIdDelay(int pIdDelay) {
		this.idDelay = pIdDelay;
	}

	/**
	 * Method that returns the id of the late student
	 *
	 * @return an integer representing the student id
	 */
	public int getIdUser() {
		return IdUser;
	}

	/**
	 * Method that sets the id of the student relative to the delay
	 *
	 * @param an integer representing the id from set
	 */
	public void setIdUser(int pIdUser) {
		this.idUser = pIdUser;
	}

	/**
	 * Method that returns the date of the delay
	 *
	 * @return a string representing the delay date
	 */
	public Date getDateDelay() {
		return dateDelay;
	}

	/**
	 * Method that sets the date of the delay
	 *
	 * @param a string representing the date of the delay
	 */
	public void setDateDelay(Date pDateDelay) {
		this.dateDelay = pDateDelay;
	}

	/**
	 * Method that returns the student's time of entry
	 *
	 * @return a string representing the student's entry time
	 * latecomer
	 */
	public String getTimeDelay() {
		if (this.TimeDelay.length() > 0) {
			return TimeDelay.substring(0, 5);
		} else {
			return this.TimeDelay;
		}
	}

	/**
			* Method that sets the student's entry time
			*
			* @param a string representing the entry time from set
*/
	public void setTimeDelay (String pTimeDelay) {
		this.TimeDelay = pTimeDelay;
	}

/**
		* Method that returns the academic year related to the absence
*
		* @return an integer representing the year the academic year started
*/
	public int getAcademicYear () {
		return academicYear;
	}

/**
		* Method that sets the academic year related to the absence
*
		* @param an integer representing the academic year from set
*/
	public void setAcademicYear(int pAcademicYear) {
		this.academicYear = pAcademicYear;
	}
}
