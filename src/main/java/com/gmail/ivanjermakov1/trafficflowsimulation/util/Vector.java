package com.gmail.ivanjermakov1.trafficflowsimulation.util;

import com.gmail.ivanjermakov1.trafficflowsimulation.type.DrivingDirection;

public class Vector {
	
	private static final double MIN_VALUE = 0.001;
	
	private double x;
	private double y;
	
	private double length;
	private double direction;
	
	public static Vector createVector(DrivingDirection drivingDirection, double coefficient) {
		switch (drivingDirection) {
			case TOP:
				return new Vector(0, -coefficient);
			case RIGHT:
				return new Vector(coefficient, 0);
			case DOWN:
				return new Vector(0, coefficient);
			case LEFT:
				return new Vector(-coefficient, 0);
		}
		
		return null;
	}
	
	public Vector() {
		x = 0;
		y = 0;
		length = 0;
		direction = 0;
	}
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
		
		length = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
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
	
	public void multiply(double coefficient) {
		x *= coefficient;
		y *= coefficient;
		length = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public void add(Vector vector) {
		x += vector.x;
		y += vector.y;
		length = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public void limit(double limit) {
		if (length <= limit) return;
		
		double k = length / limit;
		x /= k;
		y /= k;
		
		length = limit;
	}
	
	public void sub(double substitute) {
		double newLength = length - substitute;
		if (newLength < MIN_VALUE) newLength = 0;
		if (length == 0) {
			x = 0;
			y = 0;
			return;
		}
		
		double k = newLength / length;
		x *= k;
		y *= k;
		
		length = newLength;
	}
	
}
