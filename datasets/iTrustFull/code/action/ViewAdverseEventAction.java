package edu.ncsu.csc.itrust.action;

import java.sql.SQLException;
import java.util.List;
import edu.ncsu.csc.itrust.beans.AdverseEventBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AdverseEventDAO;
import edu.ncsu.csc.itrust.exception.DBException;

public class ViewAdverseEventAction {
	private AdverseEventDAO adEventDAO;

	public ViewAdverseEventAction(DAOFactory factory)
	{
		this.adEventDAO = factory.getAdverseEventDAO();
	}
	
	public AdverseEventBean getAdverseEvent(int id) throws DBException
	{
		return adEventDAO.getReport(id);
	}
	
	public List<AdverseEventBean> getUnremovedAdverseEventsByCode(String code) throws DBException
	{
		return adEventDAO.getUnremovedAdverseEventsByCode(code);
	}
	
	public String getNameForCode(String code) throws DBException
	{
		return adEventDAO.getNameForCode(code);
	}
}
