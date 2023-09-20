package edu.ncsu.csc.itrust.validate;

import edu.ncsu.csc.itrust.action.UpdateNDCodeListAction;
import edu.ncsu.csc.itrust.beans.ProcedureBean;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;

/**
 * Validates an ND code, from {@link UpdateNDCodeListAction}
 * 
 * @author Andy
 * 
 */
public class ProcedureBeanValidator extends BeanValidator<ProcedureBean> {
	/**
	 * The default constructor.
	 */
	public ProcedureBeanValidator() {
	}

	/**
	 * Performs the act of validating the bean in question, which varies depending on the
	 * type of validator.  If the validation does not succeed, a {@link FormValidationException} is thrown.
	 * 
	 * @param p A bean of the type to be validated.
	 */
	@Override
	public void validate(ProcedureBean p) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		errorList.addIfNotNull(checkFormat("CPT Code", p.getCPTCode(), ValidationFormat.CPT, false));
		errorList.addIfNotNull(checkFormat("Description", p.getDescription(),
				ValidationFormat.CPT_CODE_DESCRIPTION, false));
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}

}
