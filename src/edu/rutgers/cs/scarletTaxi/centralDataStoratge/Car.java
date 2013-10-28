package edu.rutgers.cs.scarletTaxi.centralDataStoratge;

/**
 * A Car maintains information about a car as public fields.
 * @author Isaac Yochelson
 *
 */
public class Car {
	public static enum BodyStyle {
		Sedan,
		Coupe,
		Pickup,
		SUV,
		Minivan,
		Convertible
	}
	public static enum Color {
		Red,
		Blue,
		Green,
		Black,
		White,
		Metallic,
		Yellow,
		Orange
	}
	public User driver;
	public String make;
	public BodyStyle bodyStyle;
	public Color color;
	public int seats;
	
	/**
	 * This constructor creats a car with all fields specified at the time of construction.
	 * @param driver
	 * @param make
	 * @param bodyStyle
	 * @param color
	 * @param seats
	 */
	public Car (final user driver, final String make, final BodyStyle bodyStyle, final Color color,
			final in seats) {
		this.driver = driver;
		this.make = make;
		this.bodyStyle = bodyStyle;
		this.color = color;
		this.seats = seats;
	}
	/**
	 * returs a string representation of this Car.
	 */
	public String toString () {
		return driver.username + "/n" + make + "/n" + color + " " + bodyStyle + "/n" + seats + " seats";
	}
}
