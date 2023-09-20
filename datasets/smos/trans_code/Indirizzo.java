package smos.bean;

import java.io.Serializable;

/**
 * Class used to model an address.
 *
 * 
 */
public class StreetAddress implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9194626030239503689L;

	private int idStreetAddress;
	private String name;

	/**
	 * The constructor of the class.
	 */
	public StreetAddress() {
		this.idStreetAddress = 0;
	}

	/**
	 * @return Returns the id of the address.
	 */
	public int getIdStreetAddress() {
		return idStreetAddress;
	}

	/**
	 * Set the id of the address.
	 * 
	 * @param pIdStreetAddress the id from set.
	 */
	public void setIdStreetAddress(int pIdStreetAddress) {
		this.idStreetAddress = pIdStreetAddress;
	}

	/**
	 * @return Returns the name of the address.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the address.
	 * 
	 * @param pName The name from set.
	 */
	public void setName(String pName) {
		this.name = pName;
	}

}
