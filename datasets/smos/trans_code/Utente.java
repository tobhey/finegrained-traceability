package smos.bean;

import java.io.Serializable;

/**
 * Class used to model a user.
 *
 * 
 */
public class User implements Serializable {

	private static final long serialVersionUID = 7272532316912745508L;

	private int id;
	private String login;
	private String password;
	private String firstName;
	private String surname;
	private int idParent;
	private String cell;
	private String eMail;

	/**
	 * The constructor of the class.
	 */
	public User() {
		this.id = 0;
	}

	/**
	 * @return User login returns.
	 */
	public String getLogin() {
		return this.login;
	}

	/**
	 * Set user login.
	 *
	 * @param pLogin The login from set.
	 * 
	 * @throws ValueInvalidException
	 */
	public void setLogin(String pLogin) throws ValueInvalidException {
		if (pLogin.length() <= 4)
			throw new ValueInvalidException();
		else
			this.login = pLogin;
	}

	/**
	 * @return Returns the user's name.
	 */
	public String getName() {
		return this.surname + " " + this.firstName;
	}

	/**
	 * @return Returns the user's name.
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Set the user's name.
	 * 
	 * @param pFirstName The name from set.
	 */
	public void setFirstName(String pFirstName) {
		this.firstName = pFirstName;
	}

	/**
	 * @return Returns the user's password.
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Set the user's password.
	 *
	 *  @param pPassword The password from set.
	 * 
	 * @throws ValueInvalidException
	 */
	public void setPassword(String pPassword) throws ValueInvalidException {
		if (pPassword.length() <= 4)
			throw new ValueInvalidException();
		else
			this.password = pPassword;
	}

	/**
	 * @return Returns the user's surname.
	 */
	public String getSurname() {
		return this.surname;
	}

	/**
	 * Set the user's surname.
	 * 
	 *  @param pSurname The set surname.
	 */
	public void setSurname(String pSurname) {
		this.surname = pSurname;
	}

	/**
	 * @return Return the user id.
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
	 * Returns a string containing the user's name and surname.
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getFirstName() + " " + this.getSurname();
	}

	/**
	 * @return the eMail
	 */
	public String getEMail() {
		return this.eMail;
	}

	/**
	 * @param pMail the eMail to set
	 */
	public void setEMail(String pMail) {
		this.eMail = pMail;
	}

	/**
	 * @return the cell
	 */
	public String getCell() {
		return this.cell;
	}

	/**
	 * @param cell the cell to set
	 */
	public void setCell(String pCell) {
		this.cell = pCell;
	}

	/**
	 * @return the idParent
	 */
	public int getIdParent() {
		return this.idParent;
	}

	/**
	 * @param idParent the idParent to set
	 */
	public void setIdParent(int pIdParent) {
		this.idParent = pIdParent;
	}

}
