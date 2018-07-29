package com.gmail.ivanjermakov1.trafficflowsimulation;

import com.gmail.ivanjermakov1.trafficflowsimulation.entity.Field;
import com.gmail.ivanjermakov1.trafficflowsimulation.entity.Simulator;
import com.gmail.ivanjermakov1.trafficflowsimulation.entity.cell.Cell;
import processing.core.PApplet;

public class Main extends PApplet {
	
	public static boolean debugMode = false;
	
	private static Simulator simulator = new Simulator(Field.createDefaultIntersection7x7());
	
	
	public static void main(String[] args) {
		if (args.length == 1 && args[0].equals("debug")) {
			debugMode = true;
		}
		PApplet.main("com.gmail.ivanjermakov1.trafficflowsimulation.Main", args);
	}
	
	public void settings() {
		size(Cell.CELL_SIZE * simulator.getField().getWidth(), Cell.CELL_SIZE * simulator.getField().getHeight());
//		fullScreen();
	}
	
	public void setup() {
//		surface.setResizable(true);
		simulator.init();
		frameRate(240);
	}
	
	public void draw() {
		simulator.draw(this);
	}
}