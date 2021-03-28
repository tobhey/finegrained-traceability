package edu.ncsu.csc.itrust.validate;

import edu.ncsu.csc.itrust.action.EditOfficeVisitAction;
import edu.ncsu.csc.itrust.beans.AllergyBean;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;

/**
 * Validator used to validate adding a new allergy in {@link EditOfficeVisitAction}
 * 
 * @author Andy
 * 
 */
public class AllergyBeanValidator extends BeanValidator<AllergyBean> {
	/**
	 * The default constructor.
	 */
	public AllergyBeanValidator() {
	}
	
	/**
	 * Performs the act of validating the bean in question, which varies depending on the
	 * type of validator.  If the validation does not succeed, a {@link FormValidationException} is thrown.
	 * 
	 * @param p A bean of the type to be validated.
	 */
	@Override
	public void validate(AllergyBean m) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		errorList.addIfNotNull(checkFormat("Allergy Description", m.getDescription(),
				ValidationFormat.ALLERGY_DESCRIPTION, false));
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}
}
