package com.gmail.ivanjermakov1.trafficflowsimulation.util.color;

public enum Colors {
	RED(new RGBColor(255, 0, 0)),
	GREEN(new RGBColor(0, 255, 0)),
	BLUE(new RGBColor(0, 0, 255)),
	WHITE(new RGBColor(255, 255, 255));

	private RGBColor color;
	
	Colors(RGBColor color) {
		this.color = color;
	}
	
	public RGBColor getColor() {
		return color;
	}
}
