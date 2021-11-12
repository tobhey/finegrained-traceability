package smos.utility;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;


public class MailUtility {

	public static void inviareMail(String pMittente, String pDestinatario, String pOggetto, String pTesto) throws MessagingException, UnsupportedEncodingException {
		
		// Creazione di una mail session
		Properties props = new Properties();
		props.put("mail.smtp.host", Utility.ottenereServerSmtp());
		Session session = Session.getDefaultInstance(props);

		// Creazione del messaggio da inviare
		MimeMessage message = new MimeMessage(session);
		message.setSubject(pOggetto);
		message.setText(pTesto);

		// Aggiunta degli indirizzi del mittente e del destinatario
		InternetAddress fromAddress = new InternetAddress(pMittente);
		InternetAddress toAddress = new InternetAddress(pDestinatario);
		message.setFrom(fromAddress);
		message.setRecipient(Message.RecipientType.TO, toAddress);

		// Invio del messaggio
		Transport.send(message);
	}

}
