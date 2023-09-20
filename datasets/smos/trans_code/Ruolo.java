package smos.bean;

import java.io.Serializable;

/**
 * Class used to model a user's role.
 *
 * @author Bavota Gabriele, Carnevale Filomena.
 * 
 */
public class Role implements Serializable {

	private static final long serialVersionUID = 8833734317107515203L;

	/**
	 * The id of the administrator role
	 */
	public static final int ADMINISTRATOR = 1;

	/**
	 * The id of the role of teacher
	 */
	public static final int DOCENT = 2;

	/**
	 * The role student id
	 */
	public static final int STUDENT = 3;

	/**
	 * The id of the master role
	 */
	public static final int MASTER = 4;

	/**
	 * The id of the role ata
	 */
	public static final int ATA = 5;

	/**
	 * The id of the direction role
	 */
	public static final int DIRECTOR = 6;

}
