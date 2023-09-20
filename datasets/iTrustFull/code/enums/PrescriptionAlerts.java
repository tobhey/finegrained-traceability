package edu.ncsu.csc.itrust.enums;

/**
 * Every prescription that should be alerted for emergency reports - in the future, this should be abstracted
 * out to a database table so it can be changed at runtime.
 * 
 * @author Andy
 * 
 */
public enum PrescriptionAlerts {
	Tetracycline("Tetracycline", "009042407"), Prioglitazone("Prioglitazone", "647641512");

	private String name;
	private String NDCode;

	private PrescriptionAlerts(String rName, String rNDCode) {
		this.name = rName;
		this.NDCode = rNDCode;
	}

	public String getName() {
		return name;
	}

	public String getNDCode() {
		return this.NDCode;
	}

	public static boolean isAlert(String code) {
		for (PrescriptionAlerts thisone : values()) {
			if (thisone.getNDCode().equals(code))
				return true;
		}
		return false;
	}
}
