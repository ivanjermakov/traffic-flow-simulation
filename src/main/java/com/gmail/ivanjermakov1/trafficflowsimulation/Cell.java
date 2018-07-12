package com.gmail.ivanjermakov1.trafficflowsimulation;

import com.gmail.ivanjermakov1.trafficflowsimulation.type.CellType;
import com.gmail.ivanjermakov1.trafficflowsimulation.util.Location;

public class Cell {

	public static final int CELL_SIZE = 128;
	
	private CellType cellType;
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
	
	public void setCellType(CellType cellType) {
		this.cellType = cellType;
	}
}
