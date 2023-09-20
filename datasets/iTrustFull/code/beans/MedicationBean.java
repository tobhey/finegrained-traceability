package edu.ncsu.csc.itrust.beans;

/**
 * A medication is the same thing as an ND code - it's like "Aspirin". A medication is not associated with an
 * office visit; that's a "prescription". See {@link PrescriptionBean}
 * 
 * @author Andy Meneely
 * 
 */
public class MedicationBean {
	private String NDCode = "";
	private String description = "";

	public MedicationBean() {
	}

	public MedicationBean(String code) {
		NDCode = code;
	}

	public MedicationBean(String code, String description) {
		NDCode = code;
		this.description = description;
	}

	/**
	 * Gets the ND Code for this procedure
	 * 
	 * @return The ND Code for this procedure
	 */
	public String getNDCode() {
		return NDCode;
	}

	public void setNDCode(String code) {
		NDCode = code;
	}

	/**
	 * Gets the ND Description for this procedure
	 * 
	 * @return The ND Description for this procedure
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNDCodeFormatted() {
		String code = getNDCode();
		if (code.length() > 5)
			return code.substring(0, 5) + "-" + code.substring(5);
		else
			return code;
	}

	@Override
	public boolean equals(Object other) {
		return (other != null) && this.getClass().equals(other.getClass())
				&& this.equals((MedicationBean) other);
	}

	public int hashCode() {
		assert false : "hashCode not designed";
		return 42; // any arbitrary constant will do
	}

	private boolean equals(MedicationBean other) {
		return description.equals(other.description) && NDCode.equals(other.NDCode);
	}
}
