package edu.ncsu.csc.itrust.validate;

import edu.ncsu.csc.itrust.action.AddRemoteMonitoringDataAction;
import edu.ncsu.csc.itrust.beans.RemoteMonitoringDataBean;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;

/**
 * Validator used to validate adding new remote monitoring data in {@link AddRemoteMonitoringDataAction}
 * 
 */
public class RemoteMonitoringDataBeanValidator extends BeanValidator<RemoteMonitoringDataBean> {
	/**
	 * The default constructor.
	 */
	public RemoteMonitoringDataBeanValidator() {
	}
	
	/**
	 * Performs the act of validating the bean in question, which varies depending on the
	 * type of validator.  If the validation does not succeed, a {@link FormValidationException} is thrown.
	 * 
	 * @param p A bean of the type to be validated.
	 */
	@Override
	public void validate(RemoteMonitoringDataBean m) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		errorList.addIfNotNull(checkFormat("Systolic Blood Pressure", "" + m.getSystolicBloodPressure(),
				ValidationFormat.SYSTOLIC_BLOOD_PRESSURE, false));
		errorList.addIfNotNull(checkFormat("Diastolic Blood Pressure", "" + m.getDiastolicBloodPressure(),
				ValidationFormat.DIASTOLIC_BLOOD_PRESSURE, false));
		errorList.addIfNotNull(checkFormat("Glucose Level", "" + m.getGlucoseLevel(),
				ValidationFormat.GLUCOSE_LEVEL, false));
		if (errorList.hasErrors()){
			throw new FormValidationException(errorList);
		}
	}
}
