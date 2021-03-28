
package edu.ncsu.csc.itrust.validate;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import org.apache.commons.validator.*;


public class MailValidator extends EmailValidator {
	/**
	 * The default constructor.
	 */
	public MailValidator(){
		
	}
	/**
	 * Performs the act of validating the bean in question, which varies depending on the
	 * type of validator.  If the validation does not succeed, a {@link FormValidationException} is thrown.
	 * 
	 * @param p A bean of the type to be validated.
	 */
	public boolean validateEmail(String email) throws FormValidationException {
		MailValidator val = new MailValidator();
	
		return val.isValid(email);
		
	}
	

}
 
