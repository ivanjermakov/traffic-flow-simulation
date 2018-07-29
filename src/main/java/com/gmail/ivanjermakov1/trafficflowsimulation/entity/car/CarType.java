package com.gmail.ivanjermakov1.trafficflowsimulation.entity.car;

import java.util.Random;

import static com.gmail.ivanjermakov1.trafficflowsimulation.entity.car.Car.DEFAULT_FOW;
import static com.gmail.ivanjermakov1.trafficflowsimulation.entity.car.Car.DEFAULT_LENGTH;

public enum CarType {
	NORMAL(DEFAULT_LENGTH, DEFAULT_FOW, DEFAULT_FOW),
	BUS(DEFAULT_LENGTH * 2, (int)(DEFAULT_FOW * 1.5), (int)(DEFAULT_FOW * 1.5));
	
	private double length;
	private int forwardFow;
	private int backwardFow;
	
	CarType(double length, int forwardFow, int backwardFow) {
		this.length = length;
		this.forwardFow = forwardFow;
		this.backwardFow = backwardFow;
	}
	
	public double getLength() {
		return length;
	}
	
	public int getForwardFow() {
		return forwardFow;
	}
	
	public int getBackwardFow() {
		return backwardFow;
	}
	
	public static CarType getRandom() {
		return values()[new Random().nextInt(values().length)];
	}
	
}
