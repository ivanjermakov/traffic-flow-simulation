package com.gmail.ivanjermakov1.trafficflowsimulation;

import com.gmail.ivanjermakov1.trafficflowsimulation.util.Vector;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.color.Colors;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.Location;

public class Car {

	private Colors color;
	
	private Location location;
	private Vector speed;
	private Vector acceleration;
	private double direction;

	private boolean isRotating;
	
}
