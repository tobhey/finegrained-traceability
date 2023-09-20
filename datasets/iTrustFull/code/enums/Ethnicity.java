package edu.ncsu.csc.itrust.enums;

/**
 * All possible ethnicities represented in iTrust.
 */
public enum Ethnicity {
	Caucasian("Caucasian"), AfricanAmerican("African American"), Hispanic("Hispanic"), AmericanIndian(
			"American Indian"), Asian("Asian"), NotSpecified("Not Specified");
	private String name;

	private Ethnicity(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

	public static Ethnicity parse(String input) {
		for (Ethnicity ethnicity : Ethnicity.values()) {
			if (ethnicity.name.equals(input))
				return ethnicity;
		}
		return NotSpecified;
	}
}
