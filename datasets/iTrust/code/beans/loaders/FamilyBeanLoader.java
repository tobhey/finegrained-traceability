package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.FamilyMemberBean;

/**
 * A loader for FamilyMemberBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class FamilyBeanLoader implements BeanLoader<FamilyMemberBean> {
	private String relation;

	public FamilyBeanLoader(String relation) {
		this.relation = relation;
	}

	public List<FamilyMemberBean> loadList(ResultSet rs) throws SQLException {
		ArrayList<FamilyMemberBean> list = new ArrayList<FamilyMemberBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, FamilyMemberBean bean) throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}

	public FamilyMemberBean loadSingle(ResultSet rs) throws SQLException {
		FamilyMemberBean fam = new FamilyMemberBean();
		fam.setRelation(relation);
		fam.setFirstName(rs.getString("FirstName"));
		fam.setLastName(rs.getString("LastName"));
		fam.setMid(rs.getInt("MID"));
		return fam;
	}
}
