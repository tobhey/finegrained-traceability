package edu.ncsu.csc.itrust.validate;

import edu.ncsu.csc.itrust.beans.AdverseEventBean;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;

/**
 * Validates an adverse event bean, from {@link AdverseEventAction}
 * 
 */
public class AdverseEventValidator extends BeanValidator<AdverseEventBean> {
	/**
	 * The default constructor.
	 */
	public AdverseEventValidator() {
	}

	/**
	 * Performs the act of validating the bean in question, which varies depending on the
	 * type of validator.  If the validation does not succeed, a {@link FormValidationException} is thrown.
	 * 
	 * @param d A bean of the type to be validated.
	 */
	@Override
	public void validate(AdverseEventBean b) throws FormValidationException {
		ErrorList errorList = new ErrorList();errorList.addIfNotNull(checkFormat("comment", b.getDescription(), ValidationFormat.ADVERSE_EVENT_COMMENTS,false));
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}
}
