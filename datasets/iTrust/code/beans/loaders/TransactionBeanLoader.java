package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.TransactionBean;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * A loader for TransactionBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class TransactionBeanLoader implements BeanLoader<TransactionBean> {

	public List<TransactionBean> loadList(ResultSet rs) throws SQLException {
		List<TransactionBean> list = new ArrayList<TransactionBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, TransactionBean bean) throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}

	public TransactionBean loadSingle(ResultSet rs) throws SQLException {
		TransactionBean t = new TransactionBean();
		t.setAddedInfo(rs.getString("addedInfo"));
		t.setLoggedInMID(rs.getLong("loggedInMID"));
		t.setSecondaryMID(rs.getLong("secondaryMID"));
		t.setTimeLogged(rs.getTimestamp("timeLogged"));
		t.setTransactionType(TransactionType.parse(rs.getInt("transactionCode")));
		t.setTransactionID(rs.getLong("transactionID"));
		return t;
	}

}
