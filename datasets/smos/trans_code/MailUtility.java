package smos.utility;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class MailUtility {

	public static void sendMail(String pSender, String pDestination, String pSubject, String pText)
			throws MessagingException, UnsupportedEncodingException {

		// Creating a mail session
		Properties props = new Properties();
		props.put("mail.smtp.host", Utility.getServerSmtp());
		Session session = Session.getDefaultInstance(props);

		// Creating the message to send
		MimeMessage message = new MimeMessage(session);
		message.setSubject(pSubject);
		message.setText(pText);

		// Adding sender and recipient addresses
		InternetAddress fromAddress = new InternetAddress(pSender);
		InternetAddress toAddress = new InternetAddress(pDestination);
		message.setFrom(fromAddress);
		message.setRecipient(Message.RecipientType.TO, toAddress);

		// Sending the message
		Transport.send(message);
	}

}
