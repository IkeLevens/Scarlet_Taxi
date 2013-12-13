package edu.rutgers.cs.scarletTaxi.removal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.rutgers.cs.scarletTaxi.centralDataStoratge.PasswordProtector;

/**
 * This class removes rides and requests from the central data storage after the rides happen.
 * @author Isaac Yochelson
 *
 */
public class Remover implements Runnable {
	/**
	 * How often the purging is done.
	 */
	private static final long INTERVAL = PasswordProtector.INTERVAL;
	/**
	 * A connection to the mySQL server.
	 */
	private Connection connection;
	/**
	 * This is used instead of true in the while loop to allow interruption to exit the program
	 * gracefully.
	 */
	private boolean running = true;
	/**
	 * overwrites the abstract method Runnable.run(), so that this method is run when a thread is
	 * created with an object of this class and Thread.start() is called on it.
	 */
	public void run() {
		ResultSet rides = null;
		while (running) {
			openConnection();
			try {
				Statement rideStatement = connection.createStatement();
				rides = rideStatement.executeQuery("SELECT rideID, departure - CURTIME() as time FROM rides");
				while (rides.next()) {
					double time = rides.getDouble("time");
					// This is a check for if a ride occurred in the last INTERVAL milliseconds.
					if ((time > 0 && time < INTERVAL / 600) || time > 240000 - (INTERVAL / 600)) {
						int ride = rides.getInt("rideID");
						Statement requestStatement = connection.createStatement();
						requestStatement.executeUpdate("DELETE FROM requests WHERE ride = " + ride);
						requestStatement = connection.createStatement();
						requestStatement.executeUpdate("DELETE FROM rides WHERE rideID = " + ride);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeConnection();
			}
			try {
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
				running = false;
			}
		}
	}
	/**
	 * Open the connection to the mySQL server.
	 */
	private void openConnection () {
		try {
			connection = DriverManager.getConnection(
					PasswordProtector.HOST,
					PasswordProtector.USER,
					PasswordProtector.PASSWORD);
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
	}
	/**
	 * Close the connection to the mySQL server.
	 */
	private void closeConnection () {
		try {
			connection.close();
			connection = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
