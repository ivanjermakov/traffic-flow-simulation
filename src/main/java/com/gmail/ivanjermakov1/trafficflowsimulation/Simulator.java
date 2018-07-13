package com.gmail.ivanjermakov1.trafficflowsimulation;

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
	}
	
	public void draw(PApplet p) {
		field.draw(p);
	}
	
	public void setFieldSize(int width, int height) {
		field = new Field(width, height);
	}
}
