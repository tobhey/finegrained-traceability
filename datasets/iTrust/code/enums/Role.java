package edu.ncsu.csc.itrust.enums;

/**
 * The iTrust user roles: Patient, ER, HCP, UAP, PHA, Administrator and Tester.
 * Consult the requirements for the contextual meanings of these individual choices.
 */
public enum Role {
	PATIENT("patient", "Patients"), 
	ER("er", "Personnel"), 
	HCP("hcp", "Personnel"), 
	UAP("uap", "Personnel"), 
	ADMIN("admin", "Personnel"),
	PHA("pha", "Personnel"),
	TESTER("tester", "");
	
	private String userRolesString;
	private String dbTable;

	Role(String userRolesString, String dbTable) {
		this.userRolesString = userRolesString;
		this.dbTable = dbTable;
	}

	public String getDBTable() {
		return dbTable;
	}

	public String getUserRolesString() {
		return userRolesString;
	}

	public static Role parse(String str) {
		for (Role role : values()) {
			if (role.userRolesString.equals(str))
				return role;
		}
		throw new IllegalArgumentException("Role " + str + " does not exist");
	}
}
