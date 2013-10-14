package edu.rutgers.cs.scarletTaxi.centralDataStoratge;

public class User {
	public int userID;
	public String name;
	public String username;
	public byte[] password;
	public String email;
	public Address address;
	public String mobileNumber;
	public boolean receiveEmailNotification;
	public boolean receiveSMSNOtification;
	public String toString () {
		return username;
	}
}
