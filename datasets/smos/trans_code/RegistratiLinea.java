package smos.bean;

import java.io.Serializable;

/**
 * Class modeling a register line
 * 
 * @author Nicola Pisanti
 * @version 1.0
 */
public class RegisterLine implements Serializable {

	private static final long serialVersionUID = -6010085925185873838L;

	private ItemListUsers student;
	private Absence absence;
	private Delay delay;

	public RegisterLine() {
		absence = null;
		delay = null;

	}

	/**
	 * Method that returns the student of this log line
	 *
	 * @return an object of type User representing the student
	 */
	public ItemListUsers getStudent() {
		return student;
	}

	/**
	 * Method that sets the student of this register line
	 *
	 * @param an object of type User that represents the student to be inserted
	 */
	public void setStudent(ItemListUsers student) {
		this.student = student;
	}

	/**
	 * Method that returns the student's absence of this log line
	 *
	 * @return an object of type Absence representing absence, or null if
	 * the student was present
	 */
	public Absence getAbsence() {
		return absence;
	}

	/**
	 * Method that sets the student's absence of this register line
	 *
	 * @param an object of type Absence from set
	 */
	public void setAbsence(Absence absence) {
		this.absence = absence;
	}

	/**
	 * Method that returns the student delay of this log line
	 *
	 * @return an object of type Delay representing the delay, or null if
	 * the student had arrived on time or was absent
	 */
	public Delay getDelay() {
		return delay;
	}

	/**
	 * Method that sets the student delay of this register line
	 *
	 * @param an object of type Delay from set
	 */
	public void setDelay(Delay delay) {
		this.delay = delay;
	}

}
