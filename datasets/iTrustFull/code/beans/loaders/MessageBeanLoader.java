package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.itrust.beans.MessageBean;

/**
 * A loader for MessageBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class MessageBeanLoader implements BeanLoader<MessageBean> {

	public List<MessageBean> loadList(ResultSet rs) throws SQLException {
		List<MessageBean> list = new ArrayList<MessageBean>();
		while (rs.next())
			list.add(loadSingle(rs));
		return list;
	}

	public PreparedStatement loadParameters(PreparedStatement ps, MessageBean message) throws SQLException {
		ps.setLong(1, message.getFrom());
		ps.setLong(2, message.getTo());
		ps.setString(3, message.getBody());
		ps.setString(4, message.getSubject());
		ps.setInt(5, message.getRead());
		if (message.getParentMessageId() != 0L) {
				ps.setLong(6, message.getParentMessageId());
		}
		return ps;
	}

	public MessageBean loadSingle(ResultSet rs) throws SQLException {
		MessageBean message = new MessageBean();
		message.setMessageId(rs.getLong("message_id"));
		message.setFrom(rs.getLong("from_id"));
		message.setTo(rs.getLong("to_id"));
		message.setSubject(rs.getString("subject"));
		message.setBody(rs.getString("message"));
		message.setSentDate(rs.getTimestamp("sent_date"));
		message.setRead(rs.getInt("been_read"));
		message.setParentMessageId(rs.getLong("parent_msg_id"));
		return message;
	}

}
