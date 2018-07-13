package com.gmail.ivanjermakov1.trafficflowsimulation;

import com.gmail.ivanjermakov1.trafficflowsimulation.type.DrivingDirection;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.Location;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.Vector;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.color.Colors;
import processing.core.PApplet;

import java.util.Random;

import static com.gmail.ivanjermakov1.trafficflowsimulation.Cell.CELL_SIZE;
import static java.lang.Math.PI;

public class Car {

	private static final double MAX_SPEED = 4;
	
	private static final int LENGTH = CELL_SIZE / 4;
	private static final int WIDTH = CELL_SIZE / 8;
	
	private static final double ACCELERATION = 2;
	
	private Colors color = Colors.values()[new Random().nextInt(Colors.values().length)];
	
	private DrivingDirection drivingDirection;
	
	private Location location;
	private Vector speed = new Vector();
	private Vector acceleration;
	private double direction;
	
	private int traveled = 0;
	
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
		speed.add(acceleration);
		speed.limit(MAX_SPEED);
		location.add(speed);
	}
}