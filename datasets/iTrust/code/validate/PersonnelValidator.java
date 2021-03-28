package edu.ncsu.csc.itrust.validate;

import edu.ncsu.csc.itrust.action.EditPersonnelAction;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;

/**
 * Validates a personnel bean, from {@link EditPersonnelAction}
 * 
 * @author Andy
 * 
 */
public class PersonnelValidator extends BeanValidator<PersonnelBean> {
	/**
	 * The default constructor.
	 */
	public PersonnelValidator() {
	}

	/**
	 * Performs the act of validating the bean in question, which varies depending on the
	 * type of validator.  If the validation does not succeed, a {@link FormValidationException} is thrown.
	 * 
	 * @param p A bean of the type to be validated.
	 */
	@Override
	public void validate(PersonnelBean p) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		errorList.addIfNotNull(checkFormat("First name", p.getFirstName(), ValidationFormat.NAME, false));
		errorList.addIfNotNull(checkFormat("Last name", p.getLastName(), ValidationFormat.NAME, false));
		errorList.addIfNotNull(checkFormat("Street Address 1", p.getStreetAddress1(),
				ValidationFormat.ADDRESS, false));
		errorList.addIfNotNull(checkFormat("Street Address 2", p.getStreetAddress2(),
				ValidationFormat.ADDRESS, true));
		errorList.addIfNotNull(checkFormat("City", p.getCity(), ValidationFormat.CITY, false));
		errorList.addIfNotNull(checkFormat("State", p.getState(), ValidationFormat.STATE, false));
		errorList.addIfNotNull(checkFormat("Zip Code", p.getZip(), ValidationFormat.ZIPCODE, false));
		errorList
				.addIfNotNull(checkFormat("Phone Number", p.getPhone(), ValidationFormat.PHONE_NUMBER, false));
		errorList
		.addIfNotNull(checkFormat("Email", p.getEmail(), ValidationFormat.EMAIL, true));
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}

}
