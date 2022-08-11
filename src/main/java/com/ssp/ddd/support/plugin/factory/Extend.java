/**
 * 
 */
package com.ssp.ddd.support.plugin.factory;

/**
 * The relation of the value object.
 * @author fangang
 */
public class Extend {
	private String name;

	private String clazz;

	private boolean isAggregation;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public boolean isAggregation() {
		return isAggregation;
	}

	public void setAggregation(boolean aggregation) {
		isAggregation = aggregation;
	}
}
