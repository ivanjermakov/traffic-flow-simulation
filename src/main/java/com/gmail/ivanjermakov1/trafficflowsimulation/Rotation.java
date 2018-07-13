package com.gmail.ivanjermakov1.trafficflowsimulation;

import com.gmail.ivanjermakov1.trafficflowsimulation.type.DrivingDirection;
import com.gmail.ivanjermakov1.trafficflowsimulation.type.RotationDirection;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.Location;

import static java.lang.Math.*;

public class Rotation {
	
	private DrivingDirection drivingDirection;
	private RotationDirection rotationDirection;
	
	private Location startLocation;
	private Location endLocation;
	
	private int startRadius;
	private int endRadius;
	
	private Location anchorLocation;
	
	private double length;
	
	public Rotation(DrivingDirection drivingDirection, RotationDirection rotationDirection, Location startLocation, Location endLocation) {
		this.drivingDirection = drivingDirection;
		this.rotationDirection = rotationDirection;
		this.startLocation = startLocation;
		this.endLocation = endLocation;
		
		switch (this.rotationDirection) {
			case LEFT:
				anchorLocation = new Location(this.startLocation.getY(), this.endLocation.getX());
				break;
			case RIGHT:
				anchorLocation = new Location(this.startLocation.getX(), this.endLocation.getY());
				break;
			case TURN_AROUND:
				break;
		}
		
		switch (drivingDirection) {
			case TOP:
			case DOWN:
				startRadius = abs(anchorLocation.getX() - startLocation.getX());
				endRadius = abs(anchorLocation.getX() - endLocation.getX());
				break;
			case LEFT:
			case RIGHT:
				startRadius = abs(anchorLocation.getY() - startLocation.getY());
				endRadius = abs(anchorLocation.getY() - endLocation.getY());
				break;
		}
		
		double startRadiusLength = PI * startRadius / 2;
		double endRadiusLength = PI * endRadius / 2;
		length = startRadiusLength + endRadiusLength / 2;
	}
	
}

//t = l0 / l
//r = t * r1 + (1 - t) * r2
//same with angle