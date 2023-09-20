package edu.ncsu.csc.itrust.validate;

import edu.ncsu.csc.itrust.beans.LabProcedureBean;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;

public class LabProcedureValidator {
	
	/**
	 * Performs the act of validating the bean in question, which varies depending on the
	 * type of validator.  If the validation does not succeed, a {@link FormValidationException} is thrown.
	 * 
	 * @param p A bean of the type to be validated.
	 */
	public void validate(LabProcedureBean b) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		errorList.addIfNotNull(checkFormat("LOINC", b.getLoinc(), ValidationFormat.LOINC, false));
		errorList.addIfNotNull(checkFormat("Commentary", b.getCommentary(), ValidationFormat.COMMENTS, true));
		errorList.addIfNotNull(checkFormat("Results", b.getCommentary(), ValidationFormat.COMMENTS, true));
		errorList.addIfNotNull(checkFormat("Status", b.getStatus(), ValidationFormat.LAB_STATUS, false));
		errorList.addIfNotNull(checkFormat("Rights", b.getRights(), ValidationFormat.LAB_RIGHTS, false));
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}

	protected String checkFormat(String name, String value, ValidationFormat format, boolean isNullable) {
		String errorMessage = name + ": " + format.getDescription();
		if (value == null || "".equals(value))
			return isNullable ? "" : errorMessage;
		if (format.getRegex().matcher(value).matches())
			return "";
		else
			return errorMessage;
	}
}
