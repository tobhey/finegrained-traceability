package edu.ncsu.csc.itrust.enums;

/**
 * All possible blood types
 * 
 * @author Andy
 * 
 */
public enum BloodType {
	APos("A+"), ANeg("A-"), BPos("B+"), BNeg("B-"), ABPos("AB+"), ABNeg("AB-"), OPos("O+"), ONeg("O-"), NS(
			"N/S");
	private String name;

	private BloodType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

	public static BloodType parse(String bloodTypeStr) {
		for (BloodType type : BloodType.values()) {
			if (type.getName().equals(bloodTypeStr)) {
				return type;
			}
		}
		return NS;
	}
}
