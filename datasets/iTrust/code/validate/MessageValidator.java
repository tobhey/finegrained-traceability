package edu.ncsu.csc.itrust.validate;

import edu.ncsu.csc.itrust.action.EditOfficeVisitAction;
import edu.ncsu.csc.itrust.beans.MessageBean;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;

/**
 * Used to validate updating an office visit, by {@link EditOfficeVisitAction}
 * 
 * @author Andy
 * 
 */
public class MessageValidator extends BeanValidator<MessageBean> {

	public MessageValidator() {
	}
	
	public void validate(MessageBean mBean) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		errorList.addIfNotNull(checkFormat("body", mBean.getBody(), ValidationFormat.MESSAGES_BODY, false));
		errorList.addIfNotNull(checkFormat("subject", mBean.getSubject(), ValidationFormat.MESSAGES_SUBJECT, false));
		
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}

}
