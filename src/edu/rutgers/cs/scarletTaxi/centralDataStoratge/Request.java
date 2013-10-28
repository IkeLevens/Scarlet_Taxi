package edu.rutgers.cs.scarletTaxi.centralDataStoratge;

import java.sql.Time;

/**
 * The Request class holds information relating to a ride request in public fields.
 * @author Isaac Yochelson
 *
 */
public class Request {
	public User passenger;
	public Ride ride;
	public Time sent;
	public String comment;
	
	/**
	 * This constructor creates a Request object with all fields specified at time of construction.
	 * @param passenger
	 * @param ride
	 * @param sent
	 * @param comment
	 */
	public Request (final User passenger, final Ride ride, final Time sent, final String comment) {
		this.passenger = passenger;
		this.ride = ride;
		this.sent = sent;
		this.comment = comment;
	}
	/**
	 * returns a string representation of this request.
	 */
	public String toString (){
		return passenger.username + "/n" + comment;
	}
}
