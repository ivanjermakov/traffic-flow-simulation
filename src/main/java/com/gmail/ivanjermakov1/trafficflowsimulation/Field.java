package com.gmail.ivanjermakov1.trafficflowsimulation;

import com.gmail.ivanjermakov1.trafficflowsimulation.type.RoadDirection;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.Location;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.gmail.ivanjermakov1.trafficflowsimulation.Cell.CELL_SIZE;
import static com.gmail.ivanjermakov1.trafficflowsimulation.type.CellType.*;
import static com.gmail.ivanjermakov1.trafficflowsimulation.type.RoadDirection.*;
import static processing.core.PConstants.CORNER;

public class Field {
	
	private List<List<Cell>> cells;
	private int width;
	private int height;
	
	public Field(int width, int height) {
		this.width = width;
		this.height = height;
		cells = new ArrayList<>();
		IntStream.range(0, height)
				.forEach(i -> {
					cells.add(new ArrayList<>());
					IntStream.range(0, width)
							.forEach(j -> cells.get(i).add(new Cell(GRASS, new Location(i, j))));
				});
	}
	
	public List<List<Cell>> getCells() {
		return cells;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Cell getCell(int i, int j) {
		try {
			return cells.get(i).get(j);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void draw(PApplet p) {
		IntStream.range(0, getHeight()).forEach(i -> {
			IntStream.range(0, getWidth()).forEach(j -> {
				getCell(i, j).draw(p);
			});
		});
	}
	
	public static Cell getCell(List<List<Cell>> cells, int i, int j) {
		try {
			return cells.get(i).get(j);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Field createDefaultIntersection7x7() {
		Field field = new Field(7, 7);
		IntStream.range(0, 7).forEach(i -> {
			Field.getCell(field.getCells(), i, 3).setCellType(ROAD).setRoadDirection(HORIZONTAL);
			Field.getCell(field.getCells(), 3, i).setCellType(ROAD).setRoadDirection(VERTICAL);
			Field.getCell(field.getCells(), 3, 3).setCellType(INTERSECTION);
		});
		
		return field;
	}
}
