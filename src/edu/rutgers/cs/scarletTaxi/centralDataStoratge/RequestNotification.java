package edu.rutgers.cs.scarletTaxi.centralDataStoratge;

/**
 * a notification to be sent relating to a ride request.
 * @author Isaac Yochelson
 *
 */
public class RequestNotification {
	public int requestID;
	public char notificationType;
	
	/**
	 * creates a new RequestNotification
	 * @param id
	 * @param type
	 */
	public RequestNotification (final int id, final char type) {
		this.requestID = id;
		this.notificationType = type;
	}
}
