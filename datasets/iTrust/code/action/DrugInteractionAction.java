package edu.ncsu.csc.itrust.action;

import java.util.List;
import edu.ncsu.csc.itrust.beans.DrugInteractionBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.DrugInteractionDAO;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.validate.DrugInteractionValidator;

/**
 * Used by EditDrugInteraction.jsp to edit and get information about drug interactions.
 * 
 */
public class DrugInteractionAction {
	private DrugInteractionDAO drugDAO;
	private TransactionDAO tranDAO;
	private DrugInteractionValidator validator;
	long loggedInMID;

	/**
	 * Sets up defaults
	 * 
	 * @param factory The DAO factory to be used for generating the DAOs for this action.
	 * 
	 */
	public DrugInteractionAction(DAOFactory factory, long loggedInMID) {
		this.drugDAO = factory.getDrugInteractionDAO();
		this.tranDAO = factory.getTransactionDAO();
		this.validator = new DrugInteractionValidator();
		this.loggedInMID = loggedInMID;
	}
	
	/**
	 * Method to report an interaction
	 * @param firstDrug
	 * @param secondDrug
	 * @param description
	 * @return
	 */
	
	public String reportInteraction(String firstDrug, String secondDrug, String description) throws iTrustException,FormValidationException{
		if (firstDrug.equals(secondDrug)){
			return "Interactions can only be recorded between two different drugs";
		}
		DrugInteractionBean drugInt = new DrugInteractionBean();
		drugInt.setFirstDrug(firstDrug);
		drugInt.setSecondDrug(secondDrug);
		drugInt.setDescription(description);
		
		try {
			validator.validate(drugInt);
			if (drugDAO.reportInteraction(firstDrug,secondDrug,description)){
				tranDAO.logTransaction(TransactionType.DRUG_INTERACTION, loggedInMID);
				return "Interaction recorded successfully";
			} else {
				return "Interaction could not be added";
			}
		} catch (DBException e){
			e.printStackTrace();
			return e.getMessage();
			}
		}

	/**
	 * Method to delete an interaction
	 * @param firstDrug
	 * @param secondDrug
	 * @return
	 */
	public String deleteInteraction(String firstDrug,String secondDrug) throws iTrustException, FormValidationException{
		DrugInteractionBean drugInt = new DrugInteractionBean();
		drugInt.setFirstDrug(firstDrug);
		drugInt.setSecondDrug(secondDrug);
		drugInt.setDescription("blank");
		
		try {
			validator.validate(drugInt);
			if (drugDAO.deleteInteraction(firstDrug,secondDrug)){
				tranDAO.logTransaction(TransactionType.DRUG_INTERACTION, loggedInMID);
				return "Interaction deleted successfully";
			} else {
				return "Interaction could not be deleted";
			}
		} catch (DBException e){
			e.printStackTrace();
			throw new iTrustException(e.getMessage());
		}
	}
	
	/**
	 * Method to return a list of drug interactions for a given drug
	 * @param drugCode - The ND Code of the drug
	 * @return
	 */
	public List<DrugInteractionBean> getInteractions(String drugCode) throws iTrustException {
		try {
			return drugDAO.getInteractions(drugCode);
		} catch (DBException e){
			throw new iTrustException(e.getMessage());
		}
	}
}