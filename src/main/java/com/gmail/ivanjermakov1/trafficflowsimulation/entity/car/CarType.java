package com.gmail.ivanjermakov1.trafficflowsimulation.entity.car;

import java.util.Random;

import static com.gmail.ivanjermakov1.trafficflowsimulation.entity.car.Car.DEFAULT_LENGTH;

public enum CarType {
	NORMAL(DEFAULT_LENGTH),
	BUS(DEFAULT_LENGTH * 2);
	
	private double length;
	
	CarType(double length) {
		this.length = length;
	}
	
	public double getLength() {
		return length;
	}
	
	public static CarType getRandom() {
		return values()[new Random().nextInt(values().length)];
	}
	
}
