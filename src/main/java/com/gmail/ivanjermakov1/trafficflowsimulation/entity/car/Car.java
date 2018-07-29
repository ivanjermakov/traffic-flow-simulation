package com.gmail.ivanjermakov1.trafficflowsimulation.entity.car;

import com.gmail.ivanjermakov1.trafficflowsimulation.direction.DrivingDirection;
import com.gmail.ivanjermakov1.trafficflowsimulation.direction.RotationDirection;
import com.gmail.ivanjermakov1.trafficflowsimulation.entity.Field;
import com.gmail.ivanjermakov1.trafficflowsimulation.entity.Rotation;
import com.gmail.ivanjermakov1.trafficflowsimulation.entity.cell.Cell;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.Location;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.RangeRandom;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.Vector;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.color.Colors;
import processing.core.PApplet;

import java.util.List;

import static com.gmail.ivanjermakov1.trafficflowsimulation.Main.debugMode;
import static com.gmail.ivanjermakov1.trafficflowsimulation.direction.RotationDirection.STRAIGHT;
import static com.gmail.ivanjermakov1.trafficflowsimulation.entity.cell.Cell.CELL_SIZE;
import static com.gmail.ivanjermakov1.trafficflowsimulation.entity.cell.CellType.GRASS;
import static com.gmail.ivanjermakov1.trafficflowsimulation.entity.cell.CellType.ROAD;
import static java.lang.Math.*;
import static processing.core.PConstants.CENTER;

public class Car {
	
	public static final int DEFAULT_LENGTH = CELL_SIZE / 4;
	public static final int DEFAULT_WIDTH = CELL_SIZE / 8;
	
	public static final int DEFAULT_FOW = DEFAULT_LENGTH;
	
	private static final double ACCELERATION = 0.1;
	private static final double DECELERATION = 0.2;
	
	private CarType carType;
	
	//TODO: investigate why speed cannot be less than 1 on rotations
	private double maxSpeed;
	private double length;
	private int forwardFow;
	private int backwardFow;
	
	private Colors color = Colors.getRandom();
	
	private DrivingDirection drivingDirection;
	private List<RotationDirection> priorityTurns;
	
	private Location location;
	private Vector speed = new Vector();
	private Vector acceleration;
	private double direction;
	
	private boolean isBraking = false;
	
	private int travelled = 0;
	
	private boolean isRotating = false;
	private boolean isCompleteRotation = true;
	private Rotation rotation;
	
	
	public Car(DrivingDirection drivingDirection, Location location) {
		this.drivingDirection = drivingDirection;
		this.location = location;
		
		acceleration = Vector.createVector(drivingDirection, ACCELERATION);
		
		direction = setDirection(drivingDirection);
		
		maxSpeed = RangeRandom.random(1, 1.5);
		
		carType = CarType.getRandom();
		length = carType.getLength();
		forwardFow = carType.getForwardFow();
		backwardFow = carType.getBackwardFow();
	}
	
	public void draw(PApplet p) {
		p.pushMatrix();
		
		p.fill(color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue());
		p.noStroke();
		p.translate((int) location.getX(), (int) location.getY());
		p.rotate((float) direction);
		p.rectMode(CENTER);
		p.rect(0, 0, DEFAULT_WIDTH, (float) length);
		p.fill(255, 0, 0, 100);
		
		p.popMatrix();
		
		if (debugMode) {
			p.pushMatrix();
			
			p.fill(255, 0, 0, 100);
			p.ellipse((int) getForwardLocation(location).getX(), (int) getForwardLocation(location).getY(), forwardFow, forwardFow);
			p.ellipse((int) getBackwardLocation(location).getX(), (int) getBackwardLocation(location).getY(), backwardFow, backwardFow);
			
			if (isRotating) {
				p.fill(0, 0, 255);
				p.ellipse((float) rotation.getEndLocation().getX(), (float) rotation.getEndLocation().getY(), 10, 10);
				p.ellipse((float) rotation.getStartLocation().getX(), (float) rotation.getStartLocation().getY(), 10, 10);
				p.fill(255, 0, 0);
				p.ellipse((float) rotation.getAnchorLocation().getX(), (float) rotation.getAnchorLocation().getY(), 10, 10);
				p.fill(0);
				p.ellipse((float) location.getX(), (float) location.getY(), 10, 10);
				p.fill(255);
				if (rotation.getPreviousLocation() != null) {
					p.ellipse((float) rotation.getPreviousLocation().getX(), (float) rotation.getPreviousLocation().getY(), 10, 10);
				}
			}
			
			p.popMatrix();
		}
	}
	
