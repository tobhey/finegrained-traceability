package smos.bean;

import java.io.Serializable;

/**
 * 
 * Class used to model a grade.
 * 
 * @author Luigi Colangelo
 * @version 1.0
 * 
 * 
 */
public class Grade implements Serializable {

	/**
	 * Class used to model a grade
	 * 
	 */
	private static final long serialVersionUID = 3014235634635608576L;
	private int id_grade;
	private int id_user;
	private int teaching;
	private int writing;
	private int oral;
	private int laboratory;
	private int academicYear;
	private int rotation;

	/**
	 * The constructor of the class
	 */
	public Grade() {
		id_grade = 0;
	}

	/**
	 * Method that returns the grade id.
	 *
	 * @return the id of the grade
	 */
	public int getId_grade() {
		return id_grade;
	}

	/**
	 * Method that sets the grade id
	 *
	 * @param pId_grade the id from set
	 */
	public void setId_grade(int pId_grade) {
		this.id_grade = pId_grade;
	}

	/**
	 * Method that returns the id of the user connected to the grade
	 *
	 * @return the user's id
	 */
	public int getId_user() {
		return id_user;
	}

	/**
	 * Method that sets the user id relative to the grade
	 *
	 * @param pId_user the id from set
	 */
	public void setId_user(int pId_user) {
		this.id_user = pId_user;
	}

	/**
	 * method that returns the grade teaching code
	 *
	 * @return the teaching method
	 */
	public int getTeaching() {
		return teaching;
	}

	/**
	 * Method that sets the teaching code relating to the large
	 * @param pTeaching the teaching code
	 */
	public void setTeaching(int pTeaching) {
		this.teaching = pTeaching;
	}

	/**
	 * Method that returns the grade of the script
	 * 
	 * @return the grade of the script
	 */
	public int getWriting() {
		return writing;
	}

	/**
	 * Method that sets the grade of the writing, checking that it is understood
	 * between 0 and 10
	 *
	 * @param pWriting
	 */
	public void setWriting(int pWriting) throws ValueInvalidException {
		if (pWriting < 0 || pWriting > 10)
			throw new ValueInvalidException();
		else
			this.writing = pWriting;
	}

	/**
	 * method that returns the grade of the oral
	 *
	 * @return the grade of the oral
	 */
	public int getOral() {
		return oral;
	}

	/**
	 * Method that sets the grade of the oral, checking that it is between 0
	 * and 10
	 *
	 * @param pOral the grade of the oral from set
	 */
	public void setOral(int pOral) throws ValueInvalidException {
		if (pOral < 0 || pOral > 10)
			throw new ValueInvalidException();
		else
			this.oral = pOral;
	}

	/**
	 * Method that returns the laboratory grade
	 *
	 * @return the laboratory grade
	 */
	public int getLaboratory() {
		return laboratory;
	}

	/**
	 * method that sets the laboratory grade, checking that it is understood
	 * between 0 and 10
	 *
	 * @param pLaboratory the laboratory grade from set
	 */
	public void setLaboratory(int pLaboratory) throws ValueInvalidException {
		if (pLaboratory < 0 || pLaboratory > 10)
			throw new ValueInvalidException();
		else
			this.laboratory = pLaboratory;
	}

	/**
	 * Method that returns the academic year of the grade
	 *
	 * @return the academic year
	 */
	public int getAcademicYear() {
		return academicYear;
	}

	/**
	 * method that sets the academic year of the grade
	 *
	 * @param pAcademicYear
	 */
	public void setAcademicYear(int pAcademicYear) {
		this.academicYear = pAcademicYear;
	}

	/**
	 * Method that returns the quarter of the grade
	 *
	 * @return the semester of grade (0 or 1)
	 */
	public int getRotation() {
		return rotation;
	}

	/**
	 * Method that sets the quarter of the grade
	 *
	 * @param pRotation the semester of the grade from set
	 */
	public void setRotation(int pRotation) {
		this.rotation = pRotation;
	}

	@Override
	public String toString() {
		return ("id grade= " + id_grade + " id user= " + id_user + " id teaching= " + teaching + " writing= " + writing
				+ " oral= " + oral + " laboratory= " + laboratory + " anno accademico= " + academicYear
				+ " quadrimestre= " + rotation);
	}

}
