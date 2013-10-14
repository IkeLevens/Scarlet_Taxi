package edu.rutgers.cs.scarletTaxi.centralDataStoratge;

public class Location {
	public static enum campuses {
		Busch,
		Livingston,
		College_Ave,
		Cook,
		Douglas
	}
	public String locationName;
	public Address locationAddress;
	public Campus campus;
	public String toSring () {
		return locationName + ": " + campus;
	}
}
