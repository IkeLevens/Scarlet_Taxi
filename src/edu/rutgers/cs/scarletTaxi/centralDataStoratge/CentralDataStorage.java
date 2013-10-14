package edu.rutgers.cs.scarletTaxi.centralDataStoratge;

import java.sql.Time;
import java.util.List;

/**
 * This class abstract class provides static methods to retreive Objects of the User, Car, Ride,
 * and Request classes.  These objects provide access to the stored information of the Central Data
 * Storage module.  This class also provides static methods for updating and removing records from
 * the database.
 * @author Isaac Yochelson
 *
 */
public abstract class CentralDataStorage {
	/**
	 * This method retrieves a user by userID.
	 * @param userID
	 * @return The User, if any, matching the userID.  This must be tested for null!
	 */
	public User getUser(final int userID) {
		return null;
	}
	/**
	 * This method retrieves a user by userName and password hash.  This can be used for user
	 * verification on incoming communications.
	 * @param userName
	 * @param password this is a salted and hashed password stored as an array of bytes.
	 * @return The user, if any, matching the username and password.  This must be tested for null!
	 */
	public static User getUser (final String userName, final byte[] password) {
		return null;
	}
	/**
	 * Retrieves a Request based on the 
	 * @param requestID
	 * @return The request, if any, matching the requestID.  THis must be tested for null!
	 */
	public static Request getRequest (final int requestID) {
		return null;
	}
	/**
	 * Retrieves a list of requests made by a specific user.
	 * @param userID
	 * @return A list of all requests made by the specified user.  This list may be empty if either
	 * there is no such user or the user has made no requests.
	 */
	public static List<Request> getRequests (final int userID) {
		return null;
	}
	/**
	 * Retrieves information about a ride by its rideID.
	 * @param rideID
	 * @return A Ride object matching this rideID, if one exists, will be returned.  THis must be
	 * checked for null!
	 */
	public static Ride getRide (final int rideID) {
		return null;
	}
	/**
	 * Retrieves information about all ride offers a specific user has made.
	 * @param userID
	 * @return A list of all ride offers, if any, made by the specified user.  This list may be
	 * empty if either there is no such user or the user has made no ride offers.
	 */
	public static List<Ride> getRides (final int userID) {
		return null;
	}
	/**
	 * Retrieves a list of all cars registered to a specific user.
	 * @param userID
	 * @return A list of all cars, if any, registered to the user in the database.  This list can
	 * be empty if either there is no such user or the user has not registered any cars.
	 */
	public static List<Car> getCars(final int userID){
		return null;
	}
	/**
	 * Add a new user to the database
	 * @param newUser
	 * @return Returns true if the user was added successfully, and false otherwise.
	 */
	public static boolean addUser (final User newUser) {
		return false;
	}
	/**
	 * Remove a user from the database by userID
	 * @param userID
	 * @return returns true if the user was found and successfully removed, and false otherwise.
	 */
	public static boolean removeUser (final int userID) {
		return false;
	}
	/**
	 * modify a user's profile information in the database
	 * @param userID The user to be modified
	 * @param newData New data that will replace the current user data.  This will be a full
	 * replacement, so it is important that any unmodified information be included in the newData
	 * object.
	 * @return returns true if the user was found and successfully modified, and false otherwise.
	 */
	public static boolean modifyUser (final int userID, final User newData) {
		return false;
	}
	/**
	 * add a car to the database.
	 * @param newCar the data to be added.  It is important that the proper User object be in the
	 * driver field of newCar.
	 * @return returns true if the new car was added successfully, and false otherwise.
	 */
	public static boolean addCar (final Car newCar) {
		return false;
	}
	/**
	 * removes a specific car from the database.
	 * @param CarID
	 * @return returns true if the specified car was successfully removed, and false otherwise.
	 */
	public static boolean removeCar (final int CarID) {
		return false;
	}
	/**
	 * adds a ride to the database.
	 * @param newRide
	 * @return returns true if the ride was added successfully, and false otherwise.
	 */
	public static boolean addRide (final Ride newRide) {
		return false;
	}
	/**
	 * removes a ride from the database by its rideID.
	 * @param rideID
	 * @return returns true if the ride was found and removed successfully, and false otherwise.
	 */
	public static boolean removeRide (final int rideID) {
		return false;
	}
	/**
	 * removes a ride from the database by the driver and departure time.
	 * @param userID
	 * @param departure
	 * @return returns true if the ride was found and removed successfully, and false otherwise.
	 */
	public static boolean removeRide (final int userID, final Time departure) {
		return false;
	}
	/**
	 * adds a request to the database
	 * @param newRequest
	 * @return returns true if the request was added successfully, and false otherwise.
	 */
	public static boolean addRequest (final Request newRequest) {
		return false;
	}
	/**
	 * removes a request from the database by its requestID.
	 * @param requestID
	 * @return returns true if the request was found and successfully removed, and false otherwise.
	 */
	public static boolean removeRequest (final int requestID) {
		return false;
	}
	/**
	 * removes a request by the passenger and departure time.
	 * @param userID
	 * @param departure
	 * @return returns true if the request was found and successfully removed, and false otherwise.
	 */
	public static boolean removeRequest (final int userID, final Time departure) {
		return false;
	}
}
