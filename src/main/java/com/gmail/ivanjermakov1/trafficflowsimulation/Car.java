package com.gmail.ivanjermakov1.trafficflowsimulation;

import com.gmail.ivanjermakov1.trafficflowsimulation.type.DrivingDirection;
import com.gmail.ivanjermakov1.trafficflowsimulation.type.RotationDirection;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.Location;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.Vector;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.color.Colors;
import processing.core.PApplet;

import java.util.List;

import static com.gmail.ivanjermakov1.trafficflowsimulation.Cell.CELL_SIZE;
import static com.gmail.ivanjermakov1.trafficflowsimulation.type.CellType.GRASS;
import static com.gmail.ivanjermakov1.trafficflowsimulation.type.CellType.ROAD;
import static com.gmail.ivanjermakov1.trafficflowsimulation.type.RotationDirection.STRAIGHT;
import static java.lang.Math.*;
import static processing.core.PConstants.CENTER;

public class Car {
	
	private static final double MAX_SPEED = 4;
	
	private static final int LENGTH = CELL_SIZE / 4;
	private static final int WIDTH = CELL_SIZE / 8;
	
	private static final double ACCELERATION = 0.1;
	private static final double DECELERATION = 0.4;
	
	private Colors color = Colors.getRandom();
	
	private DrivingDirection drivingDirection;
	private List<RotationDirection> priorityTurns;
	
	private Location location;
	private Vector speed = new Vector();
	private Vector acceleration;
	private double direction;
	
	private boolean isBraking = false;
	
	private int travelled = 0;
	
	private Cell cell;
	
	private boolean isRotating = false;
	private boolean isCompleteRotation = true;
	private Rotation rotation;
	
	
	public Car(DrivingDirection drivingDirection, Location location) {
		this.drivingDirection = drivingDirection;
		this.location = location;
		
		acceleration = Vector.createVector(drivingDirection, ACCELERATION);
		
		direction = setDirection(drivingDirection);

//		priorityTurns = Arrays.asList(LEFT, RIGHT, STRAIGHT);
//		priorityTurns  = Arrays.asList(RIGHT, RIGHT, STRAIGHT);
//		priorityTurns = generatePriorityTurns();
	}
	
	public void draw(PApplet p) {
		p.pushMatrix();
		
		p.fill(color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue());
		p.noStroke();
		p.translate((int) location.getX(), (int) location.getY());
		p.rotate((float) direction);
		p.rectMode(CENTER);
		p.rect(0, 0, WIDTH, LENGTH);
		p.fill(255, 0, 0, 100);
		
		p.popMatrix();
		
		//test
		p.fill(255, 0, 0, 100);
		p.ellipse((int) getHoodLocation(location).getX(), (int) getHoodLocation(location).getY(), LENGTH, LENGTH);
		p.ellipse((int) getBodyLocation(location).getX(), (int) getBodyLocation(location).getY(), LENGTH, LENGTH);
		
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
	}
	
	public void update() {
		if (!isRotating) {
			location.add(speed);
		} else {
			Location newLocation = rotation.getNewLocation(travelled);
			if (newLocation == null) {
				//rotation is finished
				isRotating = false;
				location = rotation.getEndLocation();
				drivingDirection = rotation.getDrivingDirection();
				direction = setDirection(drivingDirection);
				acceleration = Vector.createVector(drivingDirection, speed.getLength());
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
	
	public void detectSideObstacle(List<Car> cars) {
		for (Car car : cars) {
			if (car != this) {
				if (Location.distance(getHoodLocation(location), getBodyLocation(car.location)) <= LENGTH) {
					isBraking = true;
					return;
				}
			}
		}
		isBraking = false;
	}
	
	public void detectForwardObstacle(List<Car> cars) {
		for (Car car : cars) {
			if (car != this) {
				if (Location.distance(getHoodLocation(location), getHoodLocation(car.location)) <= LENGTH) {
					isBraking = true;
					return;
				}
			}
		}
		isBraking = false;
	}
	
	public void detectBodyObstacle(List<Car> cars) {
		for (Car car : cars) {
			if (car != this) {
				if (Location.distance(getBodyLocation(location), getBodyLocation(car.location)) <= LENGTH) {
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
			};
			
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
		speed.limit(MAX_SPEED);
	}
	
	private Location getHoodLocation(Location location) {
		return new Location((int) location.getX() + (int) (sin(direction) * LENGTH),
				(int) location.getY() - (int) (cos(direction) * LENGTH));
	}
	
	private Location getBodyLocation(Location location) {
		return new Location((int) location.getX(), (int) location.getY());
	}
	
	private double setDirection(DrivingDirection drivingDirection) {
		switch (drivingDirection) {
			case TOP:
				return  0;
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