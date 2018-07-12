package com.gmail.ivanjermakov1.trafficflowsimulation;

import processing.core.PApplet;

public class Main extends PApplet {
	
	public static int WIDTH = 1920;
	public static int HEIGHT = 1080;
	
	private static Simulator simulator = new Simulator(Field.createDefaultIntersection7x7());
	
	public static void main(String[] args) {
		PApplet.main("com.gmail.ivanjermakov1.trafficflowsimulation.Main", args);
	}
	
	public void settings() {
		size(Cell.CELL_SIZE * simulator.getField().getWidth(), Cell.CELL_SIZE * simulator.getField().getHeight());
//		fullScreen();
	}
	
	public void setup() {
//		surface.setResizable(true);
		
		simulator.init();
	}
	
	public void draw() {
		simulator.draw(this);
	}
}