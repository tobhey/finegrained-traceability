package edu.ncsu.csc.itrust.dao;

import java.sql.Connection;
import java.sql.SQLException;
import edu.ncsu.csc.itrust.dao.mysql.AccessDAO;
import edu.ncsu.csc.itrust.dao.mysql.AdverseEventDAO;
import edu.ncsu.csc.itrust.dao.mysql.AllergyDAO;
import edu.ncsu.csc.itrust.dao.mysql.ApptDAO;
import edu.ncsu.csc.itrust.dao.mysql.ApptTypeDAO;
import edu.ncsu.csc.itrust.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.dao.mysql.CPTCodesDAO;
import edu.ncsu.csc.itrust.dao.mysql.DrugInteractionDAO;
import edu.ncsu.csc.itrust.dao.mysql.FakeEmailDAO;
import edu.ncsu.csc.itrust.dao.mysql.FamilyDAO;
import edu.ncsu.csc.itrust.dao.mysql.HealthRecordsDAO;
import edu.ncsu.csc.itrust.dao.mysql.HospitalsDAO;
import edu.ncsu.csc.itrust.dao.mysql.ICDCodesDAO;
import edu.ncsu.csc.itrust.dao.mysql.LOINCDAO;
import edu.ncsu.csc.itrust.dao.mysql.LabProcedureDAO;
import edu.ncsu.csc.itrust.dao.mysql.MessageDAO;
import edu.ncsu.csc.itrust.dao.mysql.NDCodesDAO;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.dao.mysql.ReferralDAO;
import edu.ncsu.csc.itrust.dao.mysql.RemoteMonitoringDAO;
import edu.ncsu.csc.itrust.dao.mysql.ReportRequestDAO;
import edu.ncsu.csc.itrust.dao.mysql.RiskDAO;
import edu.ncsu.csc.itrust.dao.mysql.SurveyDAO;
import edu.ncsu.csc.itrust.dao.mysql.SurveyResultDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.dao.mysql.VisitRemindersDAO;
import edu.ncsu.csc.itrust.testutils.EvilDAOFactory;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

/**
 * The central mediator for all Database Access Objects. The production instance uses the database connection pool
 * provided by Tomcat (so use the production instance when doing stuff from JSPs in the "real code"). Both the
 * production and the test instance parses the context.xml file to get the JDBC connection.
 * 
 * Also, @see {@link EvilDAOFactory} and @see {@link TestDAOFactory}.
 * 
 * Any DAO that is added to the system should be added in this class, in the same way that all other DAOs are.
 * 
 * @author Andy
 * 
 */
public class DAOFactory {
	private static DAOFactory productionInstance = null;
	private IConnectionDriver driver;

	/**
	 * 
	 * @return A production instance of the DAOFactory, to be used in deployment (by Tomcat).
	 */
	public static DAOFactory getProductionInstance() {
		if (productionInstance == null)
			productionInstance = new DAOFactory();
		return productionInstance;
	}

	protected DAOFactory() {
		this.driver = new ProductionConnectionDriver();
	}

	public Connection getConnection() throws SQLException {
		return driver.getConnection();
	}

	public AccessDAO getAccessDAO() {
		return new AccessDAO(this);
	}

	public AllergyDAO getAllergyDAO() {
		return new AllergyDAO(this);
	}
	
	public ApptDAO getApptDAO() {
		return new ApptDAO(this);
	}
	
	public ApptTypeDAO getApptTypeDAO() {
		return new ApptTypeDAO(this);
	}

	public AuthDAO getAuthDAO() {
		return new AuthDAO(this);
	}

	public CPTCodesDAO getCPTCodesDAO() {
		return new CPTCodesDAO(this);
	}
	
	public DrugInteractionDAO getDrugInteractionDAO() {
		return new DrugInteractionDAO(this);
	}

	public FamilyDAO getFamilyDAO() {
		return new FamilyDAO(this);
	}

	public HealthRecordsDAO getHealthRecordsDAO() {
		return new HealthRecordsDAO(this);
	}

	public HospitalsDAO getHospitalsDAO() {
		return new HospitalsDAO(this);
	}

	public ICDCodesDAO getICDCodesDAO() {
		return new ICDCodesDAO(this);
	}

	public NDCodesDAO getNDCodesDAO() {
		return new NDCodesDAO(this);
	}

	public OfficeVisitDAO getOfficeVisitDAO() {
		return new OfficeVisitDAO(this);
	}

	public PatientDAO getPatientDAO() {
		return new PatientDAO(this);
	}

	public PersonnelDAO getPersonnelDAO() {
		return new PersonnelDAO(this);
	}

	public ReferralDAO getReferralDAO() {
		return new ReferralDAO(this);
	}
	
	public RiskDAO getRiskDAO() {
		return new RiskDAO(this);
	}

	public TransactionDAO getTransactionDAO() {
		return new TransactionDAO(this);
	}

	public VisitRemindersDAO getVisitRemindersDAO() {
		return new VisitRemindersDAO(this);
	}

	public FakeEmailDAO getFakeEmailDAO() {
		return new FakeEmailDAO(this);
	}

	public ReportRequestDAO getReportRequestDAO() {
		return new ReportRequestDAO(this);
	}

	public SurveyDAO getSurveyDAO() {
		return new SurveyDAO(this);
	}

	public LabProcedureDAO getLabProcedureDAO() {
		return new LabProcedureDAO(this);
	}

	public LOINCDAO getLOINCDAO() {
		return new LOINCDAO(this);
	}

	public SurveyResultDAO getSurveyResultDAO() {
		return new SurveyResultDAO(this);
	}
	
	public MessageDAO getMessageDAO() {
		return new MessageDAO(this);
	}
	
	public AdverseEventDAO getAdverseEventDAO() {
		return new AdverseEventDAO(this);
	}
	
	public RemoteMonitoringDAO getRemoteMonitoringDAO() {
		return new RemoteMonitoringDAO(this);
	}
	
}
