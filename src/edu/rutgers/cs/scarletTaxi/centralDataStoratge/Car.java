package edu.rutgers.cs.scarletTaxi.centralDataStoratge;

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
	public String toString () {
		return driver.username + "/n" + make + "/n" + color + " " + bodyStyle + "/n" + seats + " seats";
	}
}
