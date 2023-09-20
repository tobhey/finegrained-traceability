package edu.ncsu.csc.itrust.validate;

import edu.ncsu.csc.itrust.beans.ApptBean;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;

public class ApptBeanValidator extends BeanValidator<ApptBean>{

	public void validate(ApptBean bean) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		if(bean.getComment() == null)
			return;
		errorList.addIfNotNull(checkFormat("Appointment Comment", bean.getComment(), ValidationFormat.APPT_COMMENT, false));
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}

}
