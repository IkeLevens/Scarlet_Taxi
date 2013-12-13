package edu.rutgers.cs.scarletTaxi.notification_exporter.export;

import edu.rutgers.cs.scarletTaxi.centralDataStoratge.User;
import edu.rutgers.cs.scarletTaxi.notification_exporter.process.NType;
import edu.rutgers.cs.scarletTaxi.notification_exporter.process.Notification;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * This class is responsible for exporting of the compiled notification objects. This class handles
 * both the export of email and SMS notifications according to the user's settings.
 * @author Michael Wasserman
 * 
 */
public class Exporter {
	
	/*Used for exporting notifications with only one recipient
	 * @param Notification
	 */
	public static boolean exportNotification(Notification n){
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        //Check that notification type does not contain multiple recipients
        if(n.type==NType.RideCancel || n.type==NType.TimeReminder){
        	return false;
        }
        User recipient = n.recipient;
        try {
        	if(recipient.receiveEmailNotification){
        		Message msg = new MimeMessage(session);
        		msg.setFrom(new InternetAddress("NO_REPLY@ScarletTaxi.com", "Scarlet Taxi Notification System"));
        		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.email, recipient.username));
        		msg.setSubject("Scarlet Taxi Notification");
        		msg.setText(n.text);
        		Transport.send(msg);
        	}
        	if(recipient.receiveSMSNOtification){
        		Message msg = new MimeMessage(session);
        		msg.setFrom(new InternetAddress("NO_REPLY@ScarletTaxi.com", "Scarlet Taxi Notification System"));
        		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.mobileNumber, recipient.username));
        		msg.setText(n.text);
        		Transport.send(msg);
        	}

        } catch (AddressException e) {
            // ...
        	return false;
        } catch (MessagingException e) {
            // ...
        	return false;
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Used for exporting notification with multiple recipients
	 * @param n
	 */
	public static boolean exportMultipleNotifications(Notification n){
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        //check to make sure notification type accommodates multiple recipients
        if(n.type != NType.RideCancel && n.type != NType.TimeReminder){
        	return false;
        }
        for(int i=0;i<n.recipients.length;i++){
	        User recipient = n.recipients[i];
	        try {
	        	if(recipient.receiveEmailNotification){
	        		Message msg = new MimeMessage(session);
	        		msg.setFrom(new InternetAddress("NO_REPLY@ScarletTaxi.com", "Scarlet Taxi Notification System"));
	        		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.email, recipient.username));
	        		msg.setSubject("Scarlet Taxi Notification");
	        		msg.setText(n.text);
	        		Transport.send(msg);
	        	}
	        	if(recipient.receiveSMSNOtification){
	        		Message msg = new MimeMessage(session);
	        		msg.setFrom(new InternetAddress("NO_REPLY@ScarletTaxi.com", "Scarlet Taxi Notification System"));
	        		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.mobileNumber, recipient.username));
	        		msg.setText(n.text);
	        		Transport.send(msg);
	        	}
	        } catch (AddressException e) {
	            // ...
	        	return false;
	        } catch (MessagingException e) {
	            // ...
	        	return false;
	        } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}			
	   }
       return true;
	}
}