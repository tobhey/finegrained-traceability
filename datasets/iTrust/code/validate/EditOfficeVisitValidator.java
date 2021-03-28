package edu.ncsu.csc.itrust.validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import edu.ncsu.csc.itrust.action.EditOfficeVisitAction;
import edu.ncsu.csc.itrust.beans.forms.EditOfficeVisitForm;
import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;

/**
 * Used to validate updating an office visit, by {@link EditOfficeVisitAction}
 * 
 * @author Andy
 * 
 */
public class EditOfficeVisitValidator extends BeanValidator<EditOfficeVisitForm> {
	private boolean validatePrescription = false;

	/**
	 * The default constructor.
	 */
	public EditOfficeVisitValidator() {
	}

	public EditOfficeVisitValidator(boolean validatePrescription) {
		this.validatePrescription = validatePrescription;
	}

	/**
	 * Performs the act of validating the bean in question, which varies depending on the
	 * type of validator.  If the validation does not succeed, a {@link FormValidationException} is thrown.
	 * 
	 * @param p A bean of the type to be validated.
	 */
	@Override
	public void validate(EditOfficeVisitForm form) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		errorList.addIfNotNull(checkFormat("HCP ID", form.getHcpID(), ValidationFormat.MID, false));
		errorList.addIfNotNull(checkFormat("Hospital ID", form.getHospitalID(), ValidationFormat.HOSPITAL_ID,
				true));
		errorList.addIfNotNull(checkFormat("Notes", form.getNotes(), ValidationFormat.NOTES, true));
		errorList.addIfNotNull(checkFormat("Patient ID", form.getPatientID(), ValidationFormat.MID, false));
		errorList.addIfNotNull(checkFormat("Visit Date", form.getVisitDate(), ValidationFormat.DATE, false));
		if (validatePrescription) {
			errorList.addIfNotNull(checkFormat("Start Date", form.getStartDate(), ValidationFormat.DATE,
					false));
			errorList.addIfNotNull(checkFormat("End Date", form.getEndDate(), ValidationFormat.DATE, false));
			errorList.addIfNotNull(checkFormat("Instructions", form.getInstructions(),
					ValidationFormat.NOTES, true));
			errorList.addIfNotNull(checkInt("Dosage", form.getDosage(), 0, 9999, false));
			if ((checkFormat("Start Date", form.getStartDate(), ValidationFormat.DATE, false)) == ""
					&& (checkFormat("End Date", form.getEndDate(), ValidationFormat.DATE, false)) == "") {
				Date sd = null;
				Date ed = null;
				try {
					sd = new SimpleDateFormat("MM/dd/yyyy").parse(form.getStartDate());
					ed = new SimpleDateFormat("MM/dd/yyyy").parse(form.getEndDate());
					String dateError = null;
					if (sd.after(ed)) {
						dateError = "The start date of the prescription must be before the end date.";
					}
					errorList.addIfNotNull(dateError);
				} catch (ParseException e) {
					errorList.addIfNotNull(ValidationFormat.DATE.getDescription());
				}

			}
		}
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}
}
