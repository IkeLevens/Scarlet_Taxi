package edu.rutgers.cs.scarletTaxi.centralDataStoratge;

import java.sql.Time;

/**
 * A ride class holds information related to a ride offer in public fields.
 * @author Isaac Yochelson
 *
 */
public class Ride {
	public Car car;
	public Location origin;
	public Location destination;
	public boolean toCampus;
	public Time departure;
	public int seatsTaken;
	
	/**
	 * This constructor creates a ride with all fields specified at the time of construction.
	 * @param car
	 * @param origin
	 * @param destination
	 * @param toCampus
	 * @param departure
	 * @param seatsTaken
	 */
	public Ride (final Car car, final Location origin, final Location destination, final boolean toCampus, final Time departure, final int seatsTaken) {
		this.car = car;
		this.origin = origin;
		this.destination = destination;
		this.toCampus = toCampus;
		this.departure = departure;
		this.seatsTaken = seatsTaken;
	}
	/**
	 * returns a string representation of this ride event.
	 */
	public String toString () {
		return car.driver.username + "/n" + origin.locationName + "/n" + departure;
	}
}
