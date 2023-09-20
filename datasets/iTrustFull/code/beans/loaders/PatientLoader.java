package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.PatientBean;

/**
 * A loader for PatientBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class PatientLoader implements BeanLoader<PatientBean> {
	public List<PatientBean> loadList(ResultSet rs) throws SQLException {
		List<PatientBean> list = new ArrayList<PatientBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public PatientBean loadSingle(ResultSet rs) throws SQLException {
		PatientBean p = new PatientBean();
		p.setMID(rs.getInt("MID"));
		p.setFirstName(rs.getString("firstName"));
		p.setLastName(rs.getString("LastName"));
		Date dateOfBirth = rs.getDate("DateOfBirth");
		if (dateOfBirth != null)
			p.setDateOfBirthStr(new SimpleDateFormat("MM/dd/yyyy").format(dateOfBirth));
		Date dateOfDeath = rs.getDate("DateOfDeath");
		if (dateOfDeath != null)
			p.setDateOfDeathStr(new SimpleDateFormat("MM/dd/yyyy").format(dateOfDeath));
		p.setCauseOfDeath(rs.getString("CauseOfDeath"));
		p.setEmail(rs.getString("Email"));
		p.setStreetAddress1(rs.getString("address1"));
		p.setStreetAddress2(rs.getString("address2"));
		p.setCity(rs.getString("City"));
		p.setState(rs.getString("State"));
		p.setZip1((rs.getString("Zip1")));
		p.setZip2((rs.getString("Zip2")));
		p.setPhone1((rs.getString("phone1")));
		p.setPhone2((rs.getString("phone2")));
		p.setPhone3((rs.getString("phone3")));
		p.setEmergencyName(rs.getString("eName"));
		p.setEmergencyPhone1(rs.getString("ePhone1"));
		p.setEmergencyPhone2(rs.getString("ePhone2"));
		p.setEmergencyPhone3(rs.getString("ePhone3"));
		p.setIcName(rs.getString("icName"));
		p.setIcAddress1(rs.getString("icAddress1"));
		p.setIcAddress2(rs.getString("icAddress2"));
		p.setIcCity(rs.getString("icCity"));
		p.setIcState(rs.getString("icState"));
		p.setIcZip1(rs.getString("icZip1"));
		p.setIcZip2(rs.getString("icZip2"));
		p.setIcPhone1(rs.getString("icPhone1"));
		p.setIcPhone2(rs.getString("icPhone2"));
		p.setIcPhone3(rs.getString("icPhone3"));
		p.setIcID(rs.getString("icID"));
		p.setMotherMID(rs.getString("MotherMID"));
		p.setFatherMID(rs.getString("FatherMID"));
		p.setBloodTypeStr(rs.getString("BloodType"));
		p.setEthnicityStr(rs.getString("Ethnicity"));
		p.setGenderStr(rs.getString("Gender"));
		p.setTopicalNotes(rs.getString("TopicalNotes"));
		p.setCreditCardType(rs.getString("CreditCardType"));
		p.setCreditCardNumber(rs.getString("CreditCardNumber"));
		p.setMessageFilter(rs.getString("MessageFilter"));
		return p;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, PatientBean p) throws SQLException {
		int i = 1;
		ps.setString(i++, p.getFirstName());
		ps.setString(i++, p.getLastName());
		ps.setString(i++, p.getEmail());
		// ps.setString(i++, p.getSecurityQuestion());
		// ps.setString(i++, p.getSecurityAnswer());
		ps.setString(i++, p.getStreetAddress1());
		ps.setString(i++, p.getStreetAddress2());
		ps.setString(i++, p.getCity());
		ps.setString(i++, p.getState());
		ps.setString(i++, p.getZip1());
		ps.setString(i++, p.getZip2());
		ps.setString(i++, p.getPhone1());
		ps.setString(i++, p.getPhone2());
		ps.setString(i++, p.getPhone3());
		ps.setString(i++, p.getEmergencyName());
		ps.setString(i++, p.getEmergencyPhone1());
		ps.setString(i++, p.getEmergencyPhone2());
		ps.setString(i++, p.getEmergencyPhone3());
		ps.setString(i++, p.getIcName());
		ps.setString(i++, p.getIcAddress1());
		ps.setString(i++, p.getIcAddress2());
		ps.setString(i++, p.getIcCity());
		ps.setString(i++, p.getIcState());
		ps.setString(i++, p.getIcZip1());
		ps.setString(i++, p.getIcZip2());
		ps.setString(i++, p.getIcPhone1());
		ps.setString(i++, p.getIcPhone2());
		ps.setString(i++, p.getIcPhone3());
		ps.setString(i++, p.getIcID());
		Date date = null;
		try {
			date = new java.sql.Date(new SimpleDateFormat("MM/dd/yyyy").parse(p.getDateOfBirthStr())
					.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ps.setDate(i++, date);
		date = null;
		try {
			date = new java.sql.Date(new SimpleDateFormat("MM/dd/yyyy").parse(p.getDateOfDeathStr())
					.getTime());
		} catch (ParseException e) {
			if ("".equals(p.getDateOfDeathStr()))
				date = null;
			else
				e.printStackTrace();
		}
		ps.setDate(i++, date);
		ps.setString(i++, p.getCauseOfDeath());
		ps.setString(i++, p.getMotherMID());
		ps.setString(i++, p.getFatherMID());
		ps.setString(i++, p.getBloodType().getName());
		ps.setString(i++, p.getEthnicity().getName());
		ps.setString(i++, p.getGender().getName());
		ps.setString(i++, p.getTopicalNotes());
		ps.setString(i++, p.getCreditCardType());
		ps.setString(i++, p.getCreditCardNumber());
		ps.setString(i++, p.getMessageFilter());
		return ps;
	}
}
