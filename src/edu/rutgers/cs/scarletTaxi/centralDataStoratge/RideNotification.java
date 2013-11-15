package edu.rutgers.cs.scarletTaxi.centralDataStoratge;

/**
 * a notification to be sent regarding a ride.
 * @author Isaac Yochelson
 *
 */
public class RideNotification {
	public int rideID;
	public char notificationType;
	
	/**
	 * Creates a RideNotification
	 * @param id
	 * @param type
	 */
	public RideNotification (final int id, final char type) {
		this.rideID = id;
		this.notificationType = type;
	}
}
