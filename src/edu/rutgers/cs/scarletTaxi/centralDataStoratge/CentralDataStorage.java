package edu.rutgers.cs.scarletTaxi.centralDataStoratge;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class provides static methods to retrieve Objects of the User, Car, Ride,
 * and Request classes.  These objects provide access to the stored information of the Central Data
 * Storage module.  This class also provides static methods for updating and removing records from
 * the database.
 * @author Isaac Yochelson
 *
 */
public abstract class CentralDataStorage {
	public static Address getAddress(int addressID) {
		Address address = null;
		ResultSet results = runQuery("SELECT streedAddress, city, zipCode FROM addresses WHERE "
				+ "addressID= " + addressID);
		if (results == null) {
			return null;
		}
		try {
			if (results.next()) {
				address = new Address(
						results.getString("streetAddress"),
						results.getString("city"),
						results.getInt("zipCode"));
			} else {
				return null;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return address;
	}
	/**
	 * This method returns a userID for a user whose username and email are given.  All userIDs
	 * will be non-negative.  The caller must test for negative returns!
	 * @param username
	 * @param email
	 * @return returns a userID if the username and email are associated with a user, and -1 if
	 * there is no matching user.
	 */
	public static int getUserID (final String username, final String email) {
		int userID = 0;
		ResultSet results = runQuery("SELECT userID FROM users WHERE userName = \""
				+ username + "\" AND email = \""
				+ email + "\";");
		if (results == null) {
			return -1;
		}
		try {
			if(results.next()) {
				userID = results.getInt("userID");
			} else {
				return -1;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return userID;
	}
	/**
	 * This method retrieves a user by userID.
	 * @param userID
	 * @return The User, if any, matching the userID.  This must be tested for null!
	 */
	public static User getUser (final int userID) {
		User user = null;
		ResultSet results = runQuery("SELECT userID, memberName, userName, password, email, address, "
				+ "mobileNumber, receiveEmailNotification, receiveSMSNotification from users"
				+ "WHERE userID = " + userID);
		if (results == null) {
			return null;
		}
		try {
			if (results.next()) {
				user = new User(
						results.getInt("userID"),
						results.getString("memberName"),
						results.getString("userName"),
						results.getBytes("password"),
						results.getString("email"),
						getAddress(results.getInt("address")),
						results.getString("mobileNumber"),
						results.getBoolean("receiveEmailNotification"),
						results.getBoolean("receiveSMSNotification"));
			} else {
				return null;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return user;
	}
	/**
	 * get a userID from a User object
	 * @param user User object
	 * @return userID
	 */
	private static int getUserID (User user) {
		return user.userID;
	}
	/**
	 * This method retrieves a user by userName and password hash.  This can be used for user
	 * verification on incoming communications.
	 * @param userName
	 * @param password this is a salted and hashed password stored as an array of bytes.
	 * @return The user, if any, matching the username and password.  This must be tested for null!
	 */
	public static User getUser (final String userName, final byte[] password) {
		User user = null;
		ResultSet results = runQuery("SELECT userID, memberName, userName, password, email, address, "
				+ "mobileNumber, receiveEmailNotification, receiveSMSNotification from users"
				+ "WHERE userName = " + userName
				+ " AND password = " + password);
		if (results == null) {
			return null;
		}
		try {
			if (results.next()) {
				user = new User(
						results.getInt("userID"),
						results.getString("memberName"),
						results.getString("userName"),
						results.getBytes("password"),
						results.getString("email"),
						getAddress(results.getInt("address")),
						results.getString("mobileNumber"),
						results.getBoolean("receiveEmailNotification"),
						results.getBoolean("receiveSMSNotification"));
			} else {
				return null;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return user;
	}
	/**
	 * Retrieves a Request based on the 
	 * @param requestID
	 * @return The request, if any, matching the requestID.  This must be tested for null!
	 */
	public static Request getRequest (final int requestID) {
		Request request = null;
		ResultSet results = runQuery("SELECT requestingUser, ride, sent, requestComment FROM requests WHERE requestID = " + requestID);
		if (results == null) {
			return null;
		}
		try {
			if (results.next()) {
				User passenger = getUser(results.getInt("requestingUser"));
				Ride ride = getRide(results.getInt("ride"));
				request = new Request(
						passenger,
						ride,
						results.getTime("sent"),
						results.getString("requestComment"));
			} else {
				return null;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return request;
	}
	/**
	 * Retrieves a list of requests made by a specific user.
	 * @param userID
	 * @return A list of all requests made by the specified user.  This list may be empty if either
	 * there is no such user or the user has made no requests.
	 */
	public static List<Request> getRequests (final int userID) {
		ResultSet requestIDs = runQuery("SELECT requestID from requests WHERE requestingUser = " + userID);
		if (requestIDs == null) {
			return null;
		}
		ArrayList<Request> requests = new ArrayList<Request>();
		try {
			while (requestIDs.next()) {
				requests.add(getRequest(requestIDs.getInt("requestID")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requests;
	}
	/**
	 * Retrieves information about a ride by its rideID.
	 * @param rideID
	 * @return A Ride object matching this rideID, if one exists, will be returned.  THis must be
	 * checked for null!
	 */
	public static Ride getRide (final int rideID) {
		ResultSet results = runQuery("SELECT car, origin, destination, toCampus, departure, seatsTaken from rides WHERE rideID = "+ rideID);
		if (results == null) {
			return null;
		}
		Ride ride = null;
		try {
			if (results.next()) {
				Car car = getCar(results.getInt("car"));
				Location origin = getLocation(results.getInt("origin"));
				Location destination = getLocation(results.getInt("destination"));
				ride = new Ride(
						car,
						origin,
						destination,
						results.getBoolean("toCampus"),
						results.getTime("departure"),
						results.getInt("seatsTaken")
				);
			} else {
				return null;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return ride;
	}
	/**
	 * Retrieves information about all ride offers a specific user has made.
	 * @param userID
	 * @return A list of all ride offers, if any, made by the specified user.  This list may be
	 * empty if either there is no such user or the user has made no ride offers.
	 */
	public static List<Ride> getRides (final int userID) {
		ResultSet cars = runQuery("SELECT carID FROM cars WHERE driver = " + userID);
		if (cars == null) {
			return null;
		}
		ArrayList<Ride> rides = new ArrayList<Ride>();
		try {
			while (cars.next()) {
				ResultSet rideIDs = runQuery("SELECT rideID from rides WHERE car = " + cars.getInt("carID"));
				if (rideIDs == null) {
					continue;
				}
				while (rideIDs.next()) {
					rides.add(getRide(rideIDs.getInt("rideID")));
				}
			}
		} catch (SQLException e) {
			System.err.println (e.getMessage());
		}
		return rides;
	}
	private static Car getCar (final int carID){
		ResultSet results = runQuery("SELECT driver, make, bodyStyle, color, seats FROM cars WHERE carID = " + carID);
		if (results == null) {
			return null;
		}
		Car car = null;
		try {
			User driver = getUser(results.getInt("driver"));
			car = new Car(
					driver,
					results.getString("make"),
					Car.getBodyStyle(results.getInt("bodyStyle")),
					Car.getColor(results.getInt("color")),
					results.getInt("seats")
			);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
	/**
	 * Retrieves a list of all cars registered to a specific user.
	 * @param userID
	 * @return A list of all cars, if any, registered to the user in the database.  This list can
	 * be empty if either there is no such user or the user has not registered any cars.
	 */
	public static List<Car> getCars(final int userID){
		ResultSet carIDs = runQuery("SELECT carID FROM cars WHERE driver = " + userID);
		if (carIDs == null) {
			return null;
		}
		ArrayList<Car> cars = new ArrayList<Car>();
		try {
			while (carIDs.next()) {
				cars.add(getCar(carIDs.getInt("carID")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cars;
	}
	/**
	 * Add a new user to the database
	 * @param newUser
	 * @return Returns true if the user was added successfully, and false otherwise.
	 */
	public static boolean addUser (final User newUser) {
		int rowsAffected = runUpdate("INSERT INTO users (memberName, userName, memberPassword, email, address, mobileNumber, receiveEmailNotification, receiveSMSNotification)VALUES ("
				+ newUser.name + ", "
				+ newUser.username + ", "
				+ newUser.password + ", "
				+ newUser.email + ", "
				+ newUser.address + ", "
				+ newUser.mobileNumber + ", "
				+ newUser.receiveEmailNotification + ", "
				+ newUser.receiveSMSNOtification + ")"
		);
		return (rowsAffected > 0);
	}
	/**
	 * Remove a user from the database by userID
	 * @param userID
	 * @return returns true if the user was found and successfully removed, and false otherwise.
	 */
	public static boolean removeUser (final int userID) {
		int rowsAffected = runUpdate("DELETE FROM users WHERE userID = " + userID);
		return (rowsAffected > 0);
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
		int rowsAffected = runUpdate("UPDATE users SET memberName=" + newData.name + ", userName="
	+ newData.username +  ", memberPassword=" + newData.password + ", email=" + newData.email
	+ ", address=" + getAddressID(newData.address) + ", mobileNumber=" + newData.mobileNumber
	+ ", receiveEmailNotification=" + newData.receiveEmailNotification + ", receiveSMSNotification="
	+ newData.receiveSMSNOtification  + " WHERE userID = " + userID);
		return (rowsAffected > 0);
	}
	private static int getAddressID(Address address) {
		return 0;
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
	private static int getCarID (Car car) {
		int carID= 0;
		ResultSet results = runQuery("SELECT carID FROM cars WHERE driver = "
				+ car.driver.userID + " AND make = \""
				+ car.make + "\";");
		if (results == null) {
			return 0;
		}
		try {
			carID = results.getInt("carID");
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return carID;
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
	private static int getRideId(Ride ride) {
		int rideID = 0;
		int car = getCarID(ride.car);
		ResultSet results = runQuery("SELECT rideID FROM rides WHERE car = "
				+ car + " AND departure = "
				+ ride.departure + ";");
		if (results == null) {
			return 0;
		}
		try {
			rideID = results.getInt("rideID");
		} catch (SQLException e){
			System.err.println(e.getMessage());
		}
		return rideID;
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
	 * removes the ride from the database whose driver matches the userID field and whose time is
	 * the closest to the current time in the future.
	 * @param userID
	 * @return returns true if a future ride for the user was found and removed successfully, and
	 * false otherwise.
	 */
	public static boolean removeNextRide (final int userID) {
		return false;
	}
	/**
	 * adds a request to the database
	 * @param newRequest
	 * @return returns true if the request was added successfully, and false otherwise.
	 */
	public static boolean addRequest (final Request newRequest) {
		int ride = 0;
		int affectedRows = runUpdate (
				"INSERT INTO requests (requestingUser, ride, sent, requestComment) VALUES ("
				+ newRequest.passenger + ", "
				+ ride + ", "
				+newRequest.comment + ");");
		if (affectedRows > 0) {
			return true;
		}
		return false;
	}
	/**
	 * removes a request from the database by its requestID.
	 * @param requestID
	 * @return returns true if the request was found and successfully removed, and false otherwise.
	 */
	public static boolean removeRequest (final int requestID) {
		int affectedRows = runUpdate("DELETE FROM requests WHERE requestID = " +  requestID);
		if (affectedRows > 0) {
			return true;
		}
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
	/**
	 * removes the ride request from the database whose passenger matches the userID field and
	 * whose time is the closest to the current time in the future.
	 * @param userID
	 * @return returns true if a future ride request for the user was found and successfully
	 * removed, and false otherwise.
	 */
	public static boolean removeNextRequest (final int userID) {
		return false;
	}
	public static List<RequestNotification> getRequestNotifications () {
		return null;
	}
	public static List<RideNotification> getRideNotifications () {
		return null;
	}
	private static Location getLocation (final int locationID) {
		return null;
	}
	/**
	 * runs a query on the MySQL server referenced in PasswordProtector.
	 * @param query The query to be run, in string form.
	 * @return The results of the query, or null in the case of a SQLException being thrown.
	 */
	private static ResultSet runQuery (String query) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(
					PasswordProtector.HOST,
					PasswordProtector.USER,
					PasswordProtector.PASSWORD);
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			connection.close();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					System.err.println(e.getMessage());
				}
			}
		return null;
	}
	/**
	 * updates the MySQL server referenced in PasswordProtector.
	 * @param update the update to be effected in string form.
	 * @return the number of effected rows, or 0 in the case of a SQLException being thrown.
	 */
	private static int runUpdate (String update) {
		Connection connection= null;
		try {
			connection = DriverManager.getConnection(
					PasswordProtector.HOST,
					PasswordProtector.USER,
					PasswordProtector.PASSWORD);
			Statement statement = connection.createStatement();
			int result = statement.executeUpdate(update);
			connection.close();
			return result;
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			} finally {
				try {
					connection.close();
				} catch (SQLException e){
					System.err.println(e.getMessage());
				}
			}
		return 0;
	}
}
