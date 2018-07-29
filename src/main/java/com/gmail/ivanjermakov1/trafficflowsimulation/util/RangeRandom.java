package com.gmail.ivanjermakov1.trafficflowsimulation.util;

import java.security.InvalidParameterException;
import java.util.Random;

public class RangeRandom {
	
	public static double random(double from, double to) {
		if (from > to) throw new InvalidParameterException("from value must be less then to value");
		return (new Random().nextDouble() * (to - from)) + from;
	}
	
}
