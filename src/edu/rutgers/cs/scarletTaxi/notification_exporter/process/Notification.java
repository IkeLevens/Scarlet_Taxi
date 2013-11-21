package edu.rutgers.cs.scarletTaxi.notification_exporter.process;

import edu.rutgers.cs.scarletTaxi.centralDataStoratge.User;

/*
 * 
 * @author Michael Wasserman
 * 
 */
public class Notification {
	public final NType type;
	public final User[] recipients;
	public final User recipient;
	public final String text;
	
	public Notification(NType _type,User[] _recipients,String _text){
		this.type = _type;
		this.recipients = _recipients;
		this.recipient = null;
		this.text = _text;
	}
	
	public Notification(NType _type,User _recipient,String _text){
		this.type = _type;
		this.recipient = _recipient;
		this.recipients = null;
		this.text = _text;
	}
}

