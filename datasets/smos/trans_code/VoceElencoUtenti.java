package smos.bean;

import java.io.Serializable;

/**
 * Class used to model a user's core information.
 */
public class ItemListUsers implements Serializable {

	private static final long serialVersionUID = 3436931564172045464L;

	private String name;
	private String eMail;
	private int id;

	/**
	 * @return Returns the user's id.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Set user id.
	 *
	 * @param pId The id from set.
	 */
	public void setId(int pId) {
		this.id = pId;
	}

	/**
	 * @return Returns the user's name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set the user's name.
	 *
	 * @param pName The name from set.
	 */
	public void setName(String pName) {
		this.name = pName;
	}

	/**
	 * @return the eMail
	 */
	public String getEMail() {
		return this.eMail;
	}

	/**
	 * @param mail the eMail to set
	 */
	public void setEMail(String pMail) {
		this.eMail = pMail;
	}
}
