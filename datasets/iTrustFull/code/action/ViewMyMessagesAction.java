package edu.ncsu.csc.itrust.action;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import edu.ncsu.csc.itrust.beans.MessageBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.MessageDAO;
import edu.ncsu.csc.itrust.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;

/**
 * Action class for ViewMyMessages.jsp
 *
 */
public class ViewMyMessagesAction {
	private long loggedInMID;
	private PatientDAO patientDAO;
	private PersonnelDAO personnelDAO;
	private MessageDAO messageDAO;

	/**
	 * Set up defaults
	 * 
	 * @param factory The DAOFactory used to create the DAOs used in this action.
	 * @param loggedInMID The MID of the user who is viewing their messages.
	 */
	public ViewMyMessagesAction(DAOFactory factory, long loggedInMID) {
		this.loggedInMID = loggedInMID;
		this.patientDAO = factory.getPatientDAO();
		this.personnelDAO = factory.getPersonnelDAO();
		this.messageDAO = factory.getMessageDAO();
	}
	
	/**
	 * Gets all the messages for the logged in user
	 * 
	 * @return a list of all the user's messages
	 * @throws SQLException
	 */
	public List<MessageBean> getAllMyMessages() throws SQLException {
		
		return messageDAO.getMessagesFor(loggedInMID);
	}
	
	/**
	 * Gets all the messages for the logged in user and sorts by ascending time
	 * 
	 * @return a list of all the user's messages
	 * @throws SQLException
	 */
	public List<MessageBean> getAllMyMessagesTimeAscending() throws SQLException {
		
		return messageDAO.getMessagesTimeAscending(loggedInMID);
	}
	
	/**
	 * Gets all the messages for the logged in user and sorts names in ascending order
	 * 
	 * @return a list of all the user's messages
	 * @throws SQLException
	 */
	public List<MessageBean> getAllMyMessagesNameAscending() throws SQLException {
		
		return messageDAO.getMessagesNameAscending(loggedInMID);
	}
	
	/**
	 * Gets all the messages for the logged in user and sorts name in descending order
	 * 
	 * @return a list of all the user's messages
	 * @throws SQLException
	 */
	public List<MessageBean> getAllMyMessagesNameDescending() throws SQLException {
		
		return messageDAO.getMessagesNameDescending(loggedInMID);
	}
	
	/**
	 * Gets all the sent messages for the logged in user
	 * 
	 * @return a list of all the user's sent messages
	 * @throws SQLException
	 */
	public List<MessageBean> getAllMySentMessages() throws SQLException {
		
		return messageDAO.getMessagesFrom(loggedInMID);
	}
	
	/**
	 * Gets all the messages for the logged in user and sorts by ascending time
	 * 
	 * @return a list of all the user's messages
	 * @throws SQLException
	 */
	public List<MessageBean> getAllMySentMessagesTimeAscending() throws SQLException {
		
		return messageDAO.getMessagesFromTimeAscending(loggedInMID);
	}
	
	/**
	 * Gets all the messages for the logged in user and sorts names in ascending order
	 * 
	 * @return a list of all the user's messages
	 * @throws SQLException
	 */
	public List<MessageBean> getAllMySentMessagesNameAscending() throws SQLException {
		
		return messageDAO.getMessagesFromNameAscending(loggedInMID);
	}
	
	/**
	 * Gets all the messages for the logged in user and sorts name in descending order
	 * 
	 * @return a list of all the user's messages
	 * @throws SQLException
	 */
	public List<MessageBean> getAllMySentMessagesNameDescending() throws SQLException {
		
		return messageDAO.getMessagesFromNameDescending(loggedInMID);
	}
	
	public String validateAndCreateFilter(String filter) {
		String[] f = filter.split(",", -1);
		
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Date s = null;
		Date en = null;
		try {	
			if(!f[4].equals(""))
				s = format.parse(f[4]);
			if(!f[5].equals(""))
				en = format.parse(f[5]);
			if(s!=null && en!=null && en.before(s)) {
				return "Error: The end date cannot be before the start date.";
			}
		} catch(Exception e) {
			return "Error: A date was not formatted correctly, please enter dates as MM/DD/YYYY";
		}
		
		for(int i=0; i<f.length; i++) {
			f[i]=f[i].replace("\"", "");
			f[i]=f[i].replace("<","");
			f[i]=f[i].replace(">","");
		}
		
		String nf = f[0]+","+f[1]+","+f[2]+","+f[3]+","+f[4]+","+f[5];
		
		return nf;
	}
	
