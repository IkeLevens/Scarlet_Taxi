package edu.rutgers.cs.scarletTaxi.centralDataStoratge;

import edu.rutgers.cs.scarletTaxi.centralDataStoratge.Car.BodyStyle;

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
	public Car (final User driver, final String make, final BodyStyle bodyStyle, final Color color,
			final int seats) {
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
	static Car.BodyStyle getBodyStyle(int bodyStyle) {
		// TODO Auto-generated method stub
		switch (bodyStyle) {
		case 1:
			return BodyStyle.Sedan;
		case 2:
			return BodyStyle.Coupe;
		case 3:
			return BodyStyle.Pickup;
		case 4:
			return BodyStyle.SUV;
		case 5:
			return BodyStyle.Minivan;
		case 6:
			return BodyStyle.Convertible;
		default:
			return null;
		}
	}
	static Car.Color getColor (int color) {
		switch (color) {
		case 1:
			return Color.Red;
		case 2:
			return Color.Blue;
		case 3:
			return Color.Green;
		case 4:
			return Color.Black;
		case 5:
			return Color.White;
		case 6:
			return Color.Metallic;
		case 7:
			return Color.Yellow;
		case 8:
			return Color.Orange;
		default:
			return null;
		}
	}
}
