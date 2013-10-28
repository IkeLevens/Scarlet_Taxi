package edu.rutgers.cs.scarletTaxi.centralDataStoratge;

/**
 * An Address holds information about an address in public fields.
 * @author Isaac Yochelson
 *
 */
public class Address {
	public String streetAddress;
	public String city;
	public int zipCode;
	
	/**
	 * This constructor creates an address with all fields specified at the time of construction.
	 * @param streetAddress
	 * @param city
	 * @param zipCode
	 */
	public Address (final String streetAddress, final String city, final int zipCode) {
		this.streetAddress = streetAddress;
		this.city = city;
		this.zipCode = zipCode;
	}
	/**
	 * returns a string representation of this address
	 */
	public String toString () {
		return streetAddress + "/n" + city + "/n" + zipCode;
	}
}
