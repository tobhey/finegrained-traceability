package smos.bean;

import java.io.Serializable;

/**
 * Class used to model a teaching.
 * 
 * @author Giulio D'Amora
 * @version 1.0
 * 
 * 
 * 
 */
public class Teaching implements Serializable {

	private static final long serialVersionUID = 2523612738702790957L;
	private int id_teaching;
	private String name;

	/**
	 * The constructor of the class.
	 */
	public Teaching() {
		this.id_teaching = 0;
	}

	/**
	 * Returns the name of the teaching
	 *
	 * @return Returns the name of the teaching.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set the name of the teaching.
	 *
	 * @param pName The name from set.
	 *
	 * @throws ValueInvalidException
	 */
	public void setName(String pName) throws ValueInvalidException {
		if (pName.length() <= 4)// to verify the test
			throw new ValueInvalidException();
		else
			this.name = pName;
	}

	/**
	 * Return the ID of the teaching.
	 *
	 * @return the id of the teaching.
	 */
	public int getId() {
		return this.id_teaching;
	}

	/**
	 * Set the ID of the teaching.
	 *
	 * @param pId The id from set.
	 */
	public void setId(int pId) {
		this.id_teaching = pId;
	}

}
