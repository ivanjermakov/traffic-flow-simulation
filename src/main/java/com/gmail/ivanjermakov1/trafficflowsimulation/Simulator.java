package com.gmail.ivanjermakov1.trafficflowsimulation;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

import static com.gmail.ivanjermakov1.trafficflowsimulation.type.DrivingDirection.*;

public class Simulator {
	
	private Field field;
	private List<Car> cars = new ArrayList<>();
	
	
	public Simulator(Field field) {
		this.field = field;
	}
	
	public Field getField() {
		return field;
	}
	
	public void init() {
		if (field == null) field = new Field(7, 7);
		cars.add(new Car(TOP, field.getCarLocation(3, 6, TOP)));
		cars.add(new Car(DOWN, field.getCarLocation(3, 0, DOWN)));
//		cars.add(new Car(DOWN, field.getCarLocation(3, 1, TOP)));
		cars.add(new Car(RIGHT, field.getCarLocation(1, 3, RIGHT)));
		cars.add(new Car(TOP, field.getCarLocation(3, 0, TOP)));
	}
	
	public void draw(PApplet p) {
		field.draw(p);
		cars.forEach(car -> {
			car.checkBounds(field);
			car.setNextCellLocation(field);
			car.detectBodyObstacle(cars);
			car.detectForwardObstacle(cars);
			car.detectSideObstacle(cars);
			car.update();
			car.draw(p);
		});
	}
	
	public void setFieldSize(int width, int height) {
		field = new Field(width, height);
	}
	
}
