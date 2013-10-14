package edu.rutgers.cs.scarletTaxi.centralDataStoratge;

public class Address {
	public String streetAddress;
	public String city;
	public int zipCode;
	public String toString () {
		return streetAddress + "/n" + city + "/n" + zipCode;
	}
}