	public void update() {
		if (!isRotating) {
			location.add(speed);
		} else {
			Location newLocation = rotation.getNewLocation(travelled);
			if (newLocation == null) {
				//rotation is finished
				isRotating = false;
				drivingDirection = rotation.getDrivingDirection();
				direction = setDirection(drivingDirection);
				location = rotation.getEndLocation();
				acceleration = Vector.createVector(drivingDirection, speed.getLength());
				speed.rotate(drivingDirection);
				rotation = null;
			} else {
				location = newLocation;
				direction = rotation.getNewDirection(direction);
			}
		}
		
		if (isBraking) {
			brake();
		} else {
			accelerate();
		}
		
		//travelled distance update
		travelled += speed.getLength();
	}
	
	public void checkBounds(Field field) {
		if (location.getX() < 0) {
			location.setX(field.getWidth() * CELL_SIZE);
		}
		if (location.getY() <= 0) {
			location.setY(field.getHeight() * CELL_SIZE);
		}
		if (location.getX() > field.getWidth() * CELL_SIZE) {
			location.setX(0);
		}
		if (location.getY() > field.getHeight() * CELL_SIZE) {
			location.setY(0);
		}
	}
	
	public void detectForwardObstacle(List<Car> cars) {
		for (Car car : cars) {
			if (car != this) {
				if (Location.distance(getForwardLocation(location), getForwardLocation(car.location)) <= forwardFow ||
						Location.distance(getForwardLocation(location), getBackwardLocation(car.location)) <= forwardFow) {
					isBraking = true;
					return;
				}
			}
		}
		isBraking = false;
	}
	
	public void detectBackwardObstacle(List<Car> cars) {
		for (Car car : cars) {
			if (car != this) {
				if (Location.distance(getBackwardLocation(location), getBackwardLocation(car.location)) <= backwardFow ||
						Location.distance(getBackwardLocation(location), getForwardLocation(car.location)) <= backwardFow) {
					isBraking = true;
					return;
				}
			}
		}
		isBraking = false;
	}
	
	public void checkRotation(Field field) {
		Cell currentCell = field.getCell(location);
		if (currentCell.getCellType() == ROAD) isCompleteRotation = true;
		
		if (isRotating || !isCompleteRotation) return;
		
		if (currentCell.getCellType() != GRASS && currentCell.getCellType() != ROAD) {
			priorityTurns = RotationDirection.generatePriorityTurns();
			RotationDirection rotationDirection = Rotation.predictRotationDirection(currentCell.getCellType(), priorityTurns);
			
			if (rotationDirection == STRAIGHT) {
				isCompleteRotation = false;
				return;
			}
			
			isCompleteRotation = false;
			isRotating = true;
			rotation = new Rotation(drivingDirection, rotationDirection, location,
					Rotation.getEndLocation(field, location, drivingDirection, rotationDirection), travelled);
		}
	}
	
	private void brake() {
		isBraking = true;
		speed.sub(DECELERATION);
	}
	
	private void accelerate() {
		isBraking = false;
		speed.add(acceleration);
		speed.limit(maxSpeed);
	}
	
	private Location getForwardLocation(Location location) {
		return new Location((int) location.getX() + (int) (sin(direction) * forwardFow),
				(int) location.getY() - (int) (cos(direction) * forwardFow));
	}
	
	private Location getBackwardLocation(Location location) {
		return new Location((int) location.getX() - (sin(direction) * length) / 2,
				(int) location.getY() + (cos(direction) * length) / 2);
	}
	
	private double setDirection(DrivingDirection drivingDirection) {
		switch (drivingDirection) {
			case TOP:
				return 0;
			case RIGHT:
				return PI / 2;
			case DOWN:
				return PI;
			case LEFT:
				return 3 * PI / 2;
		}
		throw new IllegalStateException();
	}
	
}