package edu.rutgers.cs.scarletTaxi.cancellation;
import edu.rutgers.cs.scarletTaxi.centralDataStoratge.*;
import edu.rutgers.cs.scarletTaxi.notification_exporter.importer.Importer;
import edu.rutgers.cs.scarletTaxi.notification_exporter.process.NotificationProcessor;

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.Address;
import javax.mail.event.*;
import javax.activation.*;

import com.sun.mail.imap.*;

/**
 * This class handles the parsing of email sent by users to an address set up for this service.
 * These email will be parsed using regular expression matching.  If the correct command phrases
 * are present in the email, the origin address will be used to determine the driver or passenger
 * who sent the command.
 * @author Isaac Yochelson
 * @author Mike Wasserman
 */
public class CancelHandler implements Runnable {
	private String host;
	private String user;
	private String password;
	private String mailbox;
	private int interval;
	
	/**
	 * this constructs a new CancelHandler to hand incoming email with the given host, user,
	 * password, and mailbox.  The email server will be queried once ever interval milliseconds if
	 * it does not allow the handler to idle and await push notifications.
	 * @param host
	 * @param user
	 * @param password
	 * @param mailbox
	 * @param interval
	 */
	public CancelHandler (final String host, final String user, final String password, final String mailbox, final int interval) {
		this.host = host;
		this.user = user;
		this.password = password;
		this.mailbox = mailbox;
		this.interval = interval;
	}
	private boolean running = true;
	/**
	 * parses a Message msg for a cancel command.  If such a command is found, it will call either
	 * cancelNextRide(userID) or cancelNextRequest(userID) from CentralDataStrorage.
	 * @param msg
	 */
	private void parseMail(Message msg) {
		// handle Message msg.
		System.out.println("in ParseMail: msg = " + msg);
		try {
			String message = msg.toString();
			String From = msg.getFrom().toString();
			if(message.contains("cancel")||message.contains("Cancel")||message.contains("CANCEL")){
				String[] parts = From.split("@.");
				int number = Integer.parseInt(parts[0]);
				User driver = CentralDataStorage.getUserByPhone(number);
				int rideID = CentralDataStorage.removeNextRide(driver.userID);
				RideNotification rn = new RideNotification(rideID,'C');
				NotificationProcessor.processRideNotification(rn);
			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * in this loop, the server will periodically querry the mail server for new incoming email.
	 * If new email is found, it will call parseMain(msg) for each new mail found, then delete the
	 * mail.
	 */
	private void checkMessageLoop() {try {
	    Properties props = System.getProperties();
	    props.setProperty("mail.store.protocol", "imaps");

	    // Get a Session object
	    Session session = Session.getDefaultInstance(props, null);
	    session.setDebug(true);

	    // Get a Store object
	    Store store = session.getStore("imaps");

	    // Connect
	    store.connect(this.host, this.user, this.password);

	    // Open a Folder
	    Folder folder = store.getFolder(this.mailbox);
	    if (folder == null || !folder.exists()) {
		System.err.println("Invalid folder");
		System.exit(1);
	    }

	    folder.open(Folder.READ_WRITE);

	    // Add messageCountListener to listen for new messages
	    folder.addMessageCountListener(new MessageCountAdapter() {
			public void messagesAdded(MessageCountEvent e) {
			    Message[] msgs = e.getMessages();
			    for(Message msg : msgs) {
					parseMail(msg);
					// delete msg from server.
				}
			}
		});
			
	    // Check mail once in "interval" MILLIseconds
	    boolean supportsIdle = false;
	    try {
			if (folder instanceof IMAPFolder) {
			    IMAPFolder f = (IMAPFolder)folder;
			    f.idle();
			    supportsIdle = true;
			}
		    } catch (FolderClosedException fex) {
			throw fex;
		    } catch (MessagingException mex) {
			supportsIdle = false;
		    }
		    while (running) {
				if (supportsIdle && folder instanceof IMAPFolder) {
				    IMAPFolder f = (IMAPFolder)folder;
				    f.idle();
				    System.out.println("IDLE done");
				} else {
					try{
				    Thread.sleep(this.interval); // sleep for freq milliseconds
					}catch(InterruptedException e){
						running=false;
					}
				    // This is to force the IMAP server to send us
				    // EXISTS notifications. 
				    folder.getMessageCount();
				}
		    }
	
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	}
	/**
	 * This method halts the execution on the checkMessageLoop, after which this object will no
	 * longer be checking for incoming email commands.
	 */
	public void stopChecking() {
		this.running = false;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		checkMessageLoop();
	}
}
