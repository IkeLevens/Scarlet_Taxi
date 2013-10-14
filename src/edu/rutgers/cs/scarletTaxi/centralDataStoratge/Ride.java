package edu.rutgers.cs.scarletTaxi.centralDataStoratge;

import java.sql.Time;

public class Ride {
	public Car car;
	public Location origin;
	public Location destination;
	public boolean toCampus;
	public Time departure;
	public int seatsTaken;
	public String toString () {
		return car.driver.username + "/n" + origin.locationName + "/n" + departure;
	}
}
