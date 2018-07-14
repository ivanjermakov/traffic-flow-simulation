package com.gmail.ivanjermakov1.trafficflowsimulation;

import com.gmail.ivanjermakov1.trafficflowsimulation.type.DrivingDirection;
import com.gmail.ivanjermakov1.trafficflowsimulation.type.RotationDirection;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.Location;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.Vector;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.color.Colors;
import processing.core.PApplet;

import java.util.*;

import static com.gmail.ivanjermakov1.trafficflowsimulation.Cell.CELL_SIZE;
import static java.lang.Math.PI;

public class Car {
	
	private static final double MAX_SPEED = 4;
	
	private static final int LENGTH = CELL_SIZE / 4;
	private static final int WIDTH = CELL_SIZE / 8;
	
	private static final double ACCELERATION = 2;
	private static final double DECELERATION = 6;
	
	private Colors color = Colors.values()[new Random().nextInt(Colors.values().length)];
	
	private DrivingDirection drivingDirection;
	private List<RotationDirection> priorityTurns;
	
	private Location location;
	private Vector speed = new Vector();
	private Vector acceleration;
	private double direction;
	
	private int travelled = 0;
	
	private Location nextCellLocation;
	private boolean isCellChanged = false;
	
	private boolean isRotating = false;
	private Rotation rotation;
	
	public Car(DrivingDirection drivingDirection, Location location) {
		this.drivingDirection = drivingDirection;
		this.location = location;
		
		acceleration = Vector.createVector(drivingDirection, ACCELERATION);
		
		switch (drivingDirection) {
			case TOP:
				direction = 0;
				break;
			case RIGHT:
				direction = PI / 2;
				break;
			case DOWN:
				direction = PI;
				break;
			case LEFT:
				direction = 3 * PI / 2;
				break;
		}
		
		priorityTurns = generatePriorityTurns();
	}
	
	public void draw(PApplet p) {
		p.pushMatrix();
		
		p.fill(color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue());
		p.noStroke();
		p.translate(location.getX(), location.getY());
		p.rotate((float) direction);
		p.rect(0, 0, WIDTH, LENGTH);
		
		p.popMatrix();
	}
	
	public void update() {
		//movement
		speed.add(acceleration);
		speed.limit(MAX_SPEED);
		location.add(speed);
		
		//travelled distance update
		travelled += speed.getLength();
	}
	
	public void setNextCellLocation(Field field) {
		if (!isRotating) {
			//get current cell
			Location cellLocation = field.getCellLocation(location);
			if (nextCellLocation != null) {
				isCellChanged = !cellLocation.equals(nextCellLocation);
			} else {
				isCellChanged = false;
			}
			//get next cell based on direction
			switch (drivingDirection) {
				case TOP:
					nextCellLocation = new Location(cellLocation.getX(),
							(cellLocation.getY() - 1 + field.getHeight()) % field.getHeight());
					break;
				case RIGHT:
					nextCellLocation = new Location((cellLocation.getX() + 1 + field.getWidth()) % field.getWidth(),
							cellLocation.getY());
					break;
				case DOWN:
					nextCellLocation = new Location(cellLocation.getX(),
							(cellLocation.getY() + 1 + field.getHeight()) % field.getHeight());
					break;
				case LEFT:
					nextCellLocation = new Location((cellLocation.getX() - 1 + field.getWidth()) % field.getWidth(),
							cellLocation.getY());
					break;
			}
		}
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
	
	public void detectObstacle(Field field, List<Car> cars) {
		for (Car car : cars) {
			if (car == this) continue;
			if (Location.distance(location, car.location) < Car.LENGTH * 2) {
				brake();
			}
		}
	}
	
	private void brake() {
		speed.add(-DECELERATION);
		System.out.println(speed.getLength());
	}
	
	private static List<RotationDirection> generatePriorityTurns() {
		List<RotationDirection> list = new ArrayList<>(Arrays.asList(RotationDirection.values()));
		Collections.shuffle(list);
		return list;
	}
	
}