package edu.ncsu.csc.itrust.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.beans.DrugInteractionBean;
import edu.ncsu.csc.itrust.beans.loaders.DrugInteractionBeanLoader;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * Used for managing drug interactions.
 * 
 * DAO stands for Database Access Object. All DAOs are intended to be reflections of the database, that is,
 * one DAO per table in the database (most of the time). For more complex sets of queries, extra DAOs are
 * added. DAOs can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than a factory. All DAOs should be
 * accessed by DAOFactory (@see {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 * 
 * @see http://www.fda.gov/Drugs/InformationOnDrugs/ucm142438.htm
 * @author Andy
 * 
 */
public class DrugInteractionDAO {
	private DAOFactory factory;
	private DrugInteractionBeanLoader interactionLoader;
	
	/**
	 * The typical constructor.
	 * @param factory The {@link DAOFactory} associated with this DAO, which is used for obtaining SQL connections, etc.
	 */
	public DrugInteractionDAO(DAOFactory factory) {
		this.factory = factory;
		interactionLoader = new DrugInteractionBeanLoader();
	}
	
	/**
	 * Returns a list of all drug interactions for the input drug name
	 * 
	 * @return A java.util.List of DrugInteractionBeans.
	 * @throws DBException
	 */
	public List<DrugInteractionBean> getInteractions(String drugCode) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("SELECT * FROM druginteractions WHERE FirstDrug = ? OR SecondDrug = ?");
			ps.setString(1, drugCode);
			ps.setString(2, drugCode);
			ResultSet rs = ps.executeQuery();
			return interactionLoader.loadList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Adds a new drug interaction, returns whether or not the addition was successful. If the code already exists, an
	 * iTrustException is thrown.
	 * 
	 * @param firstDrug  The name of the first drug in an interaction.
	 * @param secondDrug  The name of the second drug in an interaction.
	 * @param description  Explanation of the drug interaction.
	 * @return A boolean indicating success or failure.
	 * @throws DBException
	 * @throws iTrustException
	 */
	public boolean reportInteraction(String firstDrug, String secondDrug, String description) throws DBException, iTrustException {
		if(firstDrug.equals(secondDrug)) throw new iTrustException("Drug cannot interact with itself.");
		
		List<DrugInteractionBean> currentIntsDrug2 = getInteractions(secondDrug);
		for(DrugInteractionBean dib : currentIntsDrug2){
			if(dib.getSecondDrug().equals(firstDrug)){
				throw new iTrustException("Error: Interaction between these drugs already exists.");
			}
		}
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("INSERT INTO druginteractions (FirstDrug, SecondDrug, Description) " + "VALUES (?,?,?)");
			ps.setString(1, firstDrug);
			ps.setString(2, secondDrug);
			ps.setString(3, description);
			return (1 == ps.executeUpdate());
		} catch (SQLException e) {
			e.printStackTrace();
			if (1062 == e.getErrorCode())
				throw new iTrustException("Error: Interaction between these drugs already exists.");
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}
	
	/**
	 * Remove an interaction from the database
	 * 
	 * @param firstDrug  The name of the first patient
	 * @param secondDrug  The name of the second patient
	 * @return true if removed successfully, false if not in list
	 */
	public boolean deleteInteraction(String firstDrug,String secondDrug) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = factory.getConnection();
			ps = conn.prepareStatement("DELETE FROM druginteractions WHERE (FirstDrug = ? OR SecondDrug = ?) AND (FirstDrug = ? OR SecondDrug = ?)");
			ps.setString(1, firstDrug);
			ps.setString(2, firstDrug);
			ps.setString(3, secondDrug);
			ps.setString(4, secondDrug);
			if(ps.executeUpdate() == 0)
				return false;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, ps);
		}
	}

}
