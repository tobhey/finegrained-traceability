package edu.ncsu.csc.itrust.validate;

import edu.ncsu.csc.itrust.action.SetSecurityQuestionAction;
import edu.ncsu.csc.itrust.beans.SecurityQA;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;

/**
 * Validates the security question and answer. This doesn't follow the same format as the others because this
 * validator is used for the various states of reset password, {@link SetSecurityQuestionAction}
 * 
 * @author Andy
 * 
 */
public class SecurityQAValidator extends BeanValidator<SecurityQA> {
	/**
	 * Performs the act of validating the bean in question, which varies depending on the
	 * type of validator.  If the validation does not succeed, a {@link FormValidationException} is thrown.
	 * 
	 * @param p A bean of the type to be validated.
	 */
	@Override
	public void validate(SecurityQA bean) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		if (null == bean)
			throw new FormValidationException("Null form");

		if (null == bean.getConfirmAnswer())
			throw new FormValidationException("Confirm answer cannot be empty");

		if (!bean.getAnswer().equals(bean.getConfirmAnswer()))
			throw new FormValidationException("Security answers do not match");

		errorList.addIfNotNull(checkFormat("Security Question", bean.getQuestion(),
				ValidationFormat.QUESTION, false));
		errorList.addIfNotNull(checkFormat("Security Answer", bean.getAnswer(), ValidationFormat.ANSWER,
				false));

		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}

}
