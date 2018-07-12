package com.gmail.ivanjermakov1.trafficflowsimulation;

import com.gmail.ivanjermakov1.trafficflowsimulation.type.CellType;
import com.gmail.ivanjermakov1.trafficflowsimulation.type.RoadDirection;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.Location;
import processing.core.PApplet;

import static com.gmail.ivanjermakov1.trafficflowsimulation.type.CellType.INTERSECTION;
import static com.gmail.ivanjermakov1.trafficflowsimulation.type.CellType.ROAD;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.CORNER;

public class Cell {
	
	public static final int CELL_SIZE = 128;
	
	private CellType cellType;
	private RoadDirection roadDirection;
	private Location location;
	
	public Cell(CellType cellType, Location location) {
		this.cellType = cellType;
		this.location = location;
	}
	
	public CellType getCellType() {
		return cellType;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public Cell setCellType(CellType cellType) {
		this.cellType = cellType;
		return this;
	}
	
	public Cell setRoadDirection(RoadDirection roadDirection) {
		this.roadDirection = roadDirection;
		return this;
	}
	
	public void draw(PApplet p) {
		p.pushMatrix();
		
		p.noStroke();
		p.rectMode(CORNER);
		p.translate(location.getX() * CELL_SIZE, location.getY() * CELL_SIZE);
		
		switch (cellType) {
			case GRASS:
				p.fill(130, 240, 60);
				break;
			case ROAD:
			case INTERSECTION:
				p.fill(100, 100, 100);
				break;
		}
		
		p.rect(0, 0, CELL_SIZE, CELL_SIZE);
		
		if (cellType == ROAD) {
			p.fill(255);
			p.rectMode(CENTER);
			p.noStroke();
			p.translate(CELL_SIZE / 2, CELL_SIZE / 2);
			switch (roadDirection) {
				case VERTICAL:
					p.rect(-4, 0, CELL_SIZE / 32, CELL_SIZE);
					p.rect(4, 0, CELL_SIZE / 32, CELL_SIZE);
					break;
				case HORIZONTAL:
					p.rect(0, -4, CELL_SIZE, CELL_SIZE / 32);
					p.rect(0, 4, CELL_SIZE, CELL_SIZE / 32);
					break;
			}
		}
		
		if (cellType == INTERSECTION) {
			p.stroke(255);
			p.strokeWeight(CELL_SIZE / 32);
			p.noFill();
			p.rectMode(CENTER);
			p.translate(CELL_SIZE / 2, CELL_SIZE / 2);
			p.rect(0, 0, CELL_SIZE - CELL_SIZE / 32, CELL_SIZE - CELL_SIZE / 32);
		}
		
		p.popMatrix();
		
	}
	
}