	/**
	 * Gets a list of messages for a user based on their filter criteria.
	 * 
	 * @param messages List of all of a user's MessageBeans
	 * @param filter String containing a user's filter criteria.
	 * @return a List of MessageBeans that meet the criteria of the filter.
	 * @throws iTrustException
	 * @throws ParseException
	 */
	public List<MessageBean> filterMessages(List<MessageBean> messages, String filter) throws iTrustException, ParseException {
		List<MessageBean> filtered = new ArrayList<MessageBean>();
		String[] f = filter.split(",", -1);
		for(MessageBean m : messages) {
			/**
			 * Check the sender filter field.
			 * Exclude if this MessageBean does not match the 
			 * requested sender, if one is specified.
			 */
			if(!f[0].equals("")) {
				if(!this.getName(m.getFrom()).equalsIgnoreCase(f[0]))
					continue;
			}
			/**
			 * Check the subject filter field.
			 * Exclude if this MessageBean does not match the 
			 * requested subject, if one is specified.
			 */
			if(!f[1].equals("")) {
				if(!m.getSubject().equalsIgnoreCase(f[1]))
					continue;
			}
			/**
			 * Check the body of the message for certain words.
			 * Exclude if this MessageBean if it does not contain 
			 * those words in the message body.
			 */
			if(!f[2].equals("")) {
				if(!m.getSubject().toLowerCase().contains(f[2].toLowerCase()) && !m.getBody().toLowerCase().contains(f[2].toLowerCase()))
					continue;
			}
			/**
			 * Check the body of the message for certain words.
			 * Exclude if this MessageBean if it does contain 
			 * those words in the message body.
			 */
			if(!f[3].equals("")) {
				if(m.getSubject().toLowerCase().contains(f[3].toLowerCase()) || m.getBody().toLowerCase().contains(f[3].toLowerCase()))
					continue;
			}
			/**
			 * Check the start date filter field.
			 * Exclude if this MessageBean was not sent after
			 * this date.
			 */
			if(!f[4].equals("")) {
				DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
				Date s = format.parse(f[4]);
				if(s.after(m.getSentDate()))
						continue;
				
			}
			/**
			 * Check the end date filter field.
			 * Exclude if this MessageBean was not sent before
			 * this date.
			 */
			if(!f[5].equals("")) {
				DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
				Date s = format.parse(f[5]);
				Calendar c = Calendar.getInstance();
				c.setTime(s);
				c.add(Calendar.DAY_OF_MONTH, 1);
				s = c.getTime();
				if(s.before(m.getSentDate()))
						continue;
				
			}
			/**
			 * If the message has not been eliminated by any 
			 * of the filter fields, add it to the new list 
			 * of messages.
			 */
			filtered.add(m);
		}
		
		return filtered;
	}
	
	/**
	 * Gets a patient's name from their MID
	 * 
	 * @param mid the MID of the patient
	 * @return the patient's name
	 * @throws iTrustException
	 */
	public String getName(long mid) throws iTrustException {
		if(mid < 7000000000L)
			return patientDAO.getName(mid);
		else
			return personnelDAO.getName(mid);
	}
	
	/**
	 * Gets a personnel's name from their MID
	 * 
	 * @param mid the MID of the personnel
	 * @return the personnel's name
	 * @throws iTrustException
	 */
	public String getPersonnelName(long mid) throws iTrustException {
		return personnelDAO.getName(mid);
	}
	
	/**
	 * Set the state of the MessageBean to read, after 
	 * it is read by a user.
	 * @param mBean MessageBean to be read
	 */
	public void setRead(MessageBean mBean) {
		try {
			messageDAO.updateRead(mBean);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
