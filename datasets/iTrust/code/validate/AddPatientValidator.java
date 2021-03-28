package edu.ncsu.csc.itrust.validate;

import edu.ncsu.csc.itrust.action.AddPatientAction;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;

/**
 * The validator used by {@link AddPatientAction}. Only checks first name, last name, and email
 * 
 * @author Andy
 * 
 */
public class AddPatientValidator extends BeanValidator<PatientBean> {
	/**
	 * The default constructor.
	 */
	public AddPatientValidator() {
	}

	/**
	 * Performs the act of validating the bean in question, which varies depending on the
	 * type of validator.  If the validation does not succeed, a {@link FormValidationException} is thrown.
	 * 
	 * @param p A bean of the type to be validated.
	 */
	@Override
	public void validate(PatientBean p) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		errorList.addIfNotNull(checkFormat("First name", p.getFirstName(), ValidationFormat.NAME, false));
		errorList.addIfNotNull(checkFormat("Last name", p.getLastName(), ValidationFormat.NAME, false));
		errorList.addIfNotNull(checkFormat("Email", p.getEmail(), ValidationFormat.EMAIL, false));
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}
	
}
