package com.gmail.ivanjermakov1.trafficflowsimulation.entity;

import com.gmail.ivanjermakov1.trafficflowsimulation.entity.cell.CellType;
import com.gmail.ivanjermakov1.trafficflowsimulation.direction.DrivingDirection;
import com.gmail.ivanjermakov1.trafficflowsimulation.direction.RotationDirection;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.Location;
import processing.core.PApplet;

import java.util.List;

import static com.gmail.ivanjermakov1.trafficflowsimulation.entity.cell.Cell.CELL_SIZE;
import static com.gmail.ivanjermakov1.trafficflowsimulation.direction.DrivingDirection.*;
import static java.lang.Math.*;

public class Rotation {
	
	private static final double ACCURACY = 0.5;
	
	private double initialTravelledDistance;
	private double travelledDistance;
	
	private DrivingDirection drivingDirection;
	private RotationDirection rotationDirection;
	
	private Location startLocation;
	private Location endLocation;
	
	private Location currentLocation;
	private Location previousLocation;
	
	private double startRadius;
	private double endRadius;
	
	private Location anchorLocation;
	
	private double length;
	
	public Rotation(DrivingDirection drivingDirection, RotationDirection rotationDirection, Location startLocation, Location endLocation, double initialTravelledDistance) {
		this.drivingDirection = drivingDirection;
		this.rotationDirection = rotationDirection;
		this.startLocation = startLocation;
		this.endLocation = endLocation;
		this.initialTravelledDistance = initialTravelledDistance;
		
		switch (drivingDirection) {
			case TOP:
			case DOWN:
				switch (this.rotationDirection) {
					case LEFT:
						anchorLocation = new Location(endLocation.getX(), startLocation.getY());
						break;
					case RIGHT:
						anchorLocation = new Location(endLocation.getX(), startLocation.getY());
						break;
				}
				startRadius = abs(anchorLocation.getX() - startLocation.getX());
				endRadius = abs(anchorLocation.getY() - endLocation.getY());
				break;
			case LEFT:
			case RIGHT:
				switch (this.rotationDirection) {
					case LEFT:
						anchorLocation = new Location(startLocation.getX(), endLocation.getY());
						break;
					case RIGHT:
						anchorLocation = new Location(startLocation.getX(), endLocation.getY());
						break;
				}
				startRadius = abs(anchorLocation.getY() - startLocation.getY());
				endRadius = abs(anchorLocation.getX() - endLocation.getX());
				break;
		}
		
		
		double startRadiusLength = PI * startRadius / 2;
		double endRadiusLength = PI * endRadius / 2;
		length = startRadiusLength + endRadiusLength / 2;
	}
	
	public Location getEndLocation() {
		return endLocation;
	}
	
	public Location getStartLocation() {
		return startLocation;
	}
	
	public Location getAnchorLocation() {
		return anchorLocation;
	}
	
	public Location getPreviousLocation() {
		return previousLocation;
	}
	
	public Location getNewLocation(double travelled) {
		travelledDistance = travelled - initialTravelledDistance;
		//ratio of completed rotation
		double ratio = travelledDistance / length;
		if (ratio > 1) return null;
		//distance from anchor point
		double radius = (ratio * endRadius) + ((1 - ratio) * startRadius);
		double angle = PApplet.map((float) ratio, 0, 1, 0, (float) PI / 2);
		
		if (previousLocation != null &&
				!Location.closelyEquals(currentLocation, previousLocation, ACCURACY))
			previousLocation = currentLocation;
		
		switch (rotationDirection) {
			case LEFT:
				switch (drivingDirection) {
					case TOP:
						currentLocation = new Location(anchorLocation.getX() + (radius * cos(angle)), anchorLocation.getY() - (radius * sin(angle)));
						break;
					case DOWN:
						currentLocation = new Location(anchorLocation.getX() - (radius * cos(angle)), anchorLocation.getY() + (radius * sin(angle)));
						break;
					case RIGHT:
						currentLocation = new Location(anchorLocation.getX() + (radius * sin(angle)), anchorLocation.getY() + (radius * cos(angle)));
						break;
					case LEFT:
						currentLocation = new Location(anchorLocation.getX() - (radius * sin(angle)), anchorLocation.getY() - (radius * cos(angle)));
						break;
				}
				break;
			case RIGHT:
				switch (drivingDirection) {
					case TOP:
						currentLocation = new Location(anchorLocation.getX() - (radius * cos(angle)), anchorLocation.getY() - (radius * sin(angle)));
						break;
					case DOWN:
						currentLocation = new Location(anchorLocation.getX() + (radius * cos(angle)), anchorLocation.getY() + (radius * sin(angle)));
						break;
					case RIGHT:
						currentLocation = new Location(anchorLocation.getX() + (radius * sin(angle)), anchorLocation.getY() - (radius * cos(angle)));
						break;
					case LEFT:
						currentLocation = new Location(anchorLocation.getX() - (radius * sin(angle)), anchorLocation.getY() + (radius * cos(angle)));
						break;
				}
				break;
		}
		
		if (previousLocation == null) previousLocation = currentLocation;
		
		return currentLocation;
	}
	
