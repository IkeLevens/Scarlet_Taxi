package edu.rutgers.cs.scarletTaxi.removal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.rutgers.cs.scarletTaxi.centralDataStoratge.PasswordProtector;

public class Remover implements Runnable {

	private static final long INTERVAL = PasswordProtector.INTERVAL;
	private Connection connection;
	private boolean running = true;
	@Override
	public void run() {
		ResultSet rides = null;
		while (running) {
			openConnection();
			try {
				Statement rideStatement = connection.createStatement();
				rides = rideStatement.executeQuery("SELECT rideID, departure - CURTIME() as time FROM rides");
				while (rides.next()) {
					double time = rides.getDouble("time");
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
	private void closeConnection () {
		try {
			connection.close();
			connection = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
