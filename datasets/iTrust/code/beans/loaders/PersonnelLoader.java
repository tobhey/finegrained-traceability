package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.enums.Role;

/**
 * A loader for PersonnelBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class PersonnelLoader implements BeanLoader<PersonnelBean> {
	public List<PersonnelBean> loadList(ResultSet rs) throws SQLException {
		List<PersonnelBean> list = new ArrayList<PersonnelBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public PersonnelBean loadSingle(ResultSet rs) throws SQLException {
		PersonnelBean p = new PersonnelBean();
		p.setMID(rs.getLong("MID"));
		p.setAMID(rs.getLong("amid"));
		p.setRole(Role.parse(rs.getString("role")));
		p.setLastName(rs.getString("lastName"));
		p.setFirstName(rs.getString("firstName"));
		p.setPhone1(rs.getString("phone1"));
		p.setPhone2(rs.getString("phone2"));
		p.setPhone3(rs.getString("phone3"));
		p.setStreetAddress1(rs.getString("address1"));
		p.setStreetAddress2(rs.getString("address2"));
		p.setCity(rs.getString("city"));
		p.setState(rs.getString("state"));
		p.setZip1((rs.getString("zip1")));
		p.setZip2((rs.getString("zip2")));
		p.setEmail(rs.getString("email"));
		p.setSpecialty(rs.getString("specialty"));
		p.setMessageFilter(rs.getString("MessageFilter"));
		return p;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, PersonnelBean p) throws SQLException {
		int i = 1;
		ps.setLong(i++, p.getAMID());
		ps.setString(i++, p.getFirstName());
		ps.setString(i++, p.getLastName());
		ps.setString(i++, p.getPhone1());
		ps.setString(i++, p.getPhone2());
		ps.setString(i++, p.getPhone3());
		ps.setString(i++, p.getStreetAddress1());
		ps.setString(i++, p.getStreetAddress2());
		ps.setString(i++, p.getCity());
		ps.setString(i++, p.getState());
		ps.setString(i++, p.getZip());
		ps.setString(i++, p.getZip1());
		ps.setString(i++, p.getZip2());
		//ps.setString(i++, p.getSpecialty());
		ps.setString(i++, p.getEmail());
		ps.setString(i++, p.getMessageFilter());
		return ps;
	}
}
