package edu.ncsu.csc.itrust.validate;

import edu.ncsu.csc.itrust.beans.DrugInteractionBean;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;

/**
 * Validates a drug interaction bean, from {@link DrugInteractionAction}
 * 
 */
public class DrugInteractionValidator extends BeanValidator<DrugInteractionBean> {
	/**
	 * The default constructor.
	 */
	public DrugInteractionValidator() {
	}

	/**
	 * Performs the act of validating the bean in question, which varies depending on the
	 * type of validator.  If the validation does not succeed, a {@link FormValidationException} is thrown.
	 * 
	 * @param d A bean of the type to be validated.
	 */
	@Override
	public void validate(DrugInteractionBean d) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		errorList.addIfNotNull(checkFormat("FirstDrug", d.getFirstDrug(), ValidationFormat.ND,false));
		errorList.addIfNotNull(checkFormat("SecondDrug", d.getSecondDrug(), ValidationFormat.ND,false));
		errorList.addIfNotNull(checkFormat("description", d.getDescription(), ValidationFormat.DRUG_INT_COMMENTS,false));
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}
}
