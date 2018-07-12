package com.gmail.ivanjermakov1.trafficflowsimulation;

import processing.core.PApplet;

import java.util.stream.IntStream;

import static com.gmail.ivanjermakov1.trafficflowsimulation.Cell.CELL_SIZE;
import static processing.core.PConstants.CORNER;

public class Simulator {
	
	private Field field;
	
	
	public Simulator() {
	}
	
	public Simulator(Field field) {
		this.field = field;
	}
	
	public Field getField() {
		return field;
	}
	
	public void init() {
		if (field == null) field = new Field(8, 8);
	}
	
	public void draw(PApplet p) {
		IntStream.range(0, field.getHeight()).forEach(i -> {
			IntStream.range(0, field.getWidth()).forEach(j -> {
				p.pushMatrix();
				
				p.rectMode(CORNER);
				p.translate(i * CELL_SIZE, j * CELL_SIZE);
				
				switch (field.getCell(i, j).getCellType()) {
					case GRASS:
						p.fill(130, 240, 60);
						break;
					case ROAD:
						p.fill(100, 100, 100);
						break;
					case INTERSECTION:
						p.fill(70, 70, 70);
						break;
				}
				
				p.rect(0, 0, CELL_SIZE, CELL_SIZE);
				
				p.popMatrix();
			});
		});
	}
	
	public void setFieldSize(int width, int height) {
		field = new Field(width, height);
	}
}
