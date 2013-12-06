package edu.rutgers.cs.scarletTaxi.centralDataStoratge;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import edu.rutgers.cs.scarletTaxi.centralDataStoratge.Car.BodyStyle;
import edu.rutgers.cs.scarletTaxi.centralDataStoratge.Car.Color;

/**
 * This abstract class provides static methods to retrieve Objects of the User, Car, Ride,
 * and Request classes.  These objects provide access to the stored information of the Central Data
 * Storage module.  This class also provides static methods for updating and removing records from
 * the database.
 * @author Isaac Yochelson
 *
 */
public abstract class CentralDataStorage {
	
	private static Connection connection;
	
	/**
	 * Retrieves an Address object for a given addressID.
	 * @param addressID
	 * @return An Address object encapsulating the data from the record with the given ID.11
	 */
	public static Address getAddress(int addressID) {
		Address address = null;
		ResultSet results = runQuery("SELECT streedAddress, city, zipCode FROM addresses WHERE "
				+ "addressID= " + addressID);
		if (results == null) {
			if (connection != null) {
				closeConnection();
			}
			return null;
		}
		try {
			if (results.next()) {
				address = new Address(
						results.getString("streetAddress"),
						results.getString("city"),
						results.getInt("zipCode"));
			} else {
				if (connection != null) {
					closeConnection();
				}
				return null;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		if (connection != null) {
			closeConnection();
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
			if (connection != null) {
				closeConnection();
			}
			return -1;
		}
		try {
			if(results.next()) {
				userID = results.getInt("userID");
			} else {
				if (connection != null) {
					closeConnection();
				}
				return -1;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		if (connection != null) {
			closeConnection();
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
				+ "mobileNumber, receiveEmailNotification, receiveSMSNotification, carrier from users"
				+ "WHERE userID = " + userID);
		if (results == null) {
			if (connection != null) {
				closeConnection();
			}
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
						results.getBoolean("receiveSMSNotification"),
						results.getString("carrier").charAt(0));
			} else {
				if (connection != null) {
					closeConnection();
				}
				return null;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		if (connection != null) {
			closeConnection();
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
				+ "mobileNumber, receiveEmailNotification, receiveSMSNotification, carrier from users"
				+ "WHERE userName = " + userName
				+ " AND password = " + password);
		if (results == null) {
			if (connection != null) {
				closeConnection();
			}
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
						results.getBoolean("receiveSMSNotification"),
						results.getString("carrier").charAt(0));
			} else {
				if (connection != null) {
					closeConnection();
				}
				return null;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		if (connection != null) {
			closeConnection();
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
			if (connection != null) {
				closeConnection();
			}
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
				if (connection != null) {
					closeConnection();
				}
				return null;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		if (connection != null) {
			closeConnection();
		}
		return request;
	}
	private static int getRequestID (final Request request) {
		ResultSet results = runQuery("SELECT requestID FROM requests WHERE requestingUser = " + request.passenger + " AND ride = " + getRideId(request.ride));
		int id = 0;
		if (results == null) {
			if (connection != null) {
				closeConnection();
			}
			return 0;
		}
		try {
			if (results.next()) {
				id = results.getInt("requestID");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		if (connection != null) {
			closeConnection();
		}
		return id;
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
			if (connection != null) {
				closeConnection();
			}
			return null;
		}
		ArrayList<Request> requests = new ArrayList<Request>();
		try {
			while (requestIDs.next()) {
				requests.add(getRequest(requestIDs.getInt("requestID")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (connection != null) {
			closeConnection();
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
			if (connection != null) {
				closeConnection();
			}
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
				if (connection != null) {
					closeConnection();
				}
				return null;
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		if (connection != null) {
			closeConnection();
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
			boolean connectionOpen = false;
			while (cars.next()) {
				ResultSet rideIDs = runQuery("SELECT rideID from rides WHERE car = " + cars.getInt("carID"), connectionOpen);
				connectionOpen = true;
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
		if (connection != null) {
			closeConnection();
		}
		return rides;
	}
	private static Car getCar (final int carID){
		ResultSet results = runQuery("SELECT driver, make, bodyStyle, color, seats FROM cars WHERE carID = " + carID);
		if (results == null) {
			if (connection != null) {
				closeConnection();
			}
			return null;
		}
		Car car = null;
		try {
			if (results.next()) {
				User driver = getUser(results.getInt("driver"));
				car = new Car(
						driver,
						results.getString("make"),
						Car.getBodyStyle(results.getInt("bodyStyle")),
						Car.getColor(results.getInt("color")),
						results.getInt("seats")
				);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		if (connection != null) {
			closeConnection();
		}
		return car;
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
			if (connection != null) {
				closeConnection();
			}
			return null;
		}
		ArrayList<Car> cars = new ArrayList<Car>();
		try {
			while (carIDs.next()) {
				cars.add(getCar(carIDs.getInt("carID")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (connection != null) {
			closeConnection();
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
		int id = 0;
		ResultSet results = runQuery("SELECT addressID FROM addresses WHERE streetAddress = " + address.streetAddress
				+ " AND city = " + address.city
				+ " and zipCode = " + address.zipCode
		);
		if (results == null) {
			if (connection != null) {
				closeConnection();
			}
			return 0;
		}
		try {
			if (results.next()) {
				id = results.getInt("addressID");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		if (connection != null) {
			closeConnection();
		}
		return id;
	}
	/**
	 * add a car to the database.
	 * @param newCar the data to be added.  It is important that the proper User object be in the
	 * driver field of newCar.
	 * @return returns true if the new car was added successfully, and false otherwise.
	 */
	public static boolean addCar (final Car newCar) {
		int rowsAffected = runUpdate(
				"INSERT INTO cars (driver, make, color, bodyStyle, seats) VALUES (" + getUserID(newCar.driver)
				+ ", " + newCar.make
				+ ", " + getColor(newCar.color)
				+ ", " + getBodyStyle(newCar.bodyStyle)
				+ ", " + newCar.seats + ")");
		return (rowsAffected > 0);
	}
	private static int getBodyStyle(BodyStyle bodyStyle) {
		ResultSet results = runQuery("SELECT styleID FROM bodyStyles WHERE styleName = " + bodyStyle);
		if (results == null) {
			if (connection != null) {
				closeConnection();
			}
			return 0;
		}
		int styleID = 0;
		try {
			if (results.next()) {
				styleID = results.getInt("styleID");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		if (connection != null) {
			closeConnection();
		}
		return styleID;
	}
	private static int getColor(Color color) {
		ResultSet results = runQuery("SELECT colorID FROM colors WHERE colorName = " + color);
		if (results == null) {
			if (connection != null) {
				closeConnection();
			}
			return 0;
		}
		int colorID = 0;
		try {
			if (results.next()) {
				colorID = results.getInt("colorID");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		if (connection != null) {
			closeConnection();
		}
		return colorID;
	}
	private static int getCarID (Car car) {
		int carID= 0;
		ResultSet results = runQuery("SELECT carID FROM cars WHERE driver = "
				+ car.driver.userID + " AND make = \""
				+ car.make + "\";");
		if (results == null) {
			if (connection != null) {
				closeConnection();
			}
			return 0;
		}
		try {
			if (results.next()){
				carID = results.getInt("carID");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		if (connection != null) {
			closeConnection();
		}
		return carID;
	}
	/**
	 * removes a specific car from the database.
	 * @param CarID
	 * @return returns true if the specified car was successfully removed, and false otherwise.
	 */
	public static boolean removeCar (final int carID) {
		int rowsAffected = runUpdate("DELETE FROM cars WHERE CarID = " + carID);
		return (rowsAffected > 0);
	}
	/**
	 * adds a ride to the database.
	 * @param newRide
	 * @return returns true if the ride was added successfully, and false otherwise.
	 */
	public static boolean addRide (final Ride newRide) {
		int rowsAffected = runUpdate("INSERT INTO rides (car, origin, destination, toCampus, departure, seatsTaken) VALUES ("
				+ getCarID(newRide.car) + ", "
				+ getLocationID(newRide.origin) + ", "
				+ getLocationID(newRide.destination) + ", "
				+ newRide.toCampus + ", "
				+ newRide.departure + ", "
				+ newRide.seatsTaken + ")"
		);
		return (rowsAffected > 0);
	}
	/**
	 * removes a ride from the database by its rideID.
	 * @param rideID
	 * @return returns true if the ride was found and removed successfully, and false otherwise.
	 */
	public static boolean removeRide (final int rideID) {
		int rowsAffected = runUpdate("DELETE FROM rides WHERE rideID = " + rideID);
		return (rowsAffected > 0);
	}
	private static int getRideId(Ride ride) {
		int rideID = 0;
		int car = getCarID(ride.car);
		ResultSet results = runQuery("SELECT rideID FROM rides WHERE car = "
				+ car + " AND departure = "
				+ ride.departure + ";");
		if (results == null) {
			if (connection != null) {
				closeConnection();
			}
			return 0;
		}
		try {
			if (results.next()) {
				rideID = results.getInt("rideID");
			}
		} catch (SQLException e){
			System.err.println(e.getMessage());
		}
		if (connection != null) {
			closeConnection();
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
		List<Car> cars = getCars(userID);
		int totalRowsAffected = 0;
		for (Car car : cars) {
			int rowsAffected = runUpdate("DELETE FROM rides WHERE car = " + getCarID(car) + " AND departure = " + departure);
			totalRowsAffected += rowsAffected;
		}
		return (totalRowsAffected > 0);
	}
	/**
	 * removes the ride from the database whose driver matches the userID field and whose time is
	 * the closest to the current time in the future.
	 * @param userID
	 * @return returns true if a future ride for the user was found and removed successfully, and
	 * false otherwise.
	 */
	public static boolean removeNextRide (final int userID) {
		ResultSet results = runQuery("SELECT rideID FROM rides  WHERE car = (SELECT carID FROM cars WHERE driver = " + userID + ") ORDER BY departure ASC LIMIT 1");
		if (results == null) {
			if (connection != null) {
				closeConnection();
			}
			return false;
		}
		int rideID = Integer.MAX_VALUE;
		try {
			if(results.next()) {
				rideID = results.getInt("rideID");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		int rowsAffected = runUpdate("DELETE FROM rides WHERE rideID = " + rideID);
		if (connection != null) {
			closeConnection();
		}
		return (rowsAffected > 0);
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
		return (affectedRows > 0);
	}
	/**
	 * removes a request from the database by its requestID.
	 * @param requestID
	 * @return returns true if the request was found and successfully removed, and false otherwise.
	 */
	public static boolean removeRequest (final int requestID) {
		int affectedRows = runUpdate("DELETE FROM requests WHERE requestID = " +  requestID);
		return (affectedRows > 0);
	}
	/**
	 * removes a request by the passenger and departure time.
	 * @param userID
	 * @param departure
	 * @return returns true if the request was found and successfully removed, and false otherwise.
	 */
	public static boolean removeRequest (final int userID, final Time departure) {
		List<Request> requests = getRequests(userID);
		int totalRowsAffected = 0;
		for (Request request : requests) {
			int rowsAffected = runUpdate("DELETE FROM requests WHERE ride = " + getRequestID(request) + " AND departure = " + departure);
			totalRowsAffected += rowsAffected;
		}
		return (totalRowsAffected > 0);
	}
	/**
	 * removes the ride request from the database whose passenger matches the userID field and
	 * whose time is the closest to the current time in the future.
	 * @param userID
	 * @return returns true if a future ride request for the user was found and successfully
	 * removed, and false otherwise.
	 */
	public static boolean removeNextRequest (final int userID) {
		int requestID = 0;
		ResultSet results = runQuery("SELECT requestID FROM requests WHERE ride = (SELECT rideID FROM rides WHERE car = (SELECT carID  FROM cars WHERE driver = " + userID
				+"))ORDER BY (SELECT departure FROM rides WHERE car = (SELECT carID FROM cars WHERE driver = " + userID
				+ ")) ASC LIMIT 1");
		if (results == null) {
			if (connection != null) {
				closeConnection();
			}
			return false;
		}
		try {
			if (results.next()) {
				requestID = results.getInt("requestID");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		int rowsAffected = runUpdate("DELETE FROM requests WHERE requestID = " + requestID);
		if (connection != null) {
			closeConnection();
		}
		return (rowsAffected > 0);
	}
	/**
	 * returns a list of all RequestNotifications encapsulating all notifications in the table, then deletes
	 * them from the table.
	 * @return
	 */
	public static List<RequestNotification> getRequestNotifications () {
		ArrayList<RequestNotification> list = new ArrayList<RequestNotification>();
		ResultSet results = runQuery("SELECT request, notificationType FROM requestNotifications");
		runUpdate("DELETE FROM requestNotifications WHERE notificationID > 0");
		if (results == null) {
			if (connection != null) {
				closeConnection();
			}
			return null;
		}
		try {
			while (results.next()) {
				list.add(new RequestNotification(
						results.getInt("request"),
						results.getString("notificationType").charAt(0)
						));
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		if (connection != null) {
			closeConnection();
		}
		return list;
	}
	/**
	 * returns a list of all RideNotifications encapsulating all notifications in the table, then deletes
	 * them from the table.
	 * @return
	 */
	public static List<RideNotification> getRideNotifications () {
		ArrayList<RideNotification> list = new ArrayList<RideNotification>();
		ResultSet results = runQuery("SELECT ride, notificationType FROM rideNotifications");
		runUpdate("DELETE FROM rideNotifications WHERE notificationID > 0");
		if (results == null) {
			if (connection != null) {
				closeConnection();
			}
			return null;
		}
		try {
			while (results.next()) {
				list.add(new RideNotification(
						results.getInt("ride"),
						results.getString("notificationType").charAt(0)
						));
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		if (connection != null) {
			closeConnection();
		}
		return list;
	}
	/**
	 * returns the ID of the requested Location if it exists in the locations table
	 * @param location
	 * @return the id of the Location
	 */
	private static int getLocationID (Location location) {
		ResultSet results = runQuery("SELECT locationID FROM locations WHERE locationName = "
				+ location.locationName + " and campus = "
				+ location.campus);
		if (results == null) {
			if (connection != null) {
				closeConnection();
			}
			return 0;
		}
		int id = 0;
		try {
			if (results.next()) {
				id = results.getInt("locationID");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		if (connection != null) {
			closeConnection();
		}
		return id;
	}
	/**
	 * returns a Location by its ID
	 * @param locationID
	 * @return Location with ID given
	 */
	private static Location getLocation (final int locationID) {
		ResultSet results = runQuery("SELECT locationName, locationAddress, campus FROM locations WHERE locationID = " + locationID);
		if (results == null) {
			if (connection != null) {
				closeConnection();
			}
			return null;
		}
		Location location = null;
		try {
			if (results.next()) {
				location = new Location(
						results.getString("locationName"),
						getAddress(results.getInt("locationAddress")),
						results.getString("campus")
						);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		if (connection != null) {
			closeConnection();
		}
		return location;
	}
	/**
	 * runs a query on the MySQL server referenced in PasswordProtector.
	 * @param query The query to be run, in string form.
	 * @return The results of the query, or null in the case of a SQLException being thrown.
	 */
	private static ResultSet runQuery (String query) {
		ResultSet result = null;
		try {
			connection = DriverManager.getConnection(
					PasswordProtector.HOST,
					PasswordProtector.USER,
					PasswordProtector.PASSWORD);
			Statement statement = connection.createStatement();
			result = statement.executeQuery(query);
			connection.close();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		return result;
	}
	/**
	 * runs a query on the MySQL server referenced in PasswordProtector.
	 * @param query The query to be run, in string form.
	 * @return The results of the query, or null in the case of a SQLException being thrown.
	 */
	private static ResultSet runQuery (String query, boolean connectionOpen) {
		ResultSet result = null;
		try {
			if (!connectionOpen) {
				connection = DriverManager.getConnection(
						PasswordProtector.HOST,
						PasswordProtector.USER,
						PasswordProtector.PASSWORD);
			}
			Statement statement = connection.createStatement();
			result = statement.executeQuery(query);
			connection.close();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		return result;
	}
	private static void closeConnection () {
		try {
			connection.close();
			connection = null;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
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
	
	/**
	 * This method retrieves all users that have a request with the given ride.
	 * @param rideID
	 * @return Users who have requests containing a matching rideID.
	 */
	public static User[] getUsers (final int rideID) {
		List<User> users = new ArrayList<User>();
		User[] usersA=null;
		User user;
		ResultSet results = runQuery("SELECT U.userID, U.memberName, U.userName, U.password, U.email, U.address, "
				+ "U.mobileNumber, U.receiveEmailNotification, U.receiveSMSNotification, U.carrier from users U,requests R"
				+ "WHERE R.userID = U.userID AND R.rideID = " + rideID);
		if (results == null) {
			if (connection != null) {
				closeConnection();
			}
			return null;
		}
		try {
			while (results.next()) {
				user = new User(
						results.getInt("userID"),
						results.getString("memberName"),
						results.getString("userName"),
						results.getBytes("password"),
						results.getString("email"),
						getAddress(results.getInt("address")),
						results.getString("mobileNumber"),
						results.getBoolean("receiveEmailNotification"),
						results.getBoolean("receiveSMSNotification"),
						results.getString("carrier").charAt(0));
				users.add(user);
			}
			usersA = new User[users.size()];
			users.toArray(usersA);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		if (connection != null) {
			closeConnection();
		}
		return usersA;
	}
}
