package edu.ncsu.csc.itrust.validate;

import edu.ncsu.csc.itrust.action.UpdateNDCodeListAction;
import edu.ncsu.csc.itrust.beans.MedicationBean;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;

/**
 * Validates ND code beans, from {@link UpdateNDCodeListAction}
 * 
 * @author Andy
 * 
 */
public class MedicationBeanValidator extends BeanValidator<MedicationBean> {
	/**
	 * The default constructor.
	 */
	public MedicationBeanValidator() {
	}

	/**
	 * Performs the act of validating the bean in question, which varies depending on the
	 * type of validator.  If the validation does not succeed, a {@link FormValidationException} is thrown.
	 * 
	 * @param p A bean of the type to be validated.
	 */
	@Override
	public void validate(MedicationBean m) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		errorList.addIfNotNull(checkFormat("ND Code", m.getNDCode(), ValidationFormat.ND, false));
		errorList.addIfNotNull(checkFormat("Description", m.getDescription(),
				ValidationFormat.ND_CODE_DESCRIPTION, false));
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}
}