	public double getNewDirection(double currentDirection) {
		if (previousLocation == null || Location.closelyEquals(currentLocation, previousLocation, ACCURACY))
			return currentDirection;
		return Location.angle(currentLocation, previousLocation);
	}
	
	public static RotationDirection predictRotationDirection(CellType currentCellType, List<RotationDirection> priorityTurns) {
		List<RotationDirection> rotationDirections = RotationDirection.getPossibleDirections(currentCellType);
		for (RotationDirection priorityTurn : priorityTurns) {
			if (rotationDirections.contains(priorityTurn)) {
				return priorityTurn;
			}
		}
		throw new IllegalStateException("priorityTurns does not contain possible turns or empty.");
	}
	
	public static Location getEndLocation(Field field, Location startLocation, DrivingDirection drivingDirection, RotationDirection rotationDirection) {
		Location endLocation = null;
		Location currentCellLocation = field.getCell(startLocation).getLocation();
		final int OFFSET = CELL_SIZE / 2;
		
		switch (drivingDirection) {
			case TOP:
				switch (rotationDirection) {
					case STRAIGHT:
						endLocation = field.getCarLocation((int) currentCellLocation.getX(), (int) currentCellLocation.getY(), drivingDirection);
						break;
					case LEFT:
						endLocation = field.getCarLocation((int) currentCellLocation.getX() - 1, (int) currentCellLocation.getY(), LEFT);
						endLocation = new Location(endLocation.getX() + OFFSET, endLocation.getY());
						break;
					case RIGHT:
						endLocation = field.getCarLocation((int) currentCellLocation.getX() + 1, (int) currentCellLocation.getY(), RIGHT);
						endLocation = new Location(endLocation.getX() - OFFSET, endLocation.getY());
						break;
				}
				break;
			case DOWN:
				switch (rotationDirection) {
					case STRAIGHT:
						endLocation = field.getCarLocation((int) currentCellLocation.getX(), (int) currentCellLocation.getY(), drivingDirection);
						break;
					case LEFT:
						endLocation = field.getCarLocation((int) currentCellLocation.getX() + 1, (int) currentCellLocation.getY(), RIGHT);
						endLocation = new Location(endLocation.getX() - OFFSET, endLocation.getY());
						break;
					case RIGHT:
						endLocation = field.getCarLocation((int) currentCellLocation.getX() - 1, (int) currentCellLocation.getY(), LEFT);
						endLocation = new Location(endLocation.getX() + OFFSET, endLocation.getY());
						break;
				}
				break;
			case LEFT:
				switch (rotationDirection) {
					case STRAIGHT:
						endLocation = field.getCarLocation((int) currentCellLocation.getX(), (int) currentCellLocation.getY(), drivingDirection);
						break;
					case LEFT:
						endLocation = field.getCarLocation((int) currentCellLocation.getX(), (int) currentCellLocation.getY() + 1, DOWN);
						endLocation = new Location(endLocation.getX(), endLocation.getY() - OFFSET);
						break;
					case RIGHT:
						endLocation = field.getCarLocation((int) currentCellLocation.getX(), (int) currentCellLocation.getY() - 1, TOP);
						endLocation = new Location(endLocation.getX(), endLocation.getY() + OFFSET);
						break;
				}
				break;
			case RIGHT:
				switch (rotationDirection) {
					case STRAIGHT:
						endLocation = field.getCarLocation((int) currentCellLocation.getX(), (int) currentCellLocation.getY(), drivingDirection);
						break;
					case LEFT:
						endLocation = field.getCarLocation((int) currentCellLocation.getX(), (int) currentCellLocation.getY() - 1, TOP);
						endLocation = new Location(endLocation.getX(), endLocation.getY() + OFFSET);
						break;
					case RIGHT:
						endLocation = field.getCarLocation((int) currentCellLocation.getX(), (int) currentCellLocation.getY() + 1, DOWN);
						endLocation = new Location(endLocation.getX(), endLocation.getY() - OFFSET);
						break;
				}
				break;
		}
		return endLocation;
	}
	
	public DrivingDirection getDrivingDirection() {
		switch (rotationDirection) {
			case LEFT:
				switch (drivingDirection) {
					case LEFT:
						return DOWN;
					case RIGHT:
						return TOP;
					case TOP:
						return LEFT;
					case DOWN:
						return RIGHT;
				}
				break;
			case RIGHT:
				switch (drivingDirection) {
					case LEFT:
						return TOP;
					case RIGHT:
						return DOWN;
					case TOP:
						return RIGHT;
					case DOWN:
						return LEFT;
				}
				break;
			default:
				return drivingDirection;
		}
		throw new IllegalStateException();
	}
	
}

//r = t * r1 + (1 - t) * r2