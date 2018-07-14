package com.gmail.ivanjermakov1.trafficflowsimulation;

import com.gmail.ivanjermakov1.trafficflowsimulation.type.DrivingDirection;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.gmail.ivanjermakov1.trafficflowsimulation.Cell.CELL_SIZE;
import static processing.core.PConstants.CORNER;

public class Simulator {
	
	private Field field;
	private List<Car> cars = new ArrayList<>();
	
	
	public Simulator() {
	}
	
	public Simulator(Field field) {
		this.field = field;
	}
	
	public Field getField() {
		return field;
	}
	
	public void init() {
		if (field == null) field = new Field(7, 7);
		cars.add(new Car(DrivingDirection.TOP, field.getCarLocation(3, 6, DrivingDirection.TOP)));
	}
	
	public void draw(PApplet p) {
		field.draw(p);
		cars.forEach(Car::update);
		cars.forEach(car -> car.draw(p));
	}
	
	public void setFieldSize(int width, int height) {
		field = new Field(width, height);
	}
}
