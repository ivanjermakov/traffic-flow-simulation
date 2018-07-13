package com.gmail.ivanjermakov1.trafficflowsimulation.util;

public class Location {
	
	private int x;
	private int y;
	
	public Location() {
	}
	
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void add(Vector speed) {
		x += speed.getX();
		y += speed.getY();
	}
}
