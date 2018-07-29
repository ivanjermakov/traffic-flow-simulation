package com.gmail.ivanjermakov1.trafficflowsimulation.entity;

import com.gmail.ivanjermakov1.trafficflowsimulation.entity.cell.Cell;
import com.gmail.ivanjermakov1.trafficflowsimulation.direction.DrivingDirection;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.Location;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.gmail.ivanjermakov1.trafficflowsimulation.entity.cell.Cell.CELL_SIZE;
import static com.gmail.ivanjermakov1.trafficflowsimulation.entity.cell.CellType.*;
import static com.gmail.ivanjermakov1.trafficflowsimulation.direction.RoadDirection.HORIZONTAL;
import static com.gmail.ivanjermakov1.trafficflowsimulation.direction.RoadDirection.VERTICAL;

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
	
	public Cell getCell(Location location) {
		int i = (int) (location.getX() / CELL_SIZE);
		int j = (int) (location.getY() / CELL_SIZE);
		
		return getCell(i % width, j % height);
	}
	
	public void draw(PApplet p) {
		IntStream.range(0, getHeight()).forEach(i -> {
			IntStream.range(0, getWidth()).forEach(j -> {
				getCell(i, j).draw(p);
			});
		});
	}
	
	public Location getCarLocation(int i, int j, DrivingDirection drivingDirection, int... laneNumber) {
		int laneWidth = (CELL_SIZE / 2) / getCell(i, j).getLaneCount();
		int laneOffset = 0;
		if (laneNumber.length == 1 && laneNumber[0] > 0 && laneNumber[0] <= getCell(i, j).getLaneCount()) {
			laneOffset = laneNumber[0] * laneWidth;
		} else {
			switch (drivingDirection) {
				case TOP:
					return new Location(i * CELL_SIZE + CELL_SIZE / 2 + laneWidth / 2 + laneOffset,
							j * CELL_SIZE + CELL_SIZE / 2);
				case DOWN:
					return new Location(i * CELL_SIZE + CELL_SIZE / 2 - laneWidth / 2 - laneOffset,
							j * CELL_SIZE + CELL_SIZE / 2);
				case LEFT:
					return new Location(i * CELL_SIZE + CELL_SIZE / 2,
							j * CELL_SIZE + CELL_SIZE / 2 - laneWidth / 2 - laneOffset);
				case RIGHT:
					return new Location(i * CELL_SIZE + CELL_SIZE / 2,
							j * CELL_SIZE + CELL_SIZE / 2 + laneWidth / 2 + laneOffset);
			}
		}
		
		return null;
	}
	
	public Location getCellLocation(Location location) {
		return new Location((int) location.getX() / CELL_SIZE, (int) location.getY() / CELL_SIZE);
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
			field.getCell(i, 3).setCellType(ROAD);
			field.getCell(3, i).setCellType(ROAD);
		});
		
		field.setCellsProperties();
		return field;
	}
	
	private void setCellsProperties() {
		IntStream.range(0, width)
				.forEach(i -> {
					IntStream.range(0, height)
							.forEach(j -> {
								Cell cell = getCell(i, j);
								Cell top = getCell((i - 1 + height) % height, j);
								Cell down = getCell((i + 1) % height, j);
								Cell left = getCell(i, (j - 1 + width) % width);
								Cell right = getCell(i, (j + 1) % width);
								if (cell.getCellType() != GRASS) {
									if (top.getCellType() != GRASS && down.getCellType() != GRASS) {
										cell.setRoadDirection(VERTICAL);
									}
									if (left.getCellType() != GRASS && right.getCellType() != GRASS) {
										cell.setRoadDirection(HORIZONTAL);
									}
									if (getConnectionsCount(top, down, right, left) == 3) {
										if (top.getCellType() != ROAD) cell.setCellType(INTERSECTION_WITHOUT_TOP);
										if (down.getCellType() != ROAD) cell.setCellType(INTERSECTION_WITHOUT_DOWN);
										if (left.getCellType() != ROAD) cell.setCellType(INTERSECTION_WITHOUT_LEFT);
										if (right.getCellType() != ROAD) cell.setCellType(INTERSECTION_WITHOUT_RIGHT);
									}
									if (getConnectionsCount(top, down, right, left) == 4) {
										cell.setCellType(INTERSECTION);
									}
								}
							});
				});
	}
	
	private static int getConnectionsCount(Cell top, Cell down, Cell right, Cell left) {
		return (int) Stream.of(top, down, right, left)
				.filter(cell -> cell.getCellType() != GRASS)
				.count();
	}
	
}
