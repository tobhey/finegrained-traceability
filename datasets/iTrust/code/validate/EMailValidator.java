
package edu.ncsu.csc.itrust.validate;

import edu.ncsu.csc.itrust.beans.MessageBean;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;


public class EMailValidator extends BeanValidator<MessageBean>  {
	/**
	 * The default constructor.
	 */
	public EMailValidator(){
		
	}
	/**
	 * Performs the act of validating the bean in question, which varies depending on the
	 * type of validator.  If the validation does not succeed, a {@link FormValidationException} is thrown.
	 * 
	 * @param m A bean of the type to be validated.
	 */
	
	public void validate(MessageBean m) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		errorList.addIfNotNull(checkFormat("body", m.getBody(), ValidationFormat.EMAILS,
				false));

		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}

}
 
