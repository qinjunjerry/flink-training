package com.ververica.flink.training.exercises;

import java.util.Objects;

@SuppressWarnings("checkstyle:Missing a Javadoc comment")
public class SimpleMeasurement {
	private int sensorId;
	private double value;
	private String location;

	/**
	 * This is a method.
 	 */
	public SimpleMeasurement() {
	}

	/**
	 * This is a method.
	 */
	public int getSensorId() {
		return sensorId;
	}

	/**
	 * This is a method.
	 */
	public void setSensorId(int sensorId) {
		this.sensorId = sensorId;
	}

	/**
	 * This is a method.
	 */
	public double getValue() {
		return value;
	}

	/**
	 * This is a method.
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * This is a method.
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * This is a method.
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "SimpleMeasurement{" +
				"sensorId=" + sensorId +
				", value=" + value +
				", location='" + location + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SimpleMeasurement that = (SimpleMeasurement) o;
		return sensorId == that.sensorId && Double.compare(that.value, value) == 0 && Objects.equals(location, that.location);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sensorId, value, location);
	}
}
