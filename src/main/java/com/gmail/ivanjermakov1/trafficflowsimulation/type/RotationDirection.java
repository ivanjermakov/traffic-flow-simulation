package com.gmail.ivanjermakov1.trafficflowsimulation.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum RotationDirection {
	STRAIGHT,
	LEFT,
	RIGHT;
	//TODO: include turn around
	
	public static List<RotationDirection> generatePriorityTurns() {
		List<RotationDirection> list = new ArrayList<>(Arrays.asList(values()));
		Collections.shuffle(list);
		return list;
	}
	
	public static List<RotationDirection> getPossibleDirections(CellType cellType) {
		switch (cellType) {
			case LEFT_TURN:
				return Arrays.asList(LEFT);
			case RIGHT_TURN:
				return Arrays.asList(RIGHT);
			case INTERSECTION:
				return Arrays.asList(LEFT, STRAIGHT, RIGHT);
			//TODO: develop other cases
		}
		
		return new ArrayList<>();
	}
}
