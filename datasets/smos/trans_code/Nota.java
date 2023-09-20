package smos.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Class modeling a note on the register
 * 
 * @author Nicola Pisanti
 * @version 1.0
 */
public class Note implements Serializable {

	private static final long serialVersionUID = 5953926210895315436L;

	private int idNote;
	private int IdUser;
	private Date dataNote;
	private String desciptions;
	private String teacher;
	private int academicYear;

	public Note() {

	}

	/**
	 * Method that returns the note id
	 *
	 * @return an integer representing the note id
	 */
	public int getIdNote() {
		return idNote;
	}

	/**
	 * Method to set the id of the note
	 *
	 * @param an integer representing the new value of the id
	 */
	public void setIdNote(int pIdNote) {
		this.idNote = pIdNote;
	}

	/**
	 * Method that returns the id of the student who received the note
	 *
	 * @return the id of the user who received the note
	 */
	public int getIdUser() {
		return IdUser;
	}

	/**
	 * Method to set the id of the student who received the note
	 *
	 * @param an integer representing the new value of the id
	 */
	public void setIdUser(int pIdUser) {
		this.idUser = pIdUser;
	}

	/**
	 * Method that returns a string representing the date it was on
	 * given the note
	 *
	 * @return a string representing the date of the note
	 */
	public Date getDataNote() {
		return dataNote;
	}

	/**
	 * Method that sets a string representing the date the
	 * Note
	 *
	 * @param the string representing the new date
	 */
	public void setDataNote(Date pDataNote) {
		this.dataNote = pDataNote;
	}

	/**
	 * Method that returns the text of the note
	 *
	 * @return a string representing the note text
	 */
	public String getDescription() {
		return desciptions;
	}

	/**
			* Method that sets the desciptions of the note
*
		* @param a string that contains the desciptions of the note
*/
	public void setDescription (String pDescription) {
		this.desciptions = pDescription;
	}

/**
		* Method that returns the id of the teacher who gave the note
*
		* @return an integer representing the teacher's id
			*/
	public String getTeacher () {
		return teacher;
	}

/**
		* Method that sets the id of the teacher who gave the note
*
		* @param teacher the teacher to set
*/
	public void setTeacher (String pTeacher) {
		this.teacher = pTeacher;
	}

/**
		* Method that returns the current academic year
*
		* @return an integer indicating the start year of the lessons
*/
	public int getAcademicYear () {
		return academicYear;
	}

/**
		* Method that sets the current academic year during the assignment of the note
*
		* @param an integer indicating the start year of the insert lessons
*/
	public void setAcademicYear (int academicYear) {
		this.academicYear = academicYear;
	}

}
