package edu.ncsu.csc.itrust.validate;


import edu.ncsu.csc.itrust.beans.LOINCbean;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;


/**
 * Validator used to validate LOINCbean
 */
public class LOINCBeanValidator extends BeanValidator<LOINCbean> {
	/**
	 * The default constructor.
	 */
	public LOINCBeanValidator() {
	}

	/**
	 * Performs the act of validating the bean in question, which varies depending on the
	 * type of validator.  If the validation does not succeed, a {@link FormValidationException} is thrown.
	 * 
	 * @param p A bean of the type to be validated.
	 */
	@Override
	public void validate(LOINCbean m) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		if(m.getLabProcedureCode()==null || m.getComponent()==null || m.getKindOfProperty()==null){
			throw new FormValidationException("You must have a Lab Procedure Code, Component and Kind Of Property");
		}
		errorList.addIfNotNull(checkFormat("LaboratoryProcedureCode", m.getLabProcedureCode(), ValidationFormat.LOINC, false));
		errorList.addIfNotNull(checkFormat("Component", m.getComponent(), ValidationFormat.LOINC_ITEM, false));
		errorList.addIfNotNull(checkFormat("KindOfProperty", m.getKindOfProperty(), ValidationFormat.LOINC_ITEM, false));
		errorList.addIfNotNull(checkFormat("TimeAspect", m.getTimeAspect(), ValidationFormat.LOINC_ITEM, true));
		errorList.addIfNotNull(checkFormat("System", m.getSystem(), ValidationFormat.LOINC_ITEM, true));
		errorList.addIfNotNull(checkFormat("ScaleType",m.getScaleType(), ValidationFormat.LOINC_ITEM, true));
		errorList.addIfNotNull(checkFormat("MethodType", m.getMethodType(), ValidationFormat.LOINC_ITEM, true));

		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}
}
