package edu.ncsu.csc.itrust.enums;

/**
 * All of the possible transaction types, in no particular order, used in producing the operational profile.
 */
public enum TransactionType {
	ENTER_EDIT_DEMOGRAPHICS(1, "Enter/Edit patient/personnel demographics"),
	DECLARE_HCP(2, "View HCP / Change designation"),
	ALLOW_DISALLOW_ACCESS(3, "Allow/Disallow access to patient diagnosis"),
	VIEW_ACCESS_LOG(4, "View patient's record access log"),
	VIEW_RECORDS(5, "View patient's medical records"),
	AUTHENTICATE_USER(6, "Authenticate user"),
	ENTER_EDIT_PHR(7, "Enter/Edit Personal Health Information"),
	DOCUMENT_OFFICE_VISIT(8, "Document an office visit"),
	CREATE_DISABLE_PATIENT_HCP(9, "Create or disable a patient or hcp"),
	MANAGE_DIAGNOSIS_CODE(10, "Manage ICD9CM diagnosis codes"),
	REQUEST_BIOSURVEILLANCE(11, "Request biosurveillance"),
	MANAGE_PROCEDURE_CODE(12, "Manage CPT Procedure Codes"),
	MANAGE_DRUG_CODE(13, "Manage ND Drug Codes"),
	IDENTIFY_RISK_FACTORS(14, "Identify risk factors for chronic diseases"),
	CAUSE_OF_DEATH(15, "Examine cause-of-death trends"),
	DECLARE_REPRESENTATIVE(16, "Declare Personal Health Representative"),
	PATIENT_REMINDERS(17, "Proactively determine necessary patient care"),
	MAINTAIN_HOSPITALS(18, "Maintain hospital listing"),
	VIEW_PRESCRIPTION_REPORT(19, "View prescription report"),
	VIEW_HOSPITAL_STATS(20, "View hospital statistics"),
	VIEW_COMPREHENSIVE_RECORD(21, "View comprehensive patient report"),
	VIEW_EMERGENCY_REPORT(22, "View emergency patient report"),
	COMPREHENSIVE_REPORT_REQUEST(28, "Comprehensive patient report"),
	VIEW_LAB_PROCEDURE(29, "View lab procedure"),
	ENTER_EDIT_LAB_PROCEDURE(30, "Enter/Edit lab procedure"),
	MANAGE_LOINC(31, "Enter/Edit LOINC Code"),
	ADD_PATIENT_SURVEY(32, "Added Patient Survey"),
	View_HCP_SURVEY_RESULTS(33, "View HCP survey results"),
	VIEW_PATIENT_LIST(34, "View Patient List"),
	FIND_HCPS_WITH_EXP(35, "Find LHCPs with experience with a diagnosis"),
	VIEW_HEALTH_RECORDS(36, "View Patient Health Records"),
	VIEW_OFFICE_VISIT(37, "View Office Visit"),
	ADD_PRESCRIPTION(38, "Add Prescription"),
	UPDATE_OFFICE_VISIT(39, "Update an Office Visit"),
	SEND_MESSAGE(40, "Send a Message"),
	VIEW_RENEWAL_NEEDS_PATIENTS(41, "View renewal needs patients"),
	SEND_REFERRAL(42, "Refer patient to hcp"),
	CREATE_DISABLE_ER(43, "Create/disable emergency responder"),
	TELEMEDICINE_MONITORING(45, "Telemedicine monitoring"),
	CREATE_DISABLE_PHA(47, "Create/disable public health agent"),
	DRUG_INTERACTION(48, "Drug Interaction"),
	ADVERSE_EVENT(49, "Adverse Event Report"),
	LOGIN_FAILURE(50, "Login Failure"),
	UPDATE_APPT_TYPE(51, "Update an Appointment Type"),
	ADD_APPT_TYPE(52, "Add an Appointment Type"),
	ADD_APPT(53, "Appointment Added"),
	SEND_REMINDERS(54, "Reminders Sent");


	
	// S1, S3, S5, S7, S8, S14, S16, S19, S21, S22.
	public static final String patientViewableStr = "1,3,5,7,8,14,16,19,21,22,30,39,45";
	public static final int[] patientViewable = { 1, 3, 5, 7, 8, 14, 16, 19, 21, 22, 30, 39, 45 };

	private TransactionType(int code, String description) {
		this.code = code;
		this.description = description;
	}

	private int code;
	private String description;

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public static TransactionType parse(int code) {

		for (TransactionType type : TransactionType.values()) {
			if (type.code == code)
				return type;
		}
		throw new IllegalArgumentException("No transaction type exists for code " + code);
	}
}
