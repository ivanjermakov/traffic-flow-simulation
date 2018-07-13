package com.gmail.ivanjermakov1.trafficflowsimulation.util;

public class Vector {
	
	private double x;
	private double y;
	
	private double length;
	private double direction;
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
		
		length = Math.sqrt(Math.pow(x, 2) * Math.pow(y, 2));
		direction = Math.tan(this.x / this.y);
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getLength() {
		return length;
	}
	
	public double getDirection() {
		return direction;
	}
}
