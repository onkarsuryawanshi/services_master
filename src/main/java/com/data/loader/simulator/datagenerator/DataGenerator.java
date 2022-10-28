package com.data.loader.simulator.datagenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.time.Instant;

public class DataGenerator {
	
	public String generateIPAddress() {
		return randomNumber() + "." + randomNumber() + "." + randomNumber() + "." + randomNumber();
	}
	
	public int randomNumber() {
		return new Random().nextInt((255 - 1) + 1) + 1;
	}
	
	public int generateNumericData() {
		Random random = new Random();
		return random.nextInt(100);
	}
	
	public Float generateFloatData() {
		Random random = new Random();
		return (random.nextFloat() * 100);
	}
	
	public long generatePositiveNegativeNumericData() {
		long myRandomLong = (long) (Math.random() * 500 * (Math.random() > 0.5 ? 1 : -1));
		return myRandomLong;
	}
	
	public double generatePositiveNegativeDecimalData() {
		double myRandomLong = (Math.random() * 500 * (Math.random() > 0.5 ? 1 : -1));
		return myRandomLong;
	}
	
	public long generateDateTimeData() {
		return Instant.now().toEpochMilli();
	}
	
	public boolean generateBooleanData() {
		Random random = new Random();
		return random.nextBoolean();
	}
	
	public String generateRadioTypeData() {
		Random random = new Random();
		return random.nextBoolean() ? "BN" : "RN";
	}
	
	public List<Double> generateVectorData() {
		List<Double> vectorList = new ArrayList<>();
		for (int i = 0; i < 4; i++)
			vectorList.add(Math.random() * 500 * (Math.random() > 0.5 ? 1 : -1));
		return vectorList;
	}
	
	public String generateSoftwareVersionData() {
		String[] deviceNames = { "Fire OS 1.x", "Fire OS 2.x", "Flyme OS 1.x.x", "Flyme OS 6.x.x", "HTC Sense 1.x",
		        "ZenUI 1.0 – based on Android" };
		int length = deviceNames.length;
		Random random = new Random();
		int index = random.nextInt(100) % length;
		return deviceNames[index];
	}
	
	public String generateRandomStringData() {
		String[] deviceNames = { "OnePlus", "Samsung", "Realme", "Tarana", "OL", "Vivo" };
		int length = deviceNames.length;
		Random random = new Random();
		int index = random.nextInt(100) % length;
		return deviceNames[index];
	}
}
