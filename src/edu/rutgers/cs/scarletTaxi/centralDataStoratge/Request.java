package edu.rutgers.cs.scarletTaxi.centralDataStoratge;

import java.sql.Time;

public class Request {
	public User passenger;
	public Ride ride;
	public Time sent;
	public String comment;
	public String toString (){
		return passenger.username + "/n" + comment;
	}
}
