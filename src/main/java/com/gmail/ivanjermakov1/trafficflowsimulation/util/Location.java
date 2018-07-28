package com.gmail.ivanjermakov1.trafficflowsimulation.util;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Location {
	
	private double x;
	private double y;
	
	
	public boolean equals(Location location) {
		return x == location.x && y == location.y;
	}
	
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void add(Vector speed) {
			x += speed.getX();
			y += speed.getY();
	}
	
	public static double distance(Location location1, Location location2) {
		return sqrt(pow(location1.getX() - location2.getX(), 2) + pow(location1.getY() - location2.getY(), 2));
	}
	
}
