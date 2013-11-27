package edu.rutgers.cs.scarletTaxi.centralDataStoratge;

/**
 * A Location maintains information about a location as public fields.
 * @author Isaac Yochelson
 *
 */
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
	public String campus;
	
	/**
	 * This constructor creates a Location with all fields specified at the time of construction.
	 * @param name
	 * @param address
	 * @param campus
	 */
	public Location (final String name, final Address address, final String campus) {
		this.locationName = name;
		this.locationAddress = address;
		this.campus = campus;
	}
	/**
	 * returns a string representation of this Location.
	 * @return
	 */
	public String toSring () {
		return locationName + ": " + campus;
	}
}
