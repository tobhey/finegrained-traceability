package edu.ncsu.csc.itrust.validate;

import edu.ncsu.csc.itrust.action.EditHealthHistoryAction;
import edu.ncsu.csc.itrust.beans.forms.HealthRecordForm;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;

/**
 * Validator used in adding health information on Basic Health History, {@link EditHealthHistoryAction}
 * 
 * @author Andy
 * 
 */
public class HealthRecordFormValidator extends BeanValidator<HealthRecordForm> {
	/**
	 * Performs the act of validating the bean in question, which varies depending on the
	 * type of validator.  If the validation does not succeed, a {@link FormValidationException} is thrown.
	 * 
	 * @param p A bean of the type to be validated.
	 */
	@Override
	public void validate(HealthRecordForm bean) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		errorList.addIfNotNull(checkInt("Systolic blood pressure", bean.getBloodPressureN(), 0, 999, false));
		errorList.addIfNotNull(checkInt("Diastolic blood pressure", bean.getBloodPressureD(), 0, 999, false));
		errorList.addIfNotNull(checkInt("Cholesterol HDL", bean.getCholesterolHDL(), 0, 89, false));
		errorList.addIfNotNull(checkInt("Cholesterol LDL", bean.getCholesterolLDL(), 0, 600, false));
		errorList.addIfNotNull(checkInt("Cholesterol Triglycerides", bean.getCholesterolTri(), 100, 600,
				false));
		errorList.addIfNotNull(checkFormat("Height", bean.getHeight(), ValidationFormat.Height, false));
		errorList.addIfNotNull(checkFormat("Weight", bean.getWeight(), ValidationFormat.Weight, false));
		errorList.addIfNotNull(checkBoolean("Smoker", bean.getIsSmoker()));
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}

}
